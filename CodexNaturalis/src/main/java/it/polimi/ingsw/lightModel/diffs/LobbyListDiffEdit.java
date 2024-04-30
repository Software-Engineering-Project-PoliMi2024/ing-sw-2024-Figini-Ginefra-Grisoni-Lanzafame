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

    public List<LightLobby> getAddedLobby() {
        return addedLobby;
    }

    public List<LightLobby> getRemovedLobby() {
        return removedLobby;
    }

    /**
     * @param addedLobby the list of added lobbies
     * @param removedLobby the list of removed lobbies
     */
    public void updateLobbyListDiff(List<LightLobby> addedLobby, List<LightLobby> removedLobby) {
        this.addedLobby.addAll(addedLobby);
        this.removedLobby.addAll(removedLobby);
    }
    public void updateLobbyListDiff(LobbyListDiffEdit oldDiff) {
        this.addedLobby.addAll(oldDiff.getAddedLobby());
        this.removedLobby.addAll(oldDiff.getRemovedLobby());
    }

    @Override
    public String toString() {
        return "LobbyListDiff{" +
                "addedLobby=" + addedLobby.toString() +
                ", removedLobby=" + removedLobby.toString() +
                '}';
    }
}
