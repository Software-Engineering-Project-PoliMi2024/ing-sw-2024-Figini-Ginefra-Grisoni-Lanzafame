package it.polimi.ingsw.connectionLayer.Socket;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.ClientMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.ServerMsg;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualView;
import it.polimi.ingsw.controller.Interfaces.ControllerInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is responsible for handling the connection to the client.
 * It is responsible for sending, receiving and processing messages from the client.
 * Ensure that the messages are processed in order even if they are received out of order.
 */
public class ClientHandler implements Runnable{
    /** The socket of the client */
    private final Socket client;
    /** The VirtualView. It is the owner because every message request is sent by it */
    private VirtualView owner;
    /** The output stream to the client */
    private ObjectOutputStream output;
    /** The input stream from the client */
    private ObjectInputStream input;
    /** The controller interface used to handle messages */
    private ControllerInterface controller;
    /** The next msgIndex expected by the clientHandler */
    private int msgIndex;
    /** The queue of messages received from the client but not already processed, ordered by index */
    private final Queue<ClientMsg> receivedMsgs = new PriorityQueue<>(Comparator.comparingInt(ClientMsg::getIndex));
    /** The boolean that indicates if the clientHandler is listening for messages */
    private volatile boolean isListening = true;
    /** The executor service used to process the messages */
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    /** The readiness of the client handler. It is true only when the setupPhase (run) end*/
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
     * Initialized the input and output streams and starts the listening thread
     */
    @Override
    public void run()
    {
        msgIndex = 0;
        try {
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
        } catch (IOException e) {
            System.out.println("could not open connection to " + client.getInetAddress());
            return;
        }
        System.out.println("Connected to " + client.getInetAddress());
        //Inform the Server that the connection streams are ready, wait for controller and virtualView to be set
        ready = true;
        while(this.owner == null || this.controller == null){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Configs.printStackTrace(e);
            }
        }
        Thread listeningThread = new Thread(this::handleMsg, "Listening Thread");
        listeningThread.start();

    }

    /**
     * Sends a message to the client
     * @param serverMsg The message to be sent
     */
    public void sendServerMessage(ServerMsg serverMsg){
        serverMsg.setIndex(msgIndex);
        msgIndex++;
        try{
            output.reset();
            output.writeObject(serverMsg);
        } catch (IOException e) {
            System.out.println("could not send message to " + client.getInetAddress() + ":" + client.getPort());
            Configs.printStackTrace(e);
        }
    }

    /**
     * Handles the messages received from the client. If a message is received out of order (i.e. with an index higher than the expected one)
     * it is stored in a queue until the expected message is received. Then all the messages in the queue are processed
     */
    private void handleMsg(){
        System.out.println("Listening for messages of " + client.getInetAddress() + ":" + client.getPort());
        int expectedIndex = 0;
        while(isListening) {
            ClientMsg clientMsg;
            try {
                clientMsg = (ClientMsg) input.readObject();
            } catch (ClassNotFoundException e) {
                //This scenario should not occur if a ClientMsg is being sent, as TCP ensures that every message is always received correctly.
                System.out.println("Error during the transmission of the message. The message was not a ClientMsg object.");
                Configs.printStackTrace(e);
                return;
            }catch (IOException e) { //This will catch a SocketException("Connection reset") when the client DISCONNECTS
                this.handleIOException(e);
                return;
            }
            synchronized (this){
                if(clientMsg.getIndex() > expectedIndex){
                    receivedMsgs.add(clientMsg);
                }else if(clientMsg.getIndex()<expectedIndex){
                    throw new IllegalCallerException("The Server received a message with an index lower than the expected one");
                }else{ //clientMsg.getIndex() == expectedIndex
                    receivedMsgs.add(clientMsg);
                    Queue<ClientMsg> toBeProcessMsgs = continueMessagesWindow(receivedMsgs, expectedIndex);
                    expectedIndex = toBeProcessMsgs.stream().max(Comparator.comparingInt(ClientMsg::getIndex)).get().getIndex() + 1;

                    executorService.submit(()-> processMsgs(toBeProcessMsgs));
                }
            }
        }
    }

    /**
     * handle the IOException thrown when the connection to the client is close.
     * @param e
     */
    private void handleIOException(IOException e) {
        try{
            System.out.println("Client " + client.getInetAddress() + ":" + client.getPort() + " disconnected");
            this.connectionLayerDisconnection();
            this.controller.leave();
        }catch (Exception ex){
            System.out.println("Error during the disconnection of the client");
            Configs.printStackTrace(ex);
        }
    }

    /**
     * Close the input and output stream and the socket to the client.
     * Stop the listening thread by setting isListening to false
     */
    private void connectionLayerDisconnection() {
        try {
            if (input != null){
                input.close();
            }
            if (output != null){
                output.close();
            }
            if (client != null && !client.isClosed()){
                client.close();
            }
            this.isListening = false;
        } catch (IOException e) {
            System.out.println("Error while closing the connection");
            Configs.printStackTrace(e);
        }
    }

    /**
     * @param queue The queue of messages already present on the server
     * @param expectedIndex The index of the next message expected by the server
     * * @return A queue of ClientMsg objects that form a continuous sequence starting from the expected index.
     *  If no such sequence can be formed, it will return a queue containing only the message with the expected index
     */
    private Queue<ClientMsg> continueMessagesWindow(Queue<ClientMsg> queue, int expectedIndex){
        Queue<ClientMsg> toBeProcessMsgs = new LinkedList<>();
        boolean continuous = true;
        while(continuous){
            ClientMsg nextMsg = queue.peek();
            if(nextMsg == null){ //the queue is empty, all the already received msgs are continuous and can be process
                return toBeProcessMsgs;
            }
            if(nextMsg.getIndex() == expectedIndex){
                toBeProcessMsgs.add(queue.poll());
                expectedIndex++;
            }else{
                continuous = false;
            }
        }
        return toBeProcessMsgs;
    }

    /**
     * Processes the messages in the queue
     * @param queue The queue of messages to be processed
     */
    private void processMsgs(Queue<ClientMsg> queue){
        while(!queue.isEmpty()){
            ClientMsg clientMsg = queue.poll();
            try {
                clientMsg.processMsg(this);
            }catch (Exception e) {
                System.out.println("Error during the processing of the message");
                Configs.printStackTrace(e);
            }
        }
    }

    /**
     * Sets the owner of the clientHandler
     * @param owner The VirtualView that is the owner of the clientHandler
     */
    public void setOwner(VirtualView owner) {
        this.owner = owner;
    }

    /**
     * Sets the controller of the clientHandler
     * @param controller The controller that is the owner of the clientHandler
     */
    public void setController(ControllerInterface controller){
        this.controller = controller;
    }

    /**
     * @return The VirtualView that is the owner of the clientHandler
     */
    public ControllerInterface getController(){
        return controller;
    }

    /**
     * @return true if the clientHandler completed the setup phaser
     */
    public boolean isReady() {
        return ready;
    }
}
