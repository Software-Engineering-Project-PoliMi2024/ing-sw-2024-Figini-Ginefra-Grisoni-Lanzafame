package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.LightGame;

import java.util.List;

public class GameDiffSetPlayerActivity extends GameDiff{
    private final List<String> playersToSetActive;
    private final List<String> playersToSetInactive;
    public GameDiffSetPlayerActivity(List<String> playersToSetActive, List<String> playersToSetInactive) {
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
