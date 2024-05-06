package it.polimi.ingsw.lightModel.diffs.nuclearDiffs;

import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

import java.util.ArrayList;

public class FatManLobbyList extends LobbyListDiff {

    @Override
    public void apply(LightLobbyList lobbyList) {
        lobbyList.setLobbies(new ArrayList<>());
    }
}
