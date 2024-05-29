package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.Map;

public class CodexDiffSetFinalPoints extends GameDiff {
    private final Map<String, Integer> pointsPerPlayer;

    public CodexDiffSetFinalPoints(Map<String, Integer> pointsPerPlayer){
        this.pointsPerPlayer = pointsPerPlayer;
    }
    @Override
    public void apply(LightGame lightGame) {
        pointsPerPlayer.forEach((nick, points)->{
            lightGame.setPoint(points, nick);
        });
    }
}
