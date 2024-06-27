package it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

/**
 * This class contains the update to the lightModel for a change in the player activity such as a disconnection or a join
 */
public class GameDiffPlayerActivity extends GamePartyDiff {
    /**The list of player who rejoined the game and need to be set as active*/
    private final List<String> playersToSetActive;
    /**The list of player who disconnected and need to be set as inactive*/
    private final List<String> playersToSetInactive;

    /**
     * Creates a diff to update the player activity, setting the (re)joining player as active and the disconnecting player as inactive
     * @param playersToSetActive the players to set active
     * @param playersToSetInactive the players to set inactive
     */
    public GameDiffPlayerActivity(List<String> playersToSetActive, List<String> playersToSetInactive) {
        this.playersToSetActive = playersToSetActive;
        this.playersToSetInactive = playersToSetInactive;
    }

    /**
     * For each player in each list, set the player as active or inactive
     * @param lightGame the lightGame to update
     */
    @Override
    public void apply(LightGame lightGame) {
        for (String player : playersToSetActive) {
            lightGame.setActivePlayer(player);
        }
        for (String player : playersToSetInactive) {
            lightGame.setInactivePlayer(player);
        }
    }
}
