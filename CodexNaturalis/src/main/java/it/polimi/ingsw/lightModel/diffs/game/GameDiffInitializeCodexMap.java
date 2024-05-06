package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

public class GameDiffInitializeCodexMap extends GameDiff{
    private final List<String> players;
    public GameDiffInitializeCodexMap(List<String> players) {
        this.players = players;
    }
    @Override
    public void apply(LightGame game) {
        game.gameInitializeCodexMap(players);
    }
}
