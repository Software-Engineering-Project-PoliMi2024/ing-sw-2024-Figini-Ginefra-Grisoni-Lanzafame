package it.polimi.ingsw.controller.socket.server;

import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.tableReleted.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SocketServer implements Runnable{
    /**
     * The socket port where the server listens to client connections.
     * @implNote In a real project, this must not be a constant!
     */
    private final MultiGame games;

    public SocketServer(MultiGame games)
    {
        this.games = games;
    }

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

        while (true) {
            try {
                /* accepts connections; for every connection we accept,
                 * create a new Thread executing a ClientHandler */
                Socket client = socket.accept();
                SocketClientHandler socketClientHandler = new SocketClientHandler(client, games);
                Thread thread = new Thread(socketClientHandler, "sokcetServer_" + client.getInetAddress());
                thread.start();
            } catch (IOException e) {
                System.out.println("connection dropped");
            }
        }
    }
}