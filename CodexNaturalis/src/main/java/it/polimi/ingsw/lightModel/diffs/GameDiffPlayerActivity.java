package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

public class GameDiffPlayerActivity extends GamePartyDiff {
    private final List<String> playersToSetActive;
    private final List<String> playersToSetInactive;
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
