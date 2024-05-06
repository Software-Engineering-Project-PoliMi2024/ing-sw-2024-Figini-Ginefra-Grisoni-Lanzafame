package it.polimi.ingsw.controller2.ConnectionLayer;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.controller2.LogsOnClient;
import it.polimi.ingsw.controller2.ServerModelController;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.view.ViewState;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ConnectionServerRMI implements ConnectionLayerServer{
    private final MultiGame multiGame;
    private ConnectionClientRMI clientStub;
    ExecutorService serverExecutor = Executors.newSingleThreadExecutor();
    int secondsTimeOut = 5;

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

        //expose the controller to the client
        Future<ControllerInterface> connect = serverExecutor.submit(()->{
            ControllerInterface controller = new ServerModelController(multiGame, view);
            ControllerInterface controllerStub = (ControllerInterface) UnicastRemoteObject.exportObject(controller, 0);
            view.postConnectionInitialization(controllerStub);
            view.log(LogsOnClient.CONNECTION_SUCCESS.getMessage());
            view.transitionTo(ViewState.LOGIN_FORM);
            return controller;
        });

        try{
            ControllerInterface controller = connect.get(secondsTimeOut, TimeUnit.SECONDS);
        }catch (Exception e){
            System.out.println("Error In Client Connection");
        }

    }

    @Override
    public void pong() throws RemoteException {
        try {
            clientStub.ping();
        }catch (Exception e){
            throw new RemoteException("Connection lost");

        }
    }

    public void setConnectionClient(ConnectionLayerClient connectionClient){
        this.clientStub = (ConnectionClientRMI) connectionClient;
    }
}
