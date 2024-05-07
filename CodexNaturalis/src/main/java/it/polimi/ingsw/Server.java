package it.polimi.ingsw;

import it.polimi.ingsw.controller.socket.server.SocketServer;
import it.polimi.ingsw.controller2.ConnectionLayer.ConnectionLayerServer;
import it.polimi.ingsw.controller2.ConnectionLayer.ConnectionServerRMI;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.controller.RMI.ServerRMI;
import it.polimi.ingsw.view.TUI.Printing.Printer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Starts the server, create an object of MultiGame, starts his SocketServer Thread
 */
public class Server {

    public static void main(String[] args) {
        System.out.println("SERVER STARTED! 🚀🚀🚀");

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80));
            Printer.println("IP: " + socket.getLocalAddress().getHostAddress());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MultiGame multiGame = new MultiGame();
        Registry registry;
        try {
            registry = (LocateRegistry.createRegistry(Configs.port));
            ConnectionLayerServer connection = new ConnectionServerRMI(multiGame);
            ConnectionLayerServer stub = (ConnectionLayerServer) UnicastRemoteObject.exportObject(connection, 0);
            registry.rebind("connect", stub);
            System.out.println("RMI Server started on port " + Configs.port + "🚔!");
        } catch (Exception e) {
            System.err.println("Server exception: can't open registry " +
                    "or error while binding the object");
            e.printStackTrace();
        }


        SocketServer socketServer = new SocketServer(multiGame);
        ServerRMI serverRMI = new ServerRMI(multiGame);
        Thread serverSocketThread = new Thread(socketServer, "Socket Server Listening Thread");
        Thread serverRMIThread = new Thread(serverRMI, "RMI Server Listening Thread");
        serverSocketThread.start();
        serverRMIThread.start();
    }
}
