package it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public class GameDiffFirstPlayer extends GamePartyDiff {
    private final String firstPlayer;

    public GameDiffFirstPlayer(String firstPlayer){
        this.firstPlayer = firstPlayer;
    }


    @Override
    public void apply(LightGame lightGame) {
        lightGame.setFirstPlayerName(firstPlayer);
    }
}
