package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.NetworkMessage;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;

import java.io.IOException;

public abstract class ActionMsg extends NetworkMessage {
    /**
     * An abstract method for all ActionMsg. Each ActionMsg must send an AnswerMsg to the client
     * who performed an Action
     * @param socketClientHandler the ClientHandler who received the ActionMsg from the client
     * @throws IOException If an error occurs during the sending of the message, such as a network failure.
     */
    public abstract void processMessage(SocketClientHandler socketClientHandler) throws IOException;
}
