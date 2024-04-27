package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.LightGame;

public class GameDiffGameRound extends GameDiff{
    private final String currentPlayer;
    public GameDiffGameRound(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    @Override
    public void apply(LightGame lightGame) {
        lightGame.setCurrentPlayer(currentPlayer);
    }
}
