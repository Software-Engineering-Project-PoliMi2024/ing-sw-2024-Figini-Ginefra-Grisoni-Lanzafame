package it.polimi.ingsw.controller4.Interfaces;

import java.io.Serializable;
import java.rmi.Remote;

public interface ReceptionControllerInterface extends Serializable {
    void login(String nickname);
    void createLobby(String nickname, String gameName, int maxPlayerCount);
    void joinLobby(String nickname, String lobbyName);
    void disconnect(String nickname);
    void leaveLobby(String nickname);
}
