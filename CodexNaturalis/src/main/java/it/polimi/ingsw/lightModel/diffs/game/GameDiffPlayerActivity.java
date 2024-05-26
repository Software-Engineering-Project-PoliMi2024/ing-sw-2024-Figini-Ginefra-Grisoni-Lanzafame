package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

public class GameDiffPlayerActivity extends GamePartyDiff {
    private final List<String> playersToSetActive;
    private final List<String> playersToSetInactive;

    /**
     * Creates a diff to update the player activity,
     * i.e. which players are active and which are not
     * @param playersToSetActive the players to set active
     * @param playersToSetInactive the players to set inactive
     */
    public GameDiffPlayerActivity(List<String> playersToSetActive, List<String> playersToSetInactive) {
        this.playersToSetActive = playersToSetActive;
        this.playersToSetInactive = playersToSetInactive;
    }
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
