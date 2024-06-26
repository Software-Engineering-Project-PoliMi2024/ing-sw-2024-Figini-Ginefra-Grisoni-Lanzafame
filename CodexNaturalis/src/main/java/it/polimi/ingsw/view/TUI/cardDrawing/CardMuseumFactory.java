package it.polimi.ingsw.view.TUI.cardDrawing;

import it.polimi.ingsw.Configs;
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
import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;

import java.io.*;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * This class is a factory of CardMuseum. It can create a CardMuseum from the cards json file and save it to a binary file.
 */
public class CardMuseumFactory {
    /** The card museum to create. */
    private final CardMuseum cardMuseum;

    /** The folder path where the json file is stored. */
    private final String inFolderResourcePath;
    /** The name of the binary file. */
    private final String outFolderPath;
    /** the name of the bin file containing the TUI render of the card using emojis*/
    public static final String fileName = Configs.CardMuseumFileName;

    /**
     * Creates a new CardMuseumFactory.
     * @param inFolderResourcePath The folder path where the binary file is stored in the resources' folder.
     * @param outFolderPath The folder path where the binary file will be saved.
     */
    public CardMuseumFactory(String inFolderResourcePath, String outFolderPath) {
        this.inFolderResourcePath = inFolderResourcePath;
        this.outFolderPath = outFolderPath;
        cardMuseum = buildMuseum(false);
    }

    /**
     * Creates a new CardMuseumFactory with the possibility to force the reloading.
     * @param inFolderResourcePath The folder path where the binary file is stored.
     * @param outFolderPath The folder path where the binary file will be saved.
     * @param forceReload True if the CardMuseum must be reloaded from the json file.
     */
    public CardMuseumFactory(String inFolderResourcePath, String outFolderPath, boolean forceReload) {
        this.inFolderResourcePath = inFolderResourcePath;
        this.outFolderPath = outFolderPath;
        cardMuseum = buildMuseum(forceReload);
    }

    /**
     * Saves the CardMuseum to a binary file.
     * @param cardMuseum The CardMuseum to save.
     * @throws IOException If an I/O error occurs.
     */
    private void saveMuseum(CardMuseum cardMuseum) throws IOException {
        FileOutputStream file = new FileOutputStream(this.outFolderPath + fileName);
        ObjectOutputStream out = new ObjectOutputStream(file);

        out.writeObject(cardMuseum);
        out.flush();
        out.close();
    }

    /**
     * Loads the CardMuseum from a binary file saved in the DataFolder.
     * @return The CardMuseum loaded.
     * @throws IOException If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    private CardMuseum loadMuseum() throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(this.outFolderPath + fileName);
        ObjectInputStream in = new ObjectInputStream(file);

        CardMuseum museum = (CardMuseum) in.readObject();
        in.close();

        if(museum == null)
            throw new IOException("CardMuseum is null");

        if(Configs.debugMode)
            System.out.println("CardMuseum loaded of length: " + museum.getSize());
        return museum;
    }

    /**
     * Builds the CardMuseum.
     * @param forceReload True if the CardMuseum must be reloaded from the json file.
     * @return The CardMuseum built.
     */
    private CardMuseum buildMuseum(boolean forceReload) {
        try {
            // If the CardMuseum must be reloaded, throw an IOException to force the creation of a new CardMuseum
            if(forceReload)
                throw new IOException("Forced reload");

            // Try to load the CardMuseum from the binary file
            return loadMuseum();

        } catch (IOException | ClassNotFoundException e) {
            if(Configs.debugMode)
                System.out.println("CardMuseum not found, creating a new one...");
            // If the CardMuseum cannot be loaded, create a new CardMuseum

            String sourceFileName = Configs.CardJSONFileName;

            CardMuseum cardMuseum = new CardMuseum();

            Queue<ResourceCard> resourceCards = new ResourceCardFactory(inFolderResourcePath + sourceFileName, outFolderPath).getCards(Configs.resourceCardBinFileName);
            resourceCards.forEach(card -> cardMuseum.set(card.getIdFront(), CardPainter.drawResourceCard(card)));

            if(Configs.debugMode)
                System.out.println("Resource cards loaded: " + resourceCards.size());

            Queue<GoldCard> goldCards = new GoldCardFactory(inFolderResourcePath +sourceFileName, outFolderPath).getCards(Configs.goldCardBinFileName);
            goldCards.forEach(card -> cardMuseum.set(card.getIdFront(), CardPainter.drawGoldCard(card)));

            if(Configs.debugMode)
                System.out.println("Gold cards loaded: " + goldCards.size());

            Queue<StartCard> startCards = new StartCardFactory(inFolderResourcePath +sourceFileName, outFolderPath).getCards(Configs.startCardBinFileName);
            startCards.forEach(card -> cardMuseum.set(card.getIdFront(), CardPainter.drawStartCard(card)));

            if(Configs.debugMode)
                System.out.println("Start cards loaded: " + startCards.size());

            ObjectiveCardFactory objectiveCardFactory = new ObjectiveCardFactory(inFolderResourcePath +sourceFileName, outFolderPath);

            Queue<Pair<ObjectiveCard, CollectableCardPointMultiplier>> objectiveCardsWithCollectableMultiplier = objectiveCardFactory.getCardsWithCollectableMultiplier();
            objectiveCardsWithCollectableMultiplier.forEach(pair -> cardMuseum.set(pair.first().getIdFront(), CardPainter.drawObjectiveCardCollectableMultiplier(pair.first(), pair.second())));
            if(Configs.debugMode)
                System.out.println("Objective cards with collectable multiplier loaded: " + objectiveCardsWithCollectableMultiplier.size());

            Queue<Pair<ObjectiveCard, LCardPointMultiplier>> objectiveCardsWithLMultiplier = objectiveCardFactory.getCardsWithLMultiplier();
            objectiveCardsWithLMultiplier.forEach(pair -> cardMuseum.set(pair.first().getIdFront(), CardPainter.drawObjectiveCardLMultiplier(pair.first(), pair.second())));
            if(Configs.debugMode)
                System.out.println("Objective cards with L multiplier loaded: " + objectiveCardsWithLMultiplier.size());

            Queue<Pair<ObjectiveCard, DiagonalCardPointMultiplier>> objectiveCardsWithDiagonalMultiplier = objectiveCardFactory.getCardsWithDiagonalMultiplier();
            objectiveCardsWithDiagonalMultiplier.forEach(pair -> cardMuseum.set(pair.first().getIdFront(), CardPainter.drawObjectiveCardDiagonalMultiplier(pair.first(), pair.second())));
            if(Configs.debugMode)
                System.out.println("Objective cards with diagonal multiplier loaded: " + objectiveCardsWithDiagonalMultiplier.size());


            if(Configs.debugMode)
                System.out.println("CardMuseumFactory created of length: " + cardMuseum.getSize());

            Set<Integer> backsIds = new HashSet<>();
            for(ResourceCard card : resourceCards){
                backsIds.add(card.getIdBack());
            }
            for(GoldCard card : goldCards){
                backsIds.add(card.getIdBack());
            }
            for(int i : backsIds){
                Drawable back = cardMuseum.get(i).get(CardFace.BACK);
                cardMuseum.setBackFromId(i, back);
            }
            try {
                // Save the CardMuseum to the binary file
                saveMuseum(cardMuseum);
                return loadMuseum();
            } catch (IOException | ClassNotFoundException ioException) {
                throw new RuntimeException(ioException);
            }
        }
    }

    /**
     * Gets the CardMuseum.
     * @return The CardMuseum.
     */
    public CardMuseum getCardMuseum() {
        return cardMuseum;
    }
}
