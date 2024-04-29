package it.polimi.ingsw.controller2;

import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.view.ViewState;

import java.io.Serializable;
import java.rmi.Remote;

public class ConnectionServerRMI implements ConnectionLayerServer, Remote, Serializable{
    private final MultiGame multiGame;
    public ConnectionServerRMI(MultiGame multiGame){
        this.multiGame = multiGame;
    }
    public void connect(ViewInterface view){
        ServerModelController controller = new ServerModelController(multiGame, view);

        view.log(LogsFromServer.CONNECTION_SUCCESS.getMessage());
        view.transitionTo(ViewState.LOGIN_FORM);
    }
}
