package it.polimi.ingsw.lightModel.diffs.game.handDiffs;
import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

/**
 * This abstract class represents  an update for the lightGame that updates the lightHand of user (the owner of the view)
 */
public abstract class HandDiff extends GameDiff {
    /**The LightCard to which the update is about*/
    protected final LightCard card;
    /**
     * Constructor
     * Creates a diff to update the lightGame with the card of the user
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


