package it.polimi.ingsw.connectionLayer.Socket;

import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.ClientMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.ServerMsg;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.view.ViewInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler implements Runnable{
    private Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private VirtualController owner;
    private ViewInterface view;
    private boolean ready = false;

    public ServerHandler(Socket server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(server.getOutputStream());
            input = new ObjectInputStream(server.getInputStream());
        } catch (Exception e) {
            System.out.println("could not open connection to " + server.getInetAddress());
            return;
        }
        System.out.println("Connected to " + server.getInetAddress());
        Thread listeningThread = new Thread(this::HandleMsg, "Listening Thread");
        listeningThread.start();
        ready= true;
    }

    public void sendServerMessage(ClientMsg clientMsg) {
        try {
            output.writeObject(clientMsg);
        } catch (Exception e) {
            System.out.println("could not send message to " + server.getInetAddress());
            e.printStackTrace();
        }
    }

    public void HandleMsg() {
        System.out.println("Listening for messages of " + server.getInetAddress());
        while (true) {
            ServerMsg serverMsg;
            try {
                serverMsg = (ServerMsg) input.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println("Error during the transmission of the message");
                //todo ack failed
                e.printStackTrace();
                return;
            }catch (IOException e){
                System.out.println("A problem occurred while reading the message from the server");
                e.printStackTrace();
                return;
            }
            new Thread(() -> {
                try{
                    serverMsg.processMsg(this);
                }catch (Exception e) {
                    System.out.println("Error during the processing of the message");
                    //todo ack failed
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void setOwner(VirtualController owner) {
        this.owner = owner;
    }

    public boolean isReady() {
        return ready;
    }

    public VirtualController getOwner() {
        return owner;
    }

    public void setView(ViewInterface view) {
        this.view = view;
    }

    public ViewInterface getView() {
        return view;
    }

}
