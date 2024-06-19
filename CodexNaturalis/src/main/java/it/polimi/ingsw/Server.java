package it.polimi.ingsw;

import it.polimi.ingsw.connectionLayer.ConnectionLayerServer;
import it.polimi.ingsw.connectionLayer.RMI.ConnectionServerRMI;
import it.polimi.ingsw.connectionLayer.Socket.ClientHandler;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualView;
import it.polimi.ingsw.connectionLayer.VirtualSocket.VirtualViewSocket;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.LogsOnClientStatic;
import it.polimi.ingsw.controller.LobbyGameListsController;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.ViewState;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Starts the server, create an object of MultiGame, starts his SocketServer Thread
 */
public class Server {
    public static void main(String[] args) {
        OSRelated.checkOrCreateDataFolderServer();
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80));
            String ip = socket.getLocalAddress().getHostAddress();
            Printer.println("IP: " + ip);
            System.setProperty("java.rmi.server.hostname", ip);
        } catch (IOException e) {
            System.out.println("No internet connection, can't get IP address");
        }
        LobbyGameListsController lobbyGameListController = new LobbyGameListsController();

        Registry registry;
        try {
            registry = (LocateRegistry.createRegistry(Configs.rmiPort));
            ConnectionLayerServer connection = new ConnectionServerRMI(lobbyGameListController);
            ConnectionLayerServer stub = (ConnectionLayerServer) UnicastRemoteObject.exportObject(connection, 0);
            registry.rebind(Configs.connectionLabelRMI, stub);
            System.out.println("RMI Server started on port " + Configs.rmiPort + "🚔!");
        } catch (Exception e) {
            System.err.println("Server exception: can't open registry " +
                    "or error while binding the object");
            e.printStackTrace();
        }

        ServerSocket server;
        try {
            server = new ServerSocket(Configs.socketPort);
        } catch (IOException e) {
            System.out.println("cannot open server socket");
            System.exit(1);
            return;
        }
        Thread serverSocketThread = new Thread(() -> {
            System.out.println("Socket Server started on port " + server.getLocalPort() + "🚔!");
            while (true) {
                try {
                    //todo this should be done in a connectionServerSocket but I don't know if it is the right thing (mental note, pinPong)
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
                    virtualView.log(LogsOnClientStatic.SERVER_JOINED);
                    virtualView.transitionTo(ViewState.LOGIN_FORM);
                } catch (IOException e) {
                    System.out.println("connection dropped");
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
