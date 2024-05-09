package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
import it.polimi.ingsw.connectionLayer.Socket.NetworkMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;

public abstract class ClientMsg extends NetworkMsg {
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public abstract void processMsg(ClientHandler clientHandler) throws Exception;
}
