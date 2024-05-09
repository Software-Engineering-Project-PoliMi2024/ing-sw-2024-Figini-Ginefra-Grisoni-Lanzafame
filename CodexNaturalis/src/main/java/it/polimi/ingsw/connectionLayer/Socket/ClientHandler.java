package it.polimi.ingsw.connectionLayer.Socket;

import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.ClientMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.ServerMsg;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualView;
import it.polimi.ingsw.connectionLayer.VirtualSocket.VirtualViewSocket;
import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket client;
    private VirtualView owner;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ControllerInterface controller; //because the view is a viewInterface and the getter must return a viewInterface
    private boolean ready = false;

    /**
     * Initializes a new handler using a specific socket connected to
     * a client.
     * @param client The socket connection to the client.
     */
    public ClientHandler(Socket client){
        this.client = client;
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
        Thread listeningThread = new Thread(this::HandleMsg, "Listening Thread");
        listeningThread.start();
        ready = true;
    }

    public void sendServerMessage(ServerMsg serverMsg){
        //todo add queue of messages
        try{
            output.writeObject(serverMsg);
        } catch (IOException e) {
            System.out.println("could not send message to " + client.getInetAddress());
            e.printStackTrace();
        }
    }

    public void HandleMsg(){
        System.out.println("Listening for messages of " + client.getInetAddress());
        while(true) {
            ClientMsg clientMsg;
            try {
                clientMsg = (ClientMsg) input.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println("Error during the transmission of the message");
                //todo implement ack failed
                return;
            }catch (IOException e) {
                System.out.println("A problem occurred while reading the message from the server");
                e.printStackTrace();
                return;
            }
            new Thread(() -> {
                try{
                    clientMsg.processMsg(this);
                }catch (Exception e) {
                    System.out.println("Error during the processing of the message");
                    //todo ack failed
                    e.printStackTrace();
                }
            }).start();
        }
    }
    public void setOwner(VirtualView owner) {
        this.owner = owner;
    }

    public VirtualView getOwner() {
        return owner;
    }

    public void setController(ControllerInterface controller){
        this.controller = controller;
    }

    public ControllerInterface getController(){
        return controller;
    }

    public boolean isReady() {
        return ready;
    }
}
