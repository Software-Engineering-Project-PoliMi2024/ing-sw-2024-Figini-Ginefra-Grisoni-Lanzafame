package it.polimi.ingsw.lightModel.diffs.lobby_lobbyList;

import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

import java.util.List;

/**
 * This class represent an update to the LightLobbyList of the client, it contains the list of the lobbies that have been added and the list of the lobbies that have been removed
 */
public class LobbyListDiffEdit extends LobbyListDiff {
    /**List of the lobbies that have been added*/
    private final List<LightLobby> addedLobby;
    /**List of the lobbies that have been removed*/
    private final List<LightLobby> removedLobby;

    /**
     * Constructor of the class
     * @param addedLobby the list of the lobbies that have been added
     * @param removedLobby the list of the lobbies that have been removed
     */
    public LobbyListDiffEdit(List<LightLobby> addedLobby, List<LightLobby> removedLobby) {
        this.addedLobby = addedLobby;
        this.removedLobby = removedLobby;
    }

    /**
     * Apply the update to the LightLobbyList
     * @param lightLobbyList the LightLobbyList to which the diff applies
     */
    @Override
    public void apply(LightLobbyList lightLobbyList) {
        lightLobbyList.lobbiesDiff(addedLobby, removedLobby);
    }
}
