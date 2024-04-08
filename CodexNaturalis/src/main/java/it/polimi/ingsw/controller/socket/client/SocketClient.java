package it.polimi.ingsw.controller.socket.client;

import java.io.IOException;
import java.net.Socket;

public class SocketClient implements Runnable{
    private SocketServerHandler socketServerHandler;
    private boolean shallTerminate;

    private String ip;
    private int port;
    private String nickname;

    public SocketClient(String ip, int port, String nickname)
    {
        super();
        this.ip = ip;
        this.port = port;
        this.nickname = nickname;
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
}
