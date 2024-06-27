package it.polimi.ingsw.lightModel.diffs.game.codexDiffs;

import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.Map;

/**
 * This class represent the final points gain by each player after the end of the game, and the
 * final scoring calculated with the common and secret objectives.
 */
public class CodexDiffSetFinalPoints extends GameDiff {
    /** The points of each player */
    private final Map<String, Integer> pointsPerPlayer;

    /**
     * Create a new CodexDiffSetFinalPoints
     * @param pointsPerPlayer the points of each player
     */
    public CodexDiffSetFinalPoints(Map<String, Integer> pointsPerPlayer){
        this.pointsPerPlayer = pointsPerPlayer;
    }

    /**
     * For each player set his final points
     * @param lightGame the lightGame to which the diff is applied
     */
    @Override
    public void apply(LightGame lightGame) {
        pointsPerPlayer.forEach((nick, points)->{
            lightGame.setPoint(points, nick);
        });
    }
}
