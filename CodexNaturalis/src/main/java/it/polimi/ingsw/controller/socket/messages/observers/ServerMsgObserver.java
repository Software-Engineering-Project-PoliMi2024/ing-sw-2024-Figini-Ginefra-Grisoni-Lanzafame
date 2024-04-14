package it.polimi.ingsw.controller.socket.messages.observers;

import it.polimi.ingsw.controller.socket.messages.serverMessages.ServerMsg;

import java.io.Serializable;

public interface ServerMsgObserver {
    public void update(ServerMsg serverMsg);
}
