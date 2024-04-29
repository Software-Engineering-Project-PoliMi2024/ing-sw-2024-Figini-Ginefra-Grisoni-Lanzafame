package it.polimi.ingsw;

import it.polimi.ingsw.controller.socket.server.SocketServer;
import it.polimi.ingsw.controller2.ConnectionLayer.ConnectionServerRMI;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.controller.RMI.ServerRMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Starts the server, create an object of MultiGame, starts his SocketServer Thread
 */
public class Server {

    public static void main(String[] args) {
        System.out.println("SERVER STARTED! 🚀🚀🚀");

        MultiGame multiGame = new MultiGame();
        Registry registry;
        int port = 1234;
        try {
            registry = (LocateRegistry.createRegistry(port));
            registry.rebind("connect", new ConnectionServerRMI(multiGame));
            System.out.println("RMI Server started on port " + port + "🚔!");
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
