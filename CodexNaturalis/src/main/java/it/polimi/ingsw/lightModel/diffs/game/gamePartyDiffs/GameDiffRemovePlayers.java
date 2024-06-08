package it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

public class GameDiffRemovePlayers extends GamePartyDiff{
    private final List<String> playersToRemoveNick;

    public GameDiffRemovePlayers(List<String> playersToRemoveNick){
        this.playersToRemoveNick = playersToRemoveNick;
    }

    @Override
    public void apply(LightGame lightGame) {
        for(String playerToRemoveNick : playersToRemoveNick)
            lightGame.removePlayer(playerToRemoveNick);
    }
}
