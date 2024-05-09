package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
import it.polimi.ingsw.connectionLayer.Socket.NetworkMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;

public abstract class ServerMsg extends NetworkMsg {
    int index;
    public void setIndex(int index) {
        this.index = index;
    }
    public int getIndex() {
        return index;
    }
    public abstract void processMsg(ServerHandler clientHandler) throws Exception;
}
