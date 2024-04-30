package it.polimi.ingsw.view.TUI.cardDrawing;

import it.polimi.ingsw.model.cardReleted.cardFactories.GoldCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.ObjectiveCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.ResourceCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.StartCardFactory;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.CollectableCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.DiagonalCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.LCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.utilities.Pair;

import java.io.*;
import java.util.Queue;

public class CardMuseumFactory {
    private final CardMuseum cardMuseum;
    private final String folderPath;
    public static final String fileName = "CardMuseum.bin";

    public CardMuseumFactory(String folderPath) {
        this.folderPath = folderPath;
        cardMuseum = buildMuseum(false);
    }

    public CardMuseumFactory(String folderPath, boolean forceReload) {
        this.folderPath = folderPath;
        cardMuseum = buildMuseum(forceReload);
    }

    private void saveMuseum(CardMuseum cardMuseum) throws IOException {
        FileOutputStream file = new FileOutputStream(this.folderPath + fileName);
        ObjectOutputStream out = new ObjectOutputStream(file);

        out.writeObject(cardMuseum);
        out.flush();
        out.close();
    }

    private CardMuseum loadMuseum() throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(this.folderPath + fileName);
        ObjectInputStream in = new ObjectInputStream(file);

        CardMuseum museum = (CardMuseum) in.readObject();
        in.close();

        if(museum == null)
            throw new IOException("CardMuseum is null");

        System.out.println("CardMuseum loaded of length: " + museum.getSize());
        return museum;
    }

    private CardMuseum buildMuseum(boolean forceReload) {
        try {
            if(forceReload)
                throw new IOException("Forced reload");

            return loadMuseum();
        } catch (IOException | ClassNotFoundException e) {
            String sourceFileName = "cards.json";

            CardMuseum cardMuseum = new CardMuseum();

            Queue<ResourceCard> resourceCards = new ResourceCardFactory(folderPath+sourceFileName, folderPath).getCards();
            resourceCards.forEach(card -> cardMuseum.set(card.getId(), CardPainter.drawResourceCard(card)));

            System.out.println("Resource cards loaded: " + resourceCards.size());

            Queue<GoldCard> goldCards = new GoldCardFactory(folderPath+sourceFileName, folderPath).getCards();
            goldCards.forEach(card -> cardMuseum.set(card.getId(), CardPainter.drawGoldCard(card)));

            System.out.println("Gold cards loaded: " + goldCards.size());

            Queue<StartCard> startCards = new StartCardFactory(folderPath+sourceFileName, folderPath).getCards();
            startCards.forEach(card -> cardMuseum.set(card.getId(), CardPainter.drawStartCard(card)));

            System.out.println("Start cards loaded: " + startCards.size());

            ObjectiveCardFactory objectiveCardFactory = new ObjectiveCardFactory(folderPath+sourceFileName, folderPath);

            Queue<Pair<ObjectiveCard, CollectableCardPointMultiplier>> objectiveCardsWithCollectableMultiplier = objectiveCardFactory.getCardsWithCollectableMultiplier();
            objectiveCardsWithCollectableMultiplier.forEach(pair -> cardMuseum.set(pair.first().getId(), CardPainter.drawObjectiveCardCollectableMultiplier(pair.first(), pair.second())));
            System.out.println("Objective cards with collectable multiplier loaded: " + objectiveCardsWithCollectableMultiplier.size());

            Queue<Pair<ObjectiveCard, LCardPointMultiplier>> objectiveCardsWithLMultiplier = objectiveCardFactory.getCardsWithLMultiplier();
            objectiveCardsWithLMultiplier.forEach(pair -> cardMuseum.set(pair.first().getId(), CardPainter.drawObjectiveCardLMultiplier(pair.first(), pair.second())));
            System.out.println("Objective cards with L multiplier loaded: " + objectiveCardsWithLMultiplier.size());

            Queue<Pair<ObjectiveCard, DiagonalCardPointMultiplier>> objectiveCardsWithDiagonalMultiplier = objectiveCardFactory.getCardsWithDiagonalMultiplier();
            objectiveCardsWithDiagonalMultiplier.forEach(pair -> cardMuseum.set(pair.first().getId(), CardPainter.drawObjectiveCardDiagonalMultiplier(pair.first(), pair.second())));
            System.out.println("Objective cards with diagonal multiplier loaded: " + objectiveCardsWithDiagonalMultiplier.size());


            System.out.println("CardMuseumFactory created of length: " + cardMuseum.getSize());

            try {
                saveMuseum(cardMuseum);
                return loadMuseum();
            } catch (IOException | ClassNotFoundException ioException) {
                throw new RuntimeException(ioException);
            }
        }
    }

    public CardMuseum getCardMuseum() {
        return cardMuseum;
    }

    public static void main(String[] args){
        CardMuseum museum = new CardMuseumFactory("./cards/", true).getCardMuseum();

        for(int key : museum.getCards().keySet()){
            System.out.println("Card ID: " + key);
            System.out.println(museum.get(key).get(CardFace.FRONT).toString());
        }
    }
}
