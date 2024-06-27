package it.polimi.ingsw.connectionLayer.Socket.ServerMsg;

import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.view.ViewState;

/**
 * Transition to message class
 */
public class TransitionToMsg extends ServerMsg{
    /** State to which the view have to transition */
    private final ViewState state;

    /**
     * Class constructor
     * @param state State to which the view have to transition
     */
    public TransitionToMsg(ViewState state) {
        this.state = state;
    }

    /**
     * Call the transitionTo method of the view
     * @param serverHandler Server handler where to process the message
     * @throws Exception If an error occurs during the processing of the message
     */
    @Override
    public void processMsg(ServerHandler serverHandler) throws Exception{
        serverHandler.getView().transitionTo(state);
    }
}

