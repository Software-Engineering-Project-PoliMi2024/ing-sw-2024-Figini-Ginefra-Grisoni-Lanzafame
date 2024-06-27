package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

/**
 * This class contains an update to the lightGame that sets the winners of the game
 */
public class GameDiffWinner extends GameDiff{
    /**The list of winners of the game*/
    private final List<String> winnersList;

    /**
     * Constructor
     * @param winnersList the list of winners of the game
     */
    public GameDiffWinner(List<String> winnersList){
        this.winnersList = winnersList;
    }

    /**
     * Update the lightGame with the winners of the game
     * @param lightGame the lightGame to which the diff must be applied
     */
    @Override
    public void apply(LightGame lightGame) {
        lightGame.setWinners(winnersList);
    }
}
