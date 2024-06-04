package it.polimi.ingsw.connectionLayer.Socket;

import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.ClientMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.ServerMsg;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualView;
import it.polimi.ingsw.controller.Interfaces.ControllerInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class ClientHandler implements Runnable{
    private final Socket client;
    private VirtualView owner;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ControllerInterface controller;
    private int msgIndex;
    //Create a queue of messages ordered by index, lower index first
    private final Queue<ClientMsg> recivedMsgs = new PriorityQueue<>(Comparator.comparingInt(ClientMsg::getIndex));

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
                e.printStackTrace();
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
            output.writeObject(serverMsg);
        } catch (IOException e) {
            System.out.println("could not send message to " + client.getInetAddress() + ":" + client.getPort());
            e.printStackTrace();
        }
    }

    /**
     * Handles the messages received from the client. If a message is received out of order (i.e. with an index higher than the expected one)
     * it is stored in a queue until the expected message is received. Then all the messages in the queue are processed
     */
    private void handleMsg(){
        System.out.println("Listening for messages of " + client.getInetAddress() + ":" + client.getPort());
        int expectedIndex = 0;
        while(true) {
            ClientMsg clientMsg;
            try {
                clientMsg = (ClientMsg) input.readObject();
            } catch (ClassNotFoundException e) {
                //This scenario should not occur if a ClientMsg is being sent, as TCP ensures that every message is always received correctly.
                System.out.println("Error during the transmission of the message. The message was not a ClientMsg object.");
                e.printStackTrace();
                return;
            }catch (IOException e) { //This will catch a SocketException("Connection reset") when the client disconnects
                try{
                    this.controller.disconnect();
                    client.close();
                }catch (Exception ex){
                    System.out.println("Error during the disconnection of the client");
                    ex.printStackTrace();
                }
                return;
            }
            if(clientMsg.getIndex() > expectedIndex){
                recivedMsgs.add(clientMsg);
            }else if(clientMsg.getIndex()<expectedIndex){
                throw new IllegalCallerException("The Server received a message with an index lower than the expected one");
            }else{ //clientMsg.getIndex() == expectedIndex
                recivedMsgs.add(clientMsg);
                Queue<ClientMsg> toBeProcessMsgs = continueMessagesWindow(recivedMsgs, expectedIndex);
                expectedIndex = toBeProcessMsgs.stream().max(Comparator.comparingInt(ClientMsg::getIndex)).get().getIndex() + 1;
                Thread elaborateMsgThread = new Thread(() -> {
                    processMsgs(toBeProcessMsgs);
                });
                elaborateMsgThread.start();
            }
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
                e.printStackTrace();
            }
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

    public Queue<ClientMsg> getReceivedMsg() {
        return recivedMsgs;
    }
}
