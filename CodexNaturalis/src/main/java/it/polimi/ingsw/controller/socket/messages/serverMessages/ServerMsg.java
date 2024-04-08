package it.polimi.ingsw.controller.socket.messages.serverMessages;

import it.polimi.ingsw.controller.socket.client.SocketServerHandler;
import it.polimi.ingsw.controller.socket.messages.NetworkMessage;

import java.io.IOException;

public abstract class ServerMsg extends NetworkMessage {

    /**
     * Method invoked in the client to process the message.
     *
     */
    public abstract void processMessage(SocketServerHandler socketServerHandler) throws IOException;

}
