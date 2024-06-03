package it.polimi.ingsw.connectionLayer.Socket;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.ClientMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.ServerMsg;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.controller4.LogsOnClientStatic;
import it.polimi.ingsw.controller4.LogsOnClientStatic;
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

public class ServerHandler implements Runnable{
    private final Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private VirtualController owner;
    private ViewInterface view;
    private boolean ready = false;
    private int msgIndex;
    private final Queue<ServerMsg> receivedMsg = new PriorityQueue<>(Comparator.comparingInt(ServerMsg::getIndex));

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
                        view.logErr(LogsOnClientStatic.CONNECTION_ERROR);
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
                view.logErr(LogsOnClientStatic.CONNECTION_ERROR.getMessage());
                view.transitionTo(ViewState.SERVER_CONNECTION);
            } catch (Exception ex) {
                //This is socket, RemoteException should not be thrown
                throw new RuntimeException(ex);
            }
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
            //wait for the view to be set so that the error can be logged
            this.waitForOwnerAndView();
            try {
                System.out.println("Unknown error while connecting to the server");
                view.logErr(LogsOnClientStatic.CONNECTION_ERROR);
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
    public void sendServerMessage(ClientMsg clientMsg) {
        clientMsg.setIndex(msgIndex);
        msgIndex++;
        try {
            output.writeObject(clientMsg);
        } catch (Exception e) {
            System.out.println("could not send message to " + server.getInetAddress());
            e.printStackTrace();
        }
    }

    /**
     * Handles the messages received from the server. If a message is received out of order (i.e. with an index higher than the expected one)
     * it is stored in a queue until the expected message is received. Then all the messages in the queue are processed
     */
    private void handleMsg() {
        System.out.println("Listening for messages of " + server.getInetAddress());
        int expectedIndex = 0;
        while (true) {
            ServerMsg serverMsg;
            try {
                serverMsg = (ServerMsg) input.readObject();
            } catch (ClassNotFoundException e) {
                //This scenario should not occur if a ClientMsg is being sent, as TCP ensures that every message is always received correctly.
                System.out.println("Error during the transmission of the message. The message was not a ServerMsg object.");
                e.printStackTrace();
                return;
            }catch (IOException e){ //This will catch a SocketException("Connection reset") when the server crashes
                try{
                    server.close();
                    view.logErr(LogsOnClientStatic.CONNECTION_LOST_CLIENT_SIDE);
                }catch (Exception ex){
                    System.out.println("Error while closing the Socket of the Server");
                    e.printStackTrace();
                }
                return;
            }
            if(serverMsg.getIndex() > expectedIndex){
                receivedMsg.add(serverMsg);
            }else if(serverMsg.getIndex()<expectedIndex){
                throw new IllegalCallerException("The Client received a message with an index lower than the expected one");
            }else{ //clientMsg.getIndex() == expectingIndex
                receivedMsg.add(serverMsg);
                Queue<ServerMsg> toBeProcessMsgs = continueMessagesWindow(receivedMsg, expectedIndex);
                expectedIndex = toBeProcessMsgs.stream().max(Comparator.comparingInt(ServerMsg::getIndex)).get().getIndex() + 1;
                Thread elaborateMsgThread = new Thread(() -> {
                    processMsg(toBeProcessMsgs);
                });
                elaborateMsgThread.start();
            }
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
    private void processMsg(Queue<ServerMsg> queue) {
        while(!queue.isEmpty()){
            ServerMsg clientMsg = queue.poll();
            try {
                clientMsg.processMsg(this);
            }catch (Exception e) {
                System.out.println("Error during the processing of the message");
                e.printStackTrace();
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
                e.printStackTrace();
            }
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

    public Queue<ServerMsg> getReceivedMsg() {
        return receivedMsg;
    }
}
