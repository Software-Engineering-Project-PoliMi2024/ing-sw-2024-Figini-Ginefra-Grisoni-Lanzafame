package it.polimi.ingsw.controller.socket.server;

import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.controller.socket.messages.observers.ServerMsgObserver;
import it.polimi.ingsw.controller.socket.messages.serverMessages.ServerMsg;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientHandler implements Runnable, ServerMsgObserver {
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Game game;
    private User user;
    private MultiGame games;


    /**
     * Initializes a new handler using a specific socket connected to
     * a client.
     * @param client The socket connection to the client.
     */
    SocketClientHandler(Socket client, MultiGame games)
    {
        this.client = client;
        this.games = games;
    }

    /**
     * Send un update to a client
     * @param serverMsg the message that need be sent
     */
    public void update(ServerMsg serverMsg) {
        try {
            sendServerMessage(serverMsg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Connects to the client and runs the event loop
     * creating input and output connection to the client.
     */
    @Override
    public void run()
    {
        try {
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println("could not open connection to " + client.getInetAddress());
            return;
        }

        System.out.println("Connected to " + client.getInetAddress());

        try {
            handleClientConnection();
        } catch (IOException e) {
            System.out.println("client " + client.getInetAddress() + " connection dropped");
        }

        try {
            client.close();
        } catch (IOException e) { }
    }


    /**
     * An event loop that receives messages from the client and processes
     * them in the order they are received.
     * @throws IOException If a communication error occurs.
     */
    private void handleClientConnection() throws IOException
    {
        try {
            while (true) {
                /* read commands from the client, process them, and send replies */
                Object next = input.readObject();
                ActionMsg command = (ActionMsg) next;

                Thread thread = new Thread(() -> {
                    try {
                        command.processMessage(this);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                thread.start();
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            System.out.println("invalid stream from client");
        }
    }


    /**
     * The game instance associated with this clientHandler.
     * @return The game instance.
     */
    public Game getGame()
    {
        return game;
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    /**
     * @return the Multigame obj on this server
     */
    public MultiGame getGames(){
        return games;
    }

    /**
     * @return the user that is bound to this ClientHandler
     */
    public User getUser()
    {
        return user;
    }
    /**
     * @param user the user that is being bounded to this ClientHandler
     */
    public void setUser(User user)
    {
        this.user = user;
    }


    /**
     * Sends a message to the client.
     * @param serverMsg The message to be sent.
     * @throws IOException If a communication error occurs.
     */
    public void sendServerMessage(ServerMsg serverMsg) throws IOException
    {
        output.writeObject((Object)serverMsg);
    }
}

