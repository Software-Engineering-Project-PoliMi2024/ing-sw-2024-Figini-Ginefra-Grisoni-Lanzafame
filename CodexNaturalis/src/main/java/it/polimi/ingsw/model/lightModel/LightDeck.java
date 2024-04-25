package it.polimi.ingsw.model.lightModel;

import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.lightModel.diffs.*;

public class LightDeck implements ModelDifferentiable<DeckDiff>{
    private final LightCard[] ResourceCardBuffer;
    private final LightCard[] GoldCardBuffer;
    private Resource resourceCardDeck;
    private Resource goldCardDeck;
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
    public void setGoldCardDeck(Resource goldCardDeck) {
        this.goldCardDeck = goldCardDeck;
    }
    public void setResourceCardDeck(Resource resourceCardDeck) {
        this.resourceCardDeck = resourceCardDeck;
    }
    /**
     * Apply the diff to the deck
     * @param diff the diff to apply
     * LightCard is the card to add or remove
     * the first Integer identifies the deck to modify
     * the second Integer identifies which card to modify, from buffer or from the actual deck
     */
    public void applyDiff(DeckDiff diff) {
        diff.apply(this);
    }
    public void substituteGoldBufferCard(LightCard card, Integer position) {
        this.ResourceCardBuffer[position] = card;
    }
    public void substituteResourceBufferCard(LightCard card, Integer position) {
        this.GoldCardBuffer[position] = card;
    }
}
