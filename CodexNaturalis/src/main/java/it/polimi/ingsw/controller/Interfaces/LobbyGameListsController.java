package it.polimi.ingsw.controller.Interfaces;

import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.rmi.Remote;

public interface LobbyGameListsController extends GameList, MalevolentPlayerManager, Serializable, Remote {
    boolean login(String nickname, ViewInterface view, GameControllerReceiver controllerReceiver);
    void createLobby(String nickname, String gameName, int maxPlayerCount, GameControllerReceiver gameReceiver);
    void joinLobby(String nickname, String lobbyName, GameControllerReceiver gameReceiver);
    void disconnect(String nickname);
    void leaveLobby(String nickname);
}
