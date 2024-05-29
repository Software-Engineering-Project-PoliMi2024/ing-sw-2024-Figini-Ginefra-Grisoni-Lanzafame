package it.polimi.ingsw.lightModel.diffs.game.handDiffs;
import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public abstract class HandDiff extends GameDiff {
    protected final LightCard card;
    /**
     * @param card the LightCard to which the diff applies
     */
    protected HandDiff(LightCard card) {
        this.card = card;
    }
    /**
     * @param game the game to which get the LightHand to which the diff applies
     */
    abstract public void apply(LightGame game);
}


