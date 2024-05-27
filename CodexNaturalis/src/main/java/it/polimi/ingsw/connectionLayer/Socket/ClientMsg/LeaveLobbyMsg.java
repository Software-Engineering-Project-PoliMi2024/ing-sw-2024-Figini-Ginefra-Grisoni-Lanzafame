package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

public class LeaveLobbyMsg extends ClientMsg{
    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception{
        clientHandler.getController().leaveLobby();
    }
}