package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

/**
 * This diff is a diff that adds a secret objective option to the hand of a player.
 */
public class HandDiffAddOneSecretObjectiveOption extends HandDiff{
    /**
     * Creates a new HandDiffAddOneSecretObjectiveOption
     * @param secretObjectiveOption the secret objective option to add
     */
    public HandDiffAddOneSecretObjectiveOption(LightCard secretObjectiveOption){
        super(secretObjectiveOption);
    }

    /**
     * Applies the diff to the game
     * @param game the game to which apply the diff
     */
    @Override
    public void apply(LightGame game) {
        game.getHand().addSecretObjectiveOption(card);
    }
}
