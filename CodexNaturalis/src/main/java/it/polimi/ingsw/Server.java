package it.polimi.ingsw;

import it.polimi.ingsw.connectionLayer.ConnectionLayerServer;
import it.polimi.ingsw.connectionLayer.RMI.ConnectionServerRMI;
import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualView;
import it.polimi.ingsw.connectionLayer.Socket.VirtualSocket.VirtualViewSocket;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.LogsOnClient;
import it.polimi.ingsw.controller.LobbyGameListsController;
import it.polimi.ingsw.utils.logger.LoggerSources;
import it.polimi.ingsw.utils.logger.ProjectLogger;
import it.polimi.ingsw.utils.OSRelated;
import it.polimi.ingsw.view.ViewState;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;

/**
 * Starts the server, create an object of MultiGame, starts his SocketServer Thread
 */
public class Server {
    public static void main(String[] args) {
        OSRelated.checkOrCreateDataFolderServer();
        ProjectLogger serverLogger = new ProjectLogger(LoggerSources.SERVER);

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80));
            String ip = socket.getLocalAddress().getHostAddress();
            serverLogger.log(Level.INFO, "IP: " + ip);
            System.setProperty("java.rmi.server.hostname", ip);
        } catch (IOException e) {
            serverLogger.log(Level.WARNING, "No internet connection, can't get IP address");
        }
        LobbyGameListsController lobbyGameListController = new LobbyGameListsController();

        Registry registry;
        try {
            registry = (LocateRegistry.createRegistry(Configs.rmiPort));
            ConnectionLayerServer connection = new ConnectionServerRMI(lobbyGameListController);
            ConnectionLayerServer stub = (ConnectionLayerServer) UnicastRemoteObject.exportObject(connection, 0);
            registry.rebind(Configs.connectionLabelRMI, stub);
            serverLogger.log(Level.INFO, "RMI Server started on port " + Configs.rmiPort + "🚔!");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    for (String boundName : registry.list()) {
                        registry.unbind(boundName);
                    }
                } catch (Exception e) {
                    serverLogger.log(Level.SEVERE, "Error while unbinding ");
                }

                serverLogger.log(Level.INFO, "Shutting down server");
            }));

        } catch (Exception e) {
            serverLogger.log(Level.SEVERE, "Server exception: can't open registry " +
                    "or error while binding the object");
            System.exit(1);
            //e.printStackTrace();
        }

        ServerSocket server;
        try {
            server = new ServerSocket(Configs.socketPort);
        } catch (IOException e) {
            serverLogger.log(Level.SEVERE, "Cannot open server socket");
            System.exit(1);
            return;
        }
        Thread serverSocketThread = new Thread(() -> {
            serverLogger.log(Level.INFO, "Socket Server started on port " + server.getLocalPort() + "🚔!");
            while (true) {
                try {
                    Socket client = server.accept();
                    ClientHandler clientHandler = new ClientHandler(client);
                    Thread clientHandlerThread = new Thread(clientHandler, "clientHandler of" + client.getInetAddress());
                    clientHandlerThread.start();
                    VirtualView virtualView = new VirtualViewSocket(clientHandler);
                    Controller controller = new Controller(lobbyGameListController, virtualView);
                    virtualView.setController(controller);
                    while(!clientHandler.isReady()) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    clientHandler.setOwner(virtualView);
                    clientHandler.setController(controller);
                    virtualView.log(LogsOnClient.SERVER_JOINED);
                    virtualView.transitionTo(ViewState.LOGIN_FORM);
                } catch (IOException e) {
                    serverLogger.log(Level.SEVERE, "Connection dropped");
                    e.printStackTrace();
                }
            }
        }, "Socket Server Listening Thread");
        serverSocketThread.start();

//        SocketServer socketServer = new SocketServer(multiGame);
//        ServerRMI serverRMI = new ServerRMI(multiGame);
//        Thread serverSocketThread = new Thread(socketServer, "Socket Server Listening Thread");
//        Thread serverRMIThread = new Thread(serverRMI, "RMI Server Listening Thread");
//        serverSocketThread.start();
//        serverRMIThread.start();
    }

}
