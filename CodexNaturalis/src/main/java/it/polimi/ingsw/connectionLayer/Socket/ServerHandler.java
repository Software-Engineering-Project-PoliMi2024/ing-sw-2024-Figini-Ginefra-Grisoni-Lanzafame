package it.polimi.ingsw.connectionLayer.Socket;

import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.ClientMsg;
import it.polimi.ingsw.connectionLayer.Socket.ServerMsg.ServerMsg;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.controller2.LogsOnClient;
import it.polimi.ingsw.view.ViewInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class ServerHandler implements Runnable{
    private Socket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private VirtualController owner;
    private ViewInterface view;
    private boolean ready = false;
    private int msgIndex;
    private final Queue<ServerMsg> recivedMsgs = new PriorityQueue<>(Comparator.comparingInt(ServerMsg::getIndex));

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
        //inform the VirtualController that the connection streams are ready, wait for the view and the virtualController to be set
        ready = true;
        while(this.owner == null || this.view == null){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Thread listeningThread = new Thread(this::handleMsg, "Listening Thread");
        listeningThread.start();
    }

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
                    view.logErr(LogsOnClient.CONNECTION_LOST_CLIENT_SIDE.getMessage());
                }catch (IOException ex){
                    System.out.println("Error while closing the Socket of the Server");
                    e.printStackTrace();
                }
                return;
            }
            if(serverMsg.getIndex() > expectedIndex){
                recivedMsgs.add(serverMsg);
            }else if(serverMsg.getIndex()<expectedIndex){
                throw new IllegalCallerException("The Client received a message with an index lower than the expected one");
            }else{ //clientMsg.getIndex() == expectingIndex
                recivedMsgs.add(serverMsg);
                Queue<ServerMsg> toBeProcessMsgs = continueMessagesWindow(recivedMsgs, expectedIndex);
                expectedIndex = toBeProcessMsgs.stream().max(Comparator.comparingInt(ServerMsg::getIndex)).get().getIndex() + 1;
                Thread elaborateMsgThread = new Thread(() -> {
                    processMsg(toBeProcessMsgs);
                });
                elaborateMsgThread.start();
            }
        }
    }

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

    public Queue<ServerMsg> getRecivedMsgs() {
        return recivedMsgs;
    }
}
