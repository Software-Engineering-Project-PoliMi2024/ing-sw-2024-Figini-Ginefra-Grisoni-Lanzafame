package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightDeck;

public class DeckDiffBufferDraw extends DeckDiff{
    private final LightCard card;
    private final Integer position;
    /**
     * @param card the card drawn from the buffer
     * @param position the position in the buffer where the card is drawn
     */
    DeckDiffBufferDraw(LightCard card, Integer position) {
        this.card = card;
        this.position = position;
    }
    /**
     * @param lightDeck the deck to which the diff is applied
     */
    public void apply(LightDeck lightDeck) {
            lightDeck.substituteBufferCard(card, position);
    }
}
