package it.polimi.ingsw.model.cardReleted.cards;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.model.cardReleted.cardFactories.GoldCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.ObjectiveCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.ResourceCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.StartCardFactory;

/**
 * This class is used to retrieve any type of card from the corresponding CardLookUp
 * being ResourceCard, ObjectiveCard, StartCard or GoldCard.
 */
public class CardTable {
    /** The CardLookUp for ObjectiveCard */
    private final CardLookUp<ObjectiveCard> cardLookUpObjective;
    /** The CardLookUp for StartCard */
    private final CardLookUp<StartCard> cardLookUpStartCard;
    /** The CardLookUp for ResourceCard */
    private final CardLookUp<ResourceCard> cardLookUpResourceCard;
    /** The CardLookUp for GoldCard */
    private final CardLookUp<GoldCard> cardLookUpGoldCard;

    /**
     * Constructor for CardTable.
     * @param cardJsonPath the path to the json file containing the cards
     * @param cardJsonName the name of the json file containing the cards
     * @param outDirPath the path to the directory where the binary files will be saved
     */
    public CardTable(String cardJsonPath, String cardJsonName, String outDirPath){
        this.cardLookUpObjective = new CardLookUp<>(new ObjectiveCardFactory(cardJsonPath+cardJsonName, outDirPath).getCards(Configs.objectiveCardBinFileName));
        this.cardLookUpStartCard = new CardLookUp<>(new StartCardFactory(cardJsonPath+cardJsonName, outDirPath).getCards(Configs.startCardBinFileName));
        this.cardLookUpResourceCard = new CardLookUp<>(new ResourceCardFactory(cardJsonPath+cardJsonName, outDirPath).getCards(Configs.resourceCardBinFileName));
        this.cardLookUpGoldCard = new CardLookUp<>(new GoldCardFactory(cardJsonPath+cardJsonName, outDirPath).getCards(Configs.goldCardBinFileName));
    }

    /** @return the CardLookUp for ObjectiveCard */
    public CardLookUp<ObjectiveCard> getCardLookUpObjective() {
        return cardLookUpObjective;
    }

    /** @return the CardLookUp for StartCard */
    public CardLookUp<StartCard> getCardLookUpStartCard() {
        return cardLookUpStartCard;
    }

    /** @return the CardLookUp for ResourceCard */
    public CardLookUp<ResourceCard> getCardLookUpResourceCard() {
        return cardLookUpResourceCard;
    }

    /** @return the CardLookUp for GoldCard */
    public CardLookUp<GoldCard> getCardLookUpGoldCard() {
        return cardLookUpGoldCard;
    }
}
