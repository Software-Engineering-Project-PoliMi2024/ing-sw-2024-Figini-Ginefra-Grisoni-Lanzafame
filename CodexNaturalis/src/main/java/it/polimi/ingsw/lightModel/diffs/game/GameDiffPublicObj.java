package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

/**
 * This class contains an update to the lightGame that sets the public objectives
 */
public class GameDiffPublicObj extends GameDiff {
    /**A list of public objectives*/
    private final LightCard[] publicObjective = new LightCard[2];

    /**
     * Constructor
     * Creates a new GameDiffPublicObj with no public objectives set yet
     */
    public GameDiffPublicObj(){}

    /**
     * Constructor that set the public objectives
     * @param cards a list of public objectives
     */
    public GameDiffPublicObj(LightCard[] cards){
        publicObjective[0] = cards[0];
        publicObjective[1] = cards[1];
    }

    /**
     * add the public objectives to the lightGame
     * @param differentiableType the lightGame to which the diff must be applied to
     */
    @Override
    public void apply(LightGame differentiableType) {
        differentiableType.addObjective(publicObjective[0]);
        differentiableType.addObjective(publicObjective[1]);
    }
}
