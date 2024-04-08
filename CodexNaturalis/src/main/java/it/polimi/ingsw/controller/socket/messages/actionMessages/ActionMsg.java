package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.NetworkMessage;
import it.polimi.ingsw.controller.socket.server.SocketClientHandler;

import java.io.IOException;

public abstract class ActionMsg extends NetworkMessage {
    public abstract void processMessage(SocketClientHandler socketClientHandler) throws IOException;
}
