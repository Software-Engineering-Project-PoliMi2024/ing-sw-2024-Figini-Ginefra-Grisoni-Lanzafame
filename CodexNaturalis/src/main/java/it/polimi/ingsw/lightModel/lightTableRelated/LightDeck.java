package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

public class LightDeck implements Differentiable {
    private final LightCard[] cardBuffer;
    private LightBack deckBack;

    /**
     * Constructor of the class
     */
    public LightDeck() {
        this.cardBuffer = new LightCard[2];
    }
    public LightCard[] getCardBuffer() {
        return cardBuffer;
    }
    public LightBack getDeckBack() {
        return deckBack;
    }
    /**
     * @param cardDeck the resource of the first gold card on top of the deck
     */
    public void setTopDeckCard(LightBack cardDeck) {
        this.deckBack = cardDeck;
    }
    /**
     * @param card the card to add
     * @param position the position where to add the new card and remove the old one
     */
    public void substituteBufferCard(LightCard card, Integer position) {
        this.cardBuffer[position] = card;
    }

}
