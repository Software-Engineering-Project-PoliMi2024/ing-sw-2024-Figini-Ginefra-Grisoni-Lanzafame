package it.polimi.ingsw.lightModel.diffs.game.codexDiffs;

import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.ArrayList;
import java.util.List;
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
        List<Map.Entry<String, Integer>> pointsPerPlayerList = new ArrayList<>(pointsPerPlayer.entrySet());
        pointsPerPlayerList.sort(Map.Entry.comparingByValue());

        List<String> ranking = new ArrayList<>();
        for(Map.Entry<String, Integer> pointsOfPlayer : pointsPerPlayerList){
            ranking.add(pointsOfPlayer.getKey());
        }
        lightGame.setRanking(ranking);
    }
}
