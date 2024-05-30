package it.polimi.ingsw.controller4;

import it.polimi.ingsw.controller4.Interfaces.ReceptionControllerInterface;

/**
 * This class is the controller that handles the reception of the clients. It manages the lobbies, the nicknames and the offline games
 */
public class ReceptionController implements ReceptionControllerInterface {

    @Override
    public boolean login(String nickname) {
        return false;
    }

    @Override
    public void createLobby(String nickname, String gameName, int maxPlayerCount) {

    }

    @Override
    public void joinLobby(String nickname, String lobbyName) {

    }

    @Override
    public void disconnect(String nickname) {

    }

    @Override
    public void leaveLobby(String nickname) {

    }
}
