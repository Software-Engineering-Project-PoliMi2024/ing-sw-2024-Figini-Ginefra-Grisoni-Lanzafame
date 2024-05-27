package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public class HandDiffUpdatePlayability extends HandDiff{
    private final Boolean playability;
    /**
     * @param card the LightCard to which the diff applies
     */
    public HandDiffUpdatePlayability(LightCard card, Boolean playability) {
        super(card);
        this.playability = playability;
    }

    @Override
    public void apply(LightGame game) {
        game.updateHandPlayability(card, playability);
    }
}
