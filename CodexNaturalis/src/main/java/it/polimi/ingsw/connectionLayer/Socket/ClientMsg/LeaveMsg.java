package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;

public class LeaveMsg extends ClientMsg {

    public LeaveMsg() {
    }

    @Override
    public void processMsg(ClientHandler clientHandler) throws Exception {
        clientHandler.getController().leave();
    }
}