package it.polimi.ingsw.lightModel.diffs.game.deckDiffs;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

/**
 * This class contains the update to the lightModel for showing a new card in the buffer after a draw
 */
public class DeckDiffBufferDraw extends DeckDiff{
    /** the new lightCard shown in the buffer */
    private final LightCard card;
    /** the position in the buffer where the card will be place */
    private final Integer position;
    /** the type of the card drawn (GoldCard or ResourceCard) */
    private final DrawableCard type;
    /**
     * @param card the new lightCard shown in the buffer
     * @param position the position in the buffer where the card will be place
     * @param type the type of the card drawn (GoldCard or ResourceCard)
     */
    public DeckDiffBufferDraw(LightCard card, Integer position, DrawableCard type) {
        this.card = card;
        this.position = position;
        this.type = type;
    }

    /**
     * This method sets the new lightCard in the buffer
     * @param lightGame the lightGame to update
     */
        @Override
    public void apply(LightGame lightGame) {
        lightGame.setDeckBuffer(card, type, position);
    }
}
