package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public class GameDiffCurrentPlayer extends GamePartyDiff {
    private final String currentPlayer;
    public GameDiffCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    @Override
    public void apply(LightGame lightGame) {
        lightGame.setCurrentPlayer(currentPlayer);
    }
}
