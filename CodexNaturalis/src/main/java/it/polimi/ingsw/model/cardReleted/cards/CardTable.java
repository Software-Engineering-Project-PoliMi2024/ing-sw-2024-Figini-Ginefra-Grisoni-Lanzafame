package it.polimi.ingsw.model.cardReleted.cards;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.model.cardReleted.cardFactories.GoldCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.ObjectiveCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.ResourceCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.StartCardFactory;

public class CardTable {
    private final CardLookUp<ObjectiveCard> cardLookUpObjective;
    private final CardLookUp<StartCard> cardLookUpStartCard;
    private final CardLookUp<ResourceCard> cardLookUpResourceCard;
    private final CardLookUp<GoldCard> cardLookUpGoldCard;

    public CardTable(String cardJsonPath, String cardJsonName, String outDirPath){
        this.cardLookUpObjective = new CardLookUp<>(new ObjectiveCardFactory(cardJsonPath+cardJsonName, outDirPath).getCards(Configs.objectiveCardBinFileName));
        this.cardLookUpStartCard = new CardLookUp<>(new StartCardFactory(cardJsonPath+cardJsonName, outDirPath).getCards(Configs.startCardBinFileName));
        this.cardLookUpResourceCard = new CardLookUp<>(new ResourceCardFactory(cardJsonPath+cardJsonName, outDirPath).getCards(Configs.resourceCardBinFileName));
        this.cardLookUpGoldCard = new CardLookUp<>(new GoldCardFactory(cardJsonPath+cardJsonName, outDirPath).getCards(Configs.goldCardBinFileName));
    }

    public CardLookUp<ObjectiveCard> getCardLookUpObjective() {
        return cardLookUpObjective;
    }

    public CardLookUp<StartCard> getCardLookUpStartCard() {
        return cardLookUpStartCard;
    }

    public CardLookUp<ResourceCard> getCardLookUpResourceCard() {
        return cardLookUpResourceCard;
    }

    public CardLookUp<GoldCard> getCardLookUpGoldCard() {
        return cardLookUpGoldCard;
    }
}