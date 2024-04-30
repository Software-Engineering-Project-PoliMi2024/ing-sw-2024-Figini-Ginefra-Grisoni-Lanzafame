package it.polimi.ingsw.lightModel.diffs.nuclearDiffs;

import it.polimi.ingsw.lightModel.diffs.LobbyListDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

import java.util.ArrayList;
import java.util.List;

public class FatManLobbyList extends LobbyListDiff {

    @Override
    public void apply(LightLobbyList lobbyList) {
        lobbyList.setLobbies(new ArrayList<>());
    }
}
