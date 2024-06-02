package it.polimi.ingsw.connectionLayer;

import it.polimi.ingsw.connectionLayer.VirtualRMI.VirtualViewRMI;
import it.polimi.ingsw.controller4.Controller;
import it.polimi.ingsw.controller4.Interfaces.ControllerInterface;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualView;
import it.polimi.ingsw.controller4.LogsOnClientStatic;
import it.polimi.ingsw.controller4.LobbyGameListController;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ConnectionServerRMI implements ConnectionLayerServer {
    private final LobbyGameListController lobbyGameListController;
    private final ExecutorService serverExecutor = Executors.newSingleThreadExecutor();
    int secondsTimeOut = 5;

    /**
     * The constructor of the class
     *
     * @param lobbyGameListController the wrapper for the Model present in the server
     */
    public ConnectionServerRMI(LobbyGameListController lobbyGameListController) {
        this.lobbyGameListController = lobbyGameListController;
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
            ControllerInterface controllerOnServer = new Controller(lobbyGameListController, virtualView);
            virtualView.setController(controllerOnServer);
            virtualView.setPingPongStub(pingPong);
            ControllerInterface controllerStub = (ControllerInterface) UnicastRemoteObject.exportObject(controllerOnServer, 0);
            PingPongInterface virtualViewStub = (PingPongInterface) UnicastRemoteObject.exportObject(virtualView, 0);
            pingPong.setPingPongStub(virtualViewStub);
            controller.setControllerStub(controllerStub);
            controller.pingPong();
            virtualView.pingPong();
            virtualView.log(LogsOnClientStatic.CONNECTION_SUCCESS);
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
