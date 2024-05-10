package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.view.ViewState;

public class TransitionToMsg extends ServerMsg{
    private final ViewState state;

    public TransitionToMsg(ViewState state) {
        this.state = state;
    }

    public ViewState getState() {
        return state;
    }

    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception{
        serverHandler.getView().transitionTo(state);
    }
}

