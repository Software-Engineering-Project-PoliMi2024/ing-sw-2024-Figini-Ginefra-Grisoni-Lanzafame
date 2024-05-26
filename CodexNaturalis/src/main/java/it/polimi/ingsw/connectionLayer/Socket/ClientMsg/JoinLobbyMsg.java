package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

public class JoinLobbyMsg extends ClientMsg{
    private String lobbyName;

    public JoinLobbyMsg(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception{
        clientHandler.getController().joinLobby(lobbyName);
    }
}