package it.polimi.ingsw.controller.socket.client;

import it.polimi.ingsw.controller.socket.server.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{
    private ServerHandler serverHandler;
    private boolean shallTerminate;


    public static void main(String[] args)
    {
        /* Instantiate a new Client. The main thread will become the
         * thread where user interaction is handled. */
        Client client = new Client();
        client.run();
    }


    @Override
    public void run()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("IP address of server?");
        String ip = scanner.nextLine();

        /* Open connection to the server and start a thread for handling
         * communication. */
        Socket server;
        try {
            server = new Socket(ip, Server.SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("server unreachable");
            return;
        }
        serverHandler = new ServerHandler(server, this);
        Thread serverHandlerThread = new Thread(serverHandler, "server_" + server.getInetAddress().getHostAddress());
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
        serverHandler.stop();
    }


    /**
     * The handler object responsible for communicating with the server.
     * @return The server handler.
     */
    public ServerHandler getServerHandler()
    {
        return serverHandler;
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
