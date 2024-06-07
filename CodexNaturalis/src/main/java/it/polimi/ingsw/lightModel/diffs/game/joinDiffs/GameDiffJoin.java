package it.polimi.ingsw.lightModel.diffs.game.joinDiffs;

import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.ArrayList;
import java.util.List;

public class GameDiffJoin extends GameDiff {
    private final GameDiffInitialization initialization;
    private final List<GameDiff> gameDiffs = new ArrayList<>();

    public GameDiffJoin(GameDiffInitialization initialization){
        this.initialization = initialization;
    }

    public void put(GameDiff gameDiff){
        gameDiffs.add(gameDiff);
    }

    public void put(List<GameDiff> gameDiffs){
        this.gameDiffs.addAll(gameDiffs);
    }

    @Override
    public void apply(LightGame lightGame) {
        initialization.apply(lightGame);
        for (GameDiff gameDiff : gameDiffs) {
            gameDiff.apply(lightGame);
        }
    }
}
