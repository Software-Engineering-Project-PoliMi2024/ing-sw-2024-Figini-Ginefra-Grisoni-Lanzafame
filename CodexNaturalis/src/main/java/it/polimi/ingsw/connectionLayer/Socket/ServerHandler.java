package it.polimi.ingsw.connectionLayer.Socket;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.ClientMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.ServerMsg;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.controller.LogsOnClient;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.TimeUnit;

/**
 * This class is responsible for handling the connection to the server.
 * It is responsible for sending receiving and processing messages from the server.
 * Ensure that the messages are processed in order even if they are received out of order.
 */
public class ServerHandler implements Runnable{
    /** The socket of the server*/
    private final Socket server;
    /** The output stream to the server*/
    private ObjectOutputStream output;
    /** The input stream from the server*/
    private ObjectInputStream input;
    /**The VirtualController. It is the owner because every message request is sent by it*/
    private VirtualController owner;
    /**The view to which each message is applied*/
    private ViewInterface view;
    /** The readiness of the server handler. It is true only when the setupPhase (run) end*/
    private boolean ready = false;
    /** The next msgIndex expected by the serverHandler*/
    private int msgIndex;
    /** The queue of messages received from the server but not already processed order by index*/
    private final Queue<ServerMsg> receivedMsgs = new PriorityQueue<>(Comparator.comparingInt(ServerMsg::getIndex));
    /** The boolean that indicates if the serverHandler is listening for messages*/
    private boolean isListening = true;
    /** The executor service used to process the messages*/
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Initializes a new handler using a specific socket that is present on a server.
     * @param server The socket connection to the server.
     */
    public ServerHandler(Socket server) {
        this.server = server;
    }

    /**
     * Initialized the input and output streams and starts the listening thread
     */
    @Override
    public void run(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try{
            Future<?> future = executor.submit(() -> {
                try {
                    output = new ObjectOutputStream(server.getOutputStream());
                    input = new ObjectInputStream(server.getInputStream());
                    System.out.println("Connected to " + server.getInetAddress());
                    this.waitForOwnerAndView();
                    Thread listeningThread = new Thread(this::handleMsg, "Listening Thread");
                    listeningThread.start();
                }catch (IOException e) {
                    //wait for the view to be set so that the error can be logged
                    this.waitForOwnerAndView();
                    try {
                        System.out.println("Error while creating the input and output streams");
                        view.logErr(LogsOnClient.CONNECTION_ERROR);
                        view.transitionTo(ViewState.SERVER_CONNECTION);
                    } catch (Exception ex) {
                        //This is socket, RemoteException should not be thrown
                        throw new RuntimeException(ex);
                    }
                }
            });
            future.get(Configs.secondsTimeOut, TimeUnit.SECONDS);
        }catch (TimeoutException e) {
            //wait for the view to be set so that the error can be logged
            this.waitForOwnerAndView();
            try {
                System.out.println("Error while connecting to the server, the server exists but did not expect this protocol");
                view.logErr(LogsOnClient.CONNECTION_ERROR);
                view.transitionTo(ViewState.SERVER_CONNECTION);
                server.close();
            } catch (Exception ex) {
                //This is socket, RemoteException should not be thrown
                throw new RuntimeException(ex);
            }
        }catch (InterruptedException | ExecutionException e){
            Configs.printStackTrace(e);
            //wait for the view to be set so that the error can be logged
            this.waitForOwnerAndView();
            try {
                System.out.println("Unknown error while connecting to the server");
                view.logErr(LogsOnClient.CONNECTION_ERROR);
                view.transitionTo(ViewState.SERVER_CONNECTION);
            } catch (Exception ex) {
                //This is socket, RemoteException should not be thrown
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Sends a message to the server
     * @param clientMsg The message to be sent
     */
    public void sendClientMessage(ClientMsg clientMsg) {
        clientMsg.setIndex(msgIndex);
        msgIndex++;
        try {
            output.reset();
            output.writeObject(clientMsg);
        } catch (Exception e) {
            System.out.println("could not send message to " + server.getInetAddress());
            Configs.printStackTrace(e);
        }
    }

    /**
     * Handles the messages received from the server. If a message is received out of order (i.e. with an index higher than the expected one)
     * it is stored in a queue until the expected message is received. Then all the messages in the queue are processed
     */
    private void handleMsg() {
        System.out.println("Listening for messages of " + server.getInetAddress());
        int expectedIndex = 0;
        while (isListening) {
            ServerMsg serverMsg;
            try {
                serverMsg = (ServerMsg) input.readObject();
            } catch (ClassNotFoundException e) {
                //This scenario should not occur if a ClientMsg is being sent, as TCP ensures that every message is always received correctly.
                System.out.println("Error during the transmission of the message. The message was not a ServerMsg object.");
                Configs.printStackTrace(e);
                return;
            }catch (IOException e){ //This will catch a SocketException("Connection reset") when something happens in the connection
                this.handleIOException(e);
                return;
            }
            synchronized (this){
                if(serverMsg.getIndex() > expectedIndex){
                    System.out.println("Received a message with an index higher than the expected one");
                    receivedMsgs.add(serverMsg);
                }else if(serverMsg.getIndex()<expectedIndex){
                    throw new IllegalCallerException("The Client received a message with an index lower than the expected one");
                }else{ //clientMsg.getIndex() == expectingIndex
                    receivedMsgs.add(serverMsg);
                    Queue<ServerMsg> toBeProcessMsgs = continueMessagesWindow(receivedMsgs, expectedIndex);
                    expectedIndex = toBeProcessMsgs.stream().max(Comparator.comparingInt(ServerMsg::getIndex)).get().getIndex() + 1;

                    executorService.submit(()-> processMsgs(toBeProcessMsgs));
                }
            }
        }
    }

    /**
     * handle the IOException thrown when the connection to the server is close.
     * if isListening is true when the exception is thrown, then the disconnection was not initiated by the user
     * @param e the IoException
     */
    private void handleIOException(IOException e) {
        try{
            this.connectionLayerDisconnection();
            if(isListening){
                owner.disconnect();
                view.logErr(LogsOnClient.CONNECTION_LOST_CLIENT_SIDE);
                view.transitionTo(ViewState.SERVER_CONNECTION);
                Configs.printStackTrace(e);
            }else{
                System.out.println("Stopped listening for messages of " + server.getInetAddress());
            }
        }catch (Exception ex){
            System.out.println("Error while printing the error message or transitioning to the SERVER_CONNECTION state");
            Configs.printStackTrace(ex);
        }
    }

    /**
     * Close the input and output stream and socket to the server
     */
    private void connectionLayerDisconnection() {
        try {
            if (input != null){
                input.close();
            }
            if (output != null){
                output.close();
            }
            if (server != null && !server.isClosed()){
                server.close();
            }
        } catch (IOException e) {
            System.out.println("Error while closing the connection");
            Configs.printStackTrace(e);
        }
    }

    /**
     * allow for a "soft disconnection" from the server.
     * stop the listeningThread by setting isListening to false and close the server socket
     */
    public void stopListening() {
        this.isListening = false;
        try {
            server.close();
        } catch (IOException e) {
            Configs.printStackTrace(e);
        }
    }

    /**
     * @param queue The queue of messages already present on the client
     * @param expectedIndex The index of the next message expected by the client
     * * @return A queue of ServerMsg objects that form a continuous sequence starting from the expected index.
     *  If no such sequence can be formed, it will return a queue containing only the message with the expected index
     */
    private Queue<ServerMsg> continueMessagesWindow(Queue<ServerMsg> queue, int expectedIndex){
        Queue<ServerMsg> toBeProcessMsgs = new LinkedList<>();
        boolean continuous = true;
        while(continuous){
            ServerMsg nextMsg = queue.peek();
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
    private void processMsgs(Queue<ServerMsg> queue) {
        while(!queue.isEmpty()){
            ServerMsg serverMsg = queue.poll();
            try {
                serverMsg.processMsg(this);
            }catch (Exception e) {
                System.out.println("Error during the processing of the message");
                Configs.printStackTrace(e);
            }
        }
    }

    /**
     * Set the serverHandler has ready, wait for the controller to set the owner and the view
     */
    private void waitForOwnerAndView(){
        this.ready = true;
        while(this.owner == null || this.view == null){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Configs.printStackTrace(e);
            }
        }
    }

    /**
     * Set the virtualController as the owner of the Handler
     * @param owner the VirtualController that will be the owner of the handler
     */
    public void setOwner(VirtualController owner) {
        this.owner = owner;
    }

    /**
     * @return true if the serverHandler completed the setup phase
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Set the view that runs the serverHandler
     * @param view the view that will be used to communicate with the user
     */
    public void setView(ViewInterface view) {
        this.view = view;
    }

    /**
     * Return the view that runs the serverHandler
     */
    public ViewInterface getView() {
        return view;
    }
}
