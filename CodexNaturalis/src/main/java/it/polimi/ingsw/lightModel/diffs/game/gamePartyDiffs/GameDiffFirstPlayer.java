package it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

/**
 * This class contains the update to the lightModel for changing the first player (e.g. after the old first player has disconnected)
 */
public class GameDiffFirstPlayer extends GamePartyDiff {
    /** the nick for the new first player */
    private final String firstPlayer;

    /**
     * Constructor: set the new first player
     * @param firstPlayer the nick for the new first player
     */
    public GameDiffFirstPlayer(String firstPlayer){
        this.firstPlayer = firstPlayer;
    }


    /**
     * Set the new firstPlayer in the lightGame
     * @param lightGame the lightGame to update
     */
    @Override
    public void apply(LightGame lightGame) {
        lightGame.setFirstPlayerName(firstPlayer);
    }
}
