package it.polimi.ingsw.controller.socket.client;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.net.Socket;
import it.polimi.ingsw.controller.clientImplementation;

public class SocketClient extends clientImplementation{
    protected final Controller controller;
    protected final String ip;
    protected final int port;
    private SocketServerHandler socketServerHandler;

    /**@param ip the ip of the server to connect to
     * @param port the port of the server to connect to
     * @param nickname the nickname used by the user
     * @param view the view of user choice (TUI / GUI)
     * @param controller the communication of user choice (socket/RMI)*/
    public SocketClient(String ip, int port, String nickname, View view, Controller controller)
    {
        super(nickname, view);
        this.ip = ip;
        this.port = port;
        this.controller = controller;
    }
    /**
     * @return the reference to the Controller of this Client
     */
    public Controller getController() {
        return controller;
    }

    @Override
    public void run()
    {
        /* Open connection to the server and start a thread for handling
         * communication. */
        Socket server;

        try {
            server = new Socket(ip, port);

        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }

        socketServerHandler = new SocketServerHandler(server, this, nickname);
        Thread serverHandlerThread = new Thread(socketServerHandler, "server_" + server.getInetAddress().getHostAddress());
        serverHandlerThread.start();

        /* We are going to stop the application, so ask the server thread
         * to stop as well. Note that we are invoking the stop() method on
         * ServerHandler, not on Thread */
        while(!shallTerminate)
        {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        socketServerHandler.stop();
    }


    /**
     * The handler object responsible for communicating with the server.
     * @return The server handler.
     */
    public SocketServerHandler getServerHandler()
    {
        return socketServerHandler;
    }

    /**
     * Terminates the application as soon as possible.
     */
    public synchronized void terminate()
    {
        if (!shallTerminate) {
            /* Signal to the view handler loop that it should exit. */
            shallTerminate = true;
        }
    }

    /**
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }
}
