package it.polimi.ingsw.lightModel;

import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

public class LightDeck implements Differentiable {
    private final LightCard[] ResourceCardBuffer;
    private final LightCard[] GoldCardBuffer;
    private Resource resourceCardDeck;
    private Resource goldCardDeck;

    /**
     * Constructor of the class
     */
    public LightDeck() {
        this.ResourceCardBuffer = new LightCard[1];
        this.GoldCardBuffer = new LightCard[1];
    }
    public LightCard[] getResourceCardBuffer() {
        return ResourceCardBuffer;
    }
    public LightCard[] getGoldCardBuffer() {
        return GoldCardBuffer;
    }
    public Resource getResourceCardDeck() {
        return resourceCardDeck;
    }
    public Resource getGoldCardDeck() {
        return goldCardDeck;
    }
    /**
     * @param goldCardDeck the resource of the first gold card on top of the deck
     */
    public void setGoldCardDeck(Resource goldCardDeck) {
        this.goldCardDeck = goldCardDeck;
    }

    /**
     * @param resourceCardDeck the resource of the first resource card on top of the deck
     */
    public void setResourceCardDeck(Resource resourceCardDeck) {
        this.resourceCardDeck = resourceCardDeck;
    }
    /**
     * @param card the card to add
     * @param position the position where to add the new card and remove the old one
     */
    public void substituteGoldBufferCard(LightCard card, Integer position) {
        this.ResourceCardBuffer[position] = card;
    }

    /**
     * @param card the card to add
     * @param position the position where to add the new card and remove the old one
     */
    public void substituteResourceBufferCard(LightCard card, Integer position) {
        this.GoldCardBuffer[position] = card;
    }
}