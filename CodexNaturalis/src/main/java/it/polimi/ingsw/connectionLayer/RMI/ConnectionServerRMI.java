package it.polimi.ingsw.connectionLayer.RMI;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.connectionLayer.ConnectionLayerServer;
import it.polimi.ingsw.connectionLayer.HeartBeatInterface;
import it.polimi.ingsw.connectionLayer.RMI.VirtualRMI.VirtualViewRMI;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Interfaces.ControllerInterface;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualView;
import it.polimi.ingsw.controller.LogsOnClient;
import it.polimi.ingsw.controller.LobbyGameListsController;
import it.polimi.ingsw.utils.logger.LoggerLevel;
import it.polimi.ingsw.utils.logger.LoggerSources;
import it.polimi.ingsw.utils.logger.ServerLogger;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ConnectionServerRMI implements ConnectionLayerServer {
    /** The controller in the server that handle the creation, joining and leaving of lobby and games*/
    private final LobbyGameListsController lobbyGameListController;

    /**The executor service that will handle the connection with the client*/
    private final ExecutorService serverExecutor = Executors.newSingleThreadExecutor();
    /**The logger used to log the connection with the client if it fails*/
    private final ServerLogger logger = new ServerLogger(LoggerSources.SERVER, "RMI");

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
     * Exposes the controller to the client and starts the heartBeat mechanism between the server and the client.
     * If the connection is successful, the client is transitioned to the login form.
     * If Configs.secondsTimeOut seconds pass without a successful connection, the connection is considered failed.
     * @param view the view of the client which is trying to connect
     * @throws RemoteException if a communication-related exception occurs during the execution of this method.
     */
    public void connect(HeartBeatInterface pingPong, ViewInterface view, VirtualController controller) throws RemoteException {

        //expose the controller to the client
        Future<?> connect = serverExecutor.submit(() -> {
            try {
                VirtualView virtualView = new VirtualViewRMI(view);
                ControllerInterface controllerOnServer = new Controller(lobbyGameListController, virtualView);
                virtualView.setController(controllerOnServer);
                ControllerInterface controllerOnServerStub = (ControllerInterface) UnicastRemoteObject.exportObject(controllerOnServer, 0);
                HeartBeatInterface virtualViewStub = (HeartBeatInterface) UnicastRemoteObject.exportObject(virtualView, 0);
                virtualView.setHeartBeatStub(pingPong);
                pingPong.setHeartBeatStub(virtualViewStub);
                controller.setControllerStub(controllerOnServerStub);
                pingPong.heartBeat();
                virtualView.heartBeat();
                virtualView.log(LogsOnClient.CONNECTION_SUCCESS);
                virtualView.transitionTo(ViewState.LOGIN_FORM);
            }catch (Exception e){
                throw new RuntimeException("ConnectionServerRMI.connect: " + "\n  message: " + e.getMessage() + "\n  cause:\n" + e.getCause());
            }
        });

        try {
            connect.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (InterruptedException ignored){
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(LoggerLevel.SEVERE, "Error In Client Connection");
        }

    }
}
