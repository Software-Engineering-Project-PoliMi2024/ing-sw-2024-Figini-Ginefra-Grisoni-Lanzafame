package it.polimi.ingsw.controller.socket.server;

import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.controller.socket.messages.serverMessages.ServerMsg;
import it.polimi.ingsw.controller.socket.messages.serverMessages.answerMessages.AnswerMsg;
import it.polimi.ingsw.controller.socket.messages.serverMessages.notificationMessages.GamePartyNotificationMsg;
import it.polimi.ingsw.controller.socket.messages.serverMessages.notificationMessages.JoinGameNotificationMsg;
import it.polimi.ingsw.designPatterns.Observer.Observer;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.function.Function;

public class ClientHandler implements Runnable, Observer {
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Game game;

    private User user;

    private List<Game> games;


    /**
     * Initializes a new handler using a specific socket connected to
     * a client.
     * @param client The socket connection to the client.
     */
    ClientHandler(Socket client, List<Game> games)
    {
        this.client = client;
        this.games = games;
    }


    public void update(){
        try {
            sendServerMessage(new GamePartyNotificationMsg(this.game.getGameParty().getUsersList().stream().map(User::getNickname).toArray(String[]::new)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Connects to the client and runs the event loop.
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
                ActionMsg command = (ActionMsg)next;
                command.processMessage(this);
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            System.out.println("invalid stream from client");
        }
    }


    /**
     * The game instance associated with this client.
     * @return The game instance.
     */
    public Game getGame()
    {
        return game;
    }

    public void addGame(Game game)
    {
        games.add(game);
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public List<Game> getGames()
    {
        return games;
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

