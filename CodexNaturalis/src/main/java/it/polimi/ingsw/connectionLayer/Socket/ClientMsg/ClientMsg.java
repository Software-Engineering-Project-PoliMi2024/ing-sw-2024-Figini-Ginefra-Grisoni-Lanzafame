package it.polimi.ingsw.connectionLayer.Socket.ClientMsg;

import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
import it.polimi.ingsw.connectionLayer.Socket.NetworkMsg;

/**
 * Abstract class that represents a message from the client to the server
 */
public abstract class ClientMsg extends NetworkMsg {
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
     * In actual message this method contains the logic of the message and is called by the clientHandler
     * @param clientHandler the serverHandler that will process the message
     * @throws Exception if an error occurs during the processing of the message
     */
    public abstract void processMsg(ClientHandler clientHandler) throws Exception;
}
