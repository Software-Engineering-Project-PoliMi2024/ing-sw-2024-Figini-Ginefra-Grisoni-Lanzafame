package it.polimi.ingsw.controller.socket.server;

import it.polimi.ingsw.model.MultiGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import it.polimi.ingsw.controller.ServerImplementation;


public class SocketServer extends ServerImplementation {
    /**
     * The socket port where the server listens to client connections.
     * @implNote In a real project, this must not be a constant!
     */
    public SocketServer (MultiGame games) {
        super(games);
    }

    /**
     * Main method of SocketServer. One instance of this method is always running for each server that we have.
     * Accepts connections and for every connection we accept,create a new Thread
     * executing a ClientHandler handling that specific client*/
    public void run()
    {
        ServerSocket socket;

        try {
            socket = new ServerSocket(4444);

        } catch (IOException e) {
            System.out.println("cannot open server socket");
            System.exit(1);
            return;
        }

        System.out.println("Socket Server started on port " + socket.getLocalPort() + "🚔!");

        //For every new user that connects to the server, it launches a new SocketClient Handler
        while (true) {
            try {
                Socket client = socket.accept();
                SocketClientHandler socketClientHandler = new SocketClientHandler(client, games);
                Thread thread = new Thread(socketClientHandler, "socketServer_" + client.getInetAddress());
                thread.start();
            } catch (IOException e) {
                System.out.println("connection dropped");
            }
        }
    }
}
