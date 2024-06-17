package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

public class GameDiffWinner extends GameDiff{
    private final List<String> winnersList;
    public GameDiffWinner(List<String> winnersList){
        this.winnersList = winnersList;
    }
    @Override
    public void apply(LightGame lightGame) {
        lightGame.setWinners(winnersList);
    }
}
