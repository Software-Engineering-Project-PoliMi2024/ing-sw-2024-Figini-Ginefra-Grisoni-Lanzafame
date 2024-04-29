package it.polimi.ingsw;

import it.polimi.ingsw.controller.socket.server.SocketServer;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.controller.RMI.ServerRMI;

/**
 * Starts the server, create an object of MultiGame, starts his SocketServer Thread
 */
public class Server {
    public static void main(String[] args) {
        System.out.println("SERVER STARTED! 🚀🚀🚀");

        MultiGame multiGame = new MultiGame();

        SocketServer socketServer = new SocketServer(multiGame);
        ServerRMI serverRMI = new ServerRMI(multiGame);
        Thread serverSocketThread = new Thread(socketServer, "Socket Server Listening Thread");
        Thread serverRMIThread = new Thread(serverRMI, "RMI Server Listening Thread");
        serverSocketThread.start();
        serverRMIThread.start();
    }
}
