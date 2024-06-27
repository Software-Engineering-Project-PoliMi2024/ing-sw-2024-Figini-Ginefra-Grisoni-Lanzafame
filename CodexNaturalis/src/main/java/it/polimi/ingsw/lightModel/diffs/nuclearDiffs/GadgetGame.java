package it.polimi.ingsw.lightModel.diffs.nuclearDiffs;

import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

/**
 * This class reset the lightGame removing every diff that was applied to it
 */
public class GadgetGame extends GameDiff {

    /**
     * Apply the update to the LightGame
     * @param lightGame the LightGame to which the diff applies
     */
    @Override
    public void apply(LightGame lightGame) {
        lightGame.reset();
    }
}
