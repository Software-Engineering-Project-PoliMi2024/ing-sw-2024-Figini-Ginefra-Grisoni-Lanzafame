package it.polimi.ingsw.controller2.ConnectionLayer;

import it.polimi.ingsw.controller2.ConnectionLayer.VirtualRMI.VirtualViewRMI;
import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.controller2.LogsOnClient;
import it.polimi.ingsw.controller2.ServerModelController;
import it.polimi.ingsw.controller2.VirtualLayer.VirtualController;
import it.polimi.ingsw.controller2.VirtualLayer.VirtualView;
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

public class ConnectionServerRMI implements ConnectionLayerServer {
    private final MultiGame multiGame;
    private final ExecutorService serverExecutor = Executors.newSingleThreadExecutor();
    int secondsTimeOut = 5;

    /**
     * The constructor of the class
     *
     * @param multiGame the wrapper for the Model present in the server
     */
    public ConnectionServerRMI(MultiGame multiGame) {
        this.multiGame = multiGame;
    }

    /**
     * Establishes a connection with a client by initializing the necessary components on the server side.
     *
     * @param view the view of the client which is trying to connect
     * @throws RemoteException if a communication-related exception occurs during the execution of this method.
     */
    public void connect(PingPongInterface pingPong, ViewInterface view, VirtualController controller) throws RemoteException {
        //Create a ServerModelController for the new client

        //expose the controller to the client
        Future<Void> connect = serverExecutor.submit(() -> {
            VirtualView virtualView = new VirtualViewRMI(view);
            ControllerInterface controllerOnServer = new ServerModelController(multiGame, virtualView);
            virtualView.setController(controllerOnServer);
            virtualView.setPingPongStub(pingPong);
            ControllerInterface controllerStub = (ControllerInterface) UnicastRemoteObject.exportObject(controllerOnServer, 0);
            PingPongInterface virtualViewStub = (PingPongInterface) UnicastRemoteObject.exportObject(virtualView, 0);
            pingPong.setPingPongStub(virtualViewStub);
            controller.setControllerStub(controllerStub);
            controller.pingPong();
            virtualView.pingPong();
            virtualView.log(LogsOnClient.CONNECTION_SUCCESS.getMessage());
            virtualView.transitionTo(ViewState.LOGIN_FORM);
            return null;
        });

        try {
            connect.get(secondsTimeOut, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("Error In Client Connection");
        }

    }
}
