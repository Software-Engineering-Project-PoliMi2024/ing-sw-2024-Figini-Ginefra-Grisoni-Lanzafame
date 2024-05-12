package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

public class DisconnectMsg extends ClientMsg {

    public DisconnectMsg() {
    }

    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        clientHandler.getController().disconnect();
    }
}