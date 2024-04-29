package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

public class LightDeck implements Differentiable {
    private final LightCard[] cardBuffer;
    private Resource cardDeck;

    /**
     * Constructor of the class
     */
    public LightDeck() {
        this.cardBuffer = new LightCard[1];
    }
    public LightCard[] getCardBuffer() {
        return cardBuffer;
    }
    public Resource getCardDeck() {
        return cardDeck;
    }
    /**
     * @param cardDeck the resource of the first gold card on top of the deck
     */
    public void setTopDeckCard(Resource cardDeck) {
        this.cardDeck = cardDeck;
    }
    /**
     * @param card the card to add
     * @param position the position where to add the new card and remove the old one
     */
    public void substituteBufferCard(LightCard card, Integer position) {
        this.cardBuffer[position] = card;
    }

}
