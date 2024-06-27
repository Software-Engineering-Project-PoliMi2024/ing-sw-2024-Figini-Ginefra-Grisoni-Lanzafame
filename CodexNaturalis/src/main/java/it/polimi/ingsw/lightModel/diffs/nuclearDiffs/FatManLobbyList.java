package it.polimi.ingsw.lightModel.diffs.nuclearDiffs;

import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

import java.util.ArrayList;

/**
 * This class erase all the lobbies in the LightLobbyList
 */
public class FatManLobbyList extends LobbyListDiff {
    /**
     * Apply the update to the LightLobbyList
     * @param lobbyList the LightLobbyList to which the diff applies
     */
    @Override
    public void apply(LightLobbyList lobbyList) {
        lobbyList.setLobbies(new ArrayList<>());
    }
}
