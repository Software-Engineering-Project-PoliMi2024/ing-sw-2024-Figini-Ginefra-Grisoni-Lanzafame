package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

import java.util.List;

public class LobbyListDiffEdit extends LobbyListDiff {
    private final List<LightLobby> addedLobby;
    private final List<LightLobby> removedLobby;
    public LobbyListDiffEdit(List<LightLobby> addedLobby, List<LightLobby> removedLobby) {
        this.addedLobby = addedLobby;
        this.removedLobby = removedLobby;
    }
    /**
     * @param lightLobbyList the LightLobbyList to which the diff applies
     */
    @Override
    public void apply(LightLobbyList lightLobbyList) {
        lightLobbyList.lobbiesDiff(addedLobby, removedLobby);
    }
}
