package it.polimi.ingsw.lightModel;

import it.polimi.ingsw.lightModel.LightLobby;
import it.polimi.ingsw.model.LobbyList;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;

import java.io.Serializable;

public class Lightifier implements Serializable {
    public static LightLobby lightify(Lobby lobby) {
        return new LightLobby(lobby.getLobbyPlayerList(), lobby.getLobbyName());
    }
    public static LightLobbyList lightify(LobbyList lobbies){
        return new LightLobbyList(lobbies.getLobbies().stream().map(Lightifier::lightify).toList());
    }
}
