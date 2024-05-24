package it.polimi.ingsw.lightModel;


import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiff;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiffEdit;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.tableReleted.Lobby;

import java.util.ArrayList;
import java.util.List;

public class DiffGenerator {

    public static LobbyListDiff addLobbyDiff(Lobby addedLobby){
        return new LobbyListDiffEdit(List.of(Lightifier.lightify(addedLobby)), new ArrayList<LightLobby>());
    }

    public static LobbyListDiff removeLobbyDiff(Lobby removedLobby){
        return new LobbyListDiffEdit(new ArrayList<LightLobby>(), List.of(Lightifier.lightify(removedLobby)));
    }

    public static LobbyListDiff lobbyListHistory(List<Lobby> lobbyHistory){
        return new LobbyListDiffEdit(new ArrayList<LightLobby>(Lightifier.lightify(lobbyHistory)), new ArrayList<LightLobby>());
    }
}
