package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.lightModel.LightDeck;

public class DeckDiffBufferDraw extends DeckDiff{
    private final LightCard card;
    private final Integer position;
    /**
     * @param deck the deck where the card is drawn
     * @param card the card drawn from the buffer
     * @param position the position in the buffer where the card is drawn
     */
    DeckDiffBufferDraw(DrawableCard deck, LightCard card, Integer position) {
        super(deck);
        this.card = card;
        this.position = position;
    }
    /**
     * @param lightDeck the deck to which the diff is applied
     */
    public void apply(LightDeck lightDeck) {
        if(super.deck == DrawableCard.GOLDCARD) {
            lightDeck.substituteGoldBufferCard(card, position);
        }else if(super.deck == DrawableCard.RESOURCECARD){
            lightDeck.substituteResourceBufferCard(card, position);
        }
    }
}
