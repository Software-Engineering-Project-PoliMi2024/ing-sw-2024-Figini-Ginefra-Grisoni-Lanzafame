package it.polimi.ingsw.lightModel.diffs.game.handDiffs;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

/**
 * This diff is an update that adds a secret objective option to the hand of a player.
 */
public class HandDiffAddOneSecretObjectiveOption extends HandDiff{
    /**
     * Constructor
     * Creates a new HandDiffAddOneSecretObjectiveOption
     * @param secretObjectiveOption the secret objective option to add
     */
    public HandDiffAddOneSecretObjectiveOption(LightCard secretObjectiveOption){
        super(secretObjectiveOption);
    }

    /**
     * Add the secret objective option to the hand of the user
     * @param game the game to which apply the diff
     */
    @Override
    public void apply(LightGame game) {
        game.getHand().addSecretObjectiveOption(card);
    }
}
