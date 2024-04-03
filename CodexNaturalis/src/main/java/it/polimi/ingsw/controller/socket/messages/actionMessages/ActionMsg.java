package it.polimi.ingsw.controller.socket.messages.actionMessages;

import it.polimi.ingsw.controller.socket.messages.NetworkMessage;
import it.polimi.ingsw.controller.socket.server.ClientHandler;

import java.io.IOException;

public abstract class ActionMsg extends NetworkMessage {
    public abstract void processMessage(ClientHandler clientHandler) throws IOException;
}
