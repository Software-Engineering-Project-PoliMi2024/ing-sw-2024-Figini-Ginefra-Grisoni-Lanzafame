package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

public class DeckDiffBufferDraw extends DeckDiff{
    private final LightCard card;
    private final Integer position;
    private final DrawableCard type;
    /**
     * @param card the card drawn from the buffer
     * @param position the position in the buffer where the card is drawn
     */
    public DeckDiffBufferDraw(LightCard card, Integer position, DrawableCard type) {
        this.card = card;
        this.position = position;
        this.type = type;
    }
        @Override
    public void apply(LightGame lightGame) {
        lightGame.setDeckBuffer(card, type, position);
    }
}
