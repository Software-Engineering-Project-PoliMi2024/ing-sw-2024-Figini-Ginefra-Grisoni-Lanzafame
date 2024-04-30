package it.polimi.ingsw.controller2.ConnectionLayer;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.controller2.LogsFromServer;
import it.polimi.ingsw.controller2.ServerModelController;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.view.ViewState;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ConnectionServerRMI implements ConnectionLayerServer, Serializable{
    private final MultiGame multiGame;
    public ConnectionServerRMI(MultiGame multiGame){
        this.multiGame = multiGame;
    }
    public void connect(ViewInterface view) throws RemoteException {
        ControllerInterface controller = new ServerModelController(multiGame, view);
        ControllerInterface controllerStub = (ControllerInterface) UnicastRemoteObject.exportObject(controller, 0);
        view.postConnectionInitialization(controllerStub);
        view.log(LogsFromServer.CONNECTION_SUCCESS.getMessage());
        view.transitionTo(ViewState.LOGIN_FORM);
    }
}
