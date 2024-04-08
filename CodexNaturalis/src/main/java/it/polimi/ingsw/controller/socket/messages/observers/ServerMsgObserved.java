package it.polimi.ingsw.controller.socket.messages.observers;

import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.controller.socket.messages.serverMessages.ServerMsg;

public interface ServerMsgObserved {
    public void attach(ServerMsgObserver observer);
    public void detach(ServerMsgObserver observer);
    public void notifyObservers(ServerMsg serverMsg);
}
