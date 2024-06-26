package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.NetworkMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;

public abstract class ServerMsg extends NetworkMsg {
    /**
     * The index of the message. This value is set by the clientHandler and allow for process each serverMsg in order
     */
    int index;

    /**
     * Set the index of the msg
     * @param index to be set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Get the index of the msg
     * @return the index of the msg
     */
    public int getIndex() {
        return index;
    }

    /**
     * The actual message. This method contains the logic of the message and is called by the serverHandler
     * @param serverHandler the serverHandler that will process the message
     * @throws Exception if an error occurs in the serverHandler
     */
    public abstract void processMsg(ServerHandler serverHandler) throws Exception;
}
