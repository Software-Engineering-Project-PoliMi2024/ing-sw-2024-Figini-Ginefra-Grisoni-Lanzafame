package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public class GameDiffRound extends GamePartyDiff {
    private final String currentPlayer;
    public GameDiffRound(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    @Override
    public void apply(LightGame lightGame) {
        lightGame.setCurrentPlayer(currentPlayer);
    }
}
