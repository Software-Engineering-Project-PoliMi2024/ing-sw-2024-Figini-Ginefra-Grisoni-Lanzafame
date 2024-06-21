package it.polimi.ingsw.connectionLayer.RMI;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.connectionLayer.ConnectionLayerServer;
import it.polimi.ingsw.connectionLayer.PingPongInterface;
import it.polimi.ingsw.connectionLayer.RMI.VirtualRMI.VirtualViewRMI;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Interfaces.ControllerInterface;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualView;
import it.polimi.ingsw.controller.LogsOnClient;
import it.polimi.ingsw.controller.LobbyGameListsController;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ConnectionServerRMI implements ConnectionLayerServer {
    private final LobbyGameListsController lobbyGameListController;
    private final ExecutorService serverExecutor = Executors.newSingleThreadExecutor();
    int secondsTimeOut = Configs.secondsTimeOut;

    /**
     * The constructor of the class
     *
     * @param lobbyGameListController the wrapper for the Model present in the server
     */
    public ConnectionServerRMI(LobbyGameListsController lobbyGameListController) {
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
        Future<?> connect = serverExecutor.submit(() -> {
            try {
                VirtualView virtualView = new VirtualViewRMI(view);
                ControllerInterface controllerOnServer = new Controller(lobbyGameListController, virtualView);
                virtualView.setController(controllerOnServer);
                virtualView.setPingPongStub(pingPong);
                ControllerInterface controllerOnServerStub = (ControllerInterface) UnicastRemoteObject.exportObject(controllerOnServer, 0);
                PingPongInterface virtualViewStub = (PingPongInterface) UnicastRemoteObject.exportObject(virtualView, 0);
                pingPong.setPingPongStub(virtualViewStub);
                controller.setControllerStub(controllerOnServerStub);
                pingPong.pingPong();
                virtualView.pingPong();
                virtualView.log(LogsOnClient.CONNECTION_SUCCESS);
                virtualView.transitionTo(ViewState.LOGIN_FORM);
            }catch (Exception e){
                throw new RuntimeException("ConnectionServerRMI.connect: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });

        try {
            connect.get(secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error In Client Connection");
        }

    }
}
