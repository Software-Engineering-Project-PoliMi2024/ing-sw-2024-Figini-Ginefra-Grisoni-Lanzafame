package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

public class GameDiffInitializeGame extends GameDiff{
    private final List<String> players;
    public GameDiffInitializeGame(List<String> players) {
        this.players = players;
    }
    @Override
    public void apply(LightGame game) {
        game.gameStartInizialization(players);
    }
}
