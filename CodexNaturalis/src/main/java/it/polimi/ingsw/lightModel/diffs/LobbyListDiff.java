package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.LightLobby;
import it.polimi.ingsw.lightModel.LightLobbyList;

import java.util.List;

public record LobbyListDiff(List<LightLobby> removedGame,
                            List<LightLobby> addedGame) implements ModelDiffs<LightLobbyList> {
    /**
     * @param lightLobbyList the LightLobbyList to which the diff applies
     */
    @Override
    public void apply(LightLobbyList lightLobbyList) {
        lightLobbyList.lobbiesDiff(addedGame, removedGame);
    }
}
