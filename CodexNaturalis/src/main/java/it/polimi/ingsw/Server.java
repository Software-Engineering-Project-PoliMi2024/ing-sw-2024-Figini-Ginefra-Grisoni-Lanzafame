package it.polimi.ingsw;

import it.polimi.ingsw.controller.ModelController;
import it.polimi.ingsw.controller.socket.server.SocketServer;
import it.polimi.ingsw.model.MultiGame;

public class Server {
    public static void main(String[] args) {
        System.out.println("SERVER STARTED! 🚀🚀🚀");

        MultiGame multiGame = new MultiGame();

        SocketServer socketServer = new SocketServer(multiGame);

        Thread serverThread = new Thread(socketServer, "Socket Server Listening Thread");
        serverThread.start();

    }
}
