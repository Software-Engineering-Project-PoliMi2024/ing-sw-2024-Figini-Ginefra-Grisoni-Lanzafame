package it.polimi.ingsw.controller.socket.client;

import it.polimi.ingsw.controller.socket.messages.actionMessages.ActionMsg;
import it.polimi.ingsw.controller.socket.messages.actionMessages.GetActiveGameListActionMsg;
import it.polimi.ingsw.controller.socket.messages.serverMessages.ServerMsg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class SocketServerHandler implements Runnable{
    private Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private SocketClient owner;
    private AtomicBoolean shouldStop = new AtomicBoolean(false);

    private String nickname;

    private boolean ready = false;


    /**
     * Initializes a new handler using a specific socket connected to
     * a server.
     * @param server The socket connection to the server.
     */
    public SocketServerHandler(Socket server, SocketClient owner, String nickname)
    {
        this.server = server;
        this.owner = owner;
        this.nickname = nickname;
    }


    /**
     * Connects to the server and runs the event loop.
     */
    @Override
    public void run()
    {
        try {
            output = new ObjectOutputStream(server.getOutputStream());
            input = new ObjectInputStream(server.getInputStream());
            ready = true;
        } catch (IOException e) {
            System.out.println("could not open connection to " + server.getInetAddress());
            owner.terminate();
            return;
        }

        try {
            handleClientConnection();
        } catch (IOException e) {
            System.out.println("server " + server.getInetAddress() + " connection dropped");
        }

        try {
            server.close();
        } catch (IOException e) { }
        owner.terminate();
    }

    public String getNickname() {
        return nickname;
    }

    /**
     * An event loop that receives messages from the server and processes
     * them in the order they are received.
     * @throws IOException If a communication error occurs.
     */
    private void handleClientConnection() throws IOException
    {
        try {
            boolean stop = false;

            while (!stop) {

                try {
                    Object next = input.readObject();
                    ServerMsg command = (ServerMsg)next;

                    new Thread(() -> {
                        try {
                            command.processMessage(this);
                        } catch (IOException e) {
                            System.out.println("Communication error");
                            owner.terminate();
                        }
                    }).start();

                } catch (IOException e) {
                    /* Check if we were interrupted because another thread has asked us to stop */
                    if (shouldStop.get()) {
                        /* Yes, exit the loop gracefully */
                        stop = true;
                    } else {
                        /* No, rethrow the exception */
                        throw e;
                    }
                }
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            System.out.println("invalid stream from server");
        }
    }


    /**
     * The game instance associated with this client.
     * @return The game instance.
     */
    public SocketClient getClient()
    {
        return owner;
    }

    public boolean isReady() {
        return ready;
    }


    /**
     * Sends a message to the server.
     * @param commandMsg The message to be sent.
     */
    public void sendActionMessage(ActionMsg commandMsg)
    {
        try {
            output.writeObject(commandMsg);
        } catch (IOException e) {
            System.out.println("Communication error");
            owner.terminate();
        }
    }


    /**
     * Requires the run() method to stop as soon as possible.
     */
    public void stop()
    {
        shouldStop.set(true);
        try {
            server.shutdownInput();
        } catch (IOException e) { }
    }
}
