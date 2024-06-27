package it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

/**
 * This class contains the update to the lightModel for changing the current player
 */
public class GameDiffCurrentPlayer extends GamePartyDiff {
    /** the nick for the new current player */
    private final String currentPlayer;
    /**
     * Constructor: set the new current player
     * @param currentPlayer the nick for the new current player
     */
    public GameDiffCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Set the new currentPlayer in the lightGame
     * @param lightGame the lightGame to update
     */
    @Override
    public void apply(LightGame lightGame) {
        lightGame.setCurrentPlayer(currentPlayer);
    }
}
