package it.polimi.ingsw.model.lightModel.diffs;

import it.polimi.ingsw.model.lightModel.LightLobby;
import it.polimi.ingsw.model.lightModel.LightLobbyList;

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
