package it.polimi.ingsw.controller.socket.server;

import it.polimi.ingsw.model.tableReleted.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    /**
     * The socket port where the server listens to client connections.
     * @implNote In a real project, this must not be a constant!
     */
    public final static int SOCKET_PORT = 7777;
    private static final List<Game> games = Collections.synchronizedList(new ArrayList<>());


    public static void main(String[] args)
    {
        ServerSocket socket;

        try {
            socket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("cannot open server socket");
            System.exit(1);
            return;
        }

        System.out.println("Server started");
        System.out.println("Accepting connections on port " + SOCKET_PORT + "...");

        while (true) {
            try {
                /* accepts connections; for every connection we accept,
                 * create a new Thread executing a ClientHandler */
                Socket client = socket.accept();
                ClientHandler clientHandler = new ClientHandler(client, Server.games);
                Thread thread = new Thread(clientHandler, "server_" + client.getInetAddress());
                thread.start();
            } catch (IOException e) {
                System.out.println("connection dropped");
            }
        }
    }
}
