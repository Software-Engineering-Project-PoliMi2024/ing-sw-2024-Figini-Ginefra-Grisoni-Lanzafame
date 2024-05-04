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

    /**
     * The constructor of the class
     * @param multiGame the wrapper for the Model present in the server
     */
    public ConnectionServerRMI(MultiGame multiGame){
        this.multiGame = multiGame;
    }

    /**
     * Establishes a connection with a client by initializing the necessary components on the server side.
     *
     * @param view the view of the client which is trying to connect
     * @throws RemoteException if a communication-related exception occurs during the execution of this method.
     */
    public void connect(ViewInterface view) throws RemoteException {
        //Create a ServerModelContoller for the new client
        ControllerInterface controller = new ServerModelController(multiGame, view);
        //expose the controller to the client
        ControllerInterface controllerStub = (ControllerInterface) UnicastRemoteObject.exportObject(controller, 0);
        view.postConnectionInitialization(controllerStub);
        
        view.log(LogsFromServer.CONNECTION_SUCCESS.getMessage());
        view.transitionTo(ViewState.LOGIN_FORM);
    }
}
