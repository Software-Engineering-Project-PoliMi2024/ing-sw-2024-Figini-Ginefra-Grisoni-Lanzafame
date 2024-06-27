package it.polimi.ingsw.lightModel.diffs.game.handDiffs;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

/**
 * This class represents an update for the lightGame that adds the chosen secret objective to the hand of the user
 * This card doesn't increase the number of cards in the hand
 */
public class HandDiffSetObj extends HandDiff{
    /**
     * Constructor
     * @param secretObjective the secret objective to add
     */
    public HandDiffSetObj(LightCard secretObjective) {
        super(secretObjective);
    }

    /**
     * Add the secret objective to the hand of the user
     * @param game the game to which apply the diff
     */
    @Override
    public void apply(LightGame game) {
        game.setSecretObjective(super.card);
    }
}
