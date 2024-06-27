package it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

/**
 * This class contains the update to the lightModel for when players are removed from the game
 * (e.g. when a disconnected player is the last to place the startCard or choose his secretObjective)
 */
public class GameDiffRemovePlayers extends GamePartyDiff{
    /** the list of players to remove from the game */
    private final List<String> playersToRemoveNick;

    /**
     * Constructor: set the players to remove
     * @param playersToRemoveNick the list of players that need to be removed from the game
     */
    public GameDiffRemovePlayers(List<String> playersToRemoveNick){
        this.playersToRemoveNick = playersToRemoveNick;
    }

    /**
     * Remove each player in the list from the game
     * @param lightGame the lightGame to update
     */
    @Override
    public void apply(LightGame lightGame) {
        for(String playerToRemoveNick : playersToRemoveNick)
            lightGame.removePlayer(playerToRemoveNick);
    }
}
