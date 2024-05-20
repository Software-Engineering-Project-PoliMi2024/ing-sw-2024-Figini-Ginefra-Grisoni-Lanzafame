package it.polimi.ingsw.connectionLayer.VirtualSocket;

import it.polimi.ingsw.connectionLayer.PingPongInterface;
import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.*;
import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.controller2.LogsOnClient;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.view.ViewInterface;

import java.io.IOException;
import java.net.Socket;

public class VirtualControllerSocket implements VirtualController {

    private ViewInterface view;
    private ServerHandler serverHandler;

    /**
     * Connects to the server
     * @param ip the ip of the server
     * @param port the port of the server
     * @param view the view that will be used to communicate with the user
     * @throws Exception if an error occurs in the view
     */
    @Override
    public void connect(String ip, int port, ViewInterface view) throws Exception {
        this.view = view;
        Socket server;
        try {
            server = new Socket(ip, port);
        } catch (IOException e) { //catch a "UnknownHostException" or a "ConnectException"
            System.out.println("Server unreachable, check the port and the ip address");
            view.logErr(LogsOnClient.CONNECTION_ERROR.getMessage());
            return;
        }
        //If the connection goes well
        this.serverHandler = new ServerHandler(server);
        Thread serverHandlerThread = new Thread(serverHandler, "serverHandler of " + server.getInetAddress().getHostAddress());
        serverHandlerThread.start();
        while (!serverHandler.isReady()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        serverHandler.setOwner(this); //set the owner only when the serverHandler is ready to receive and send messages
        serverHandler.setView(view);
    }

    @Override
    public void pingPong(){

    }

    @Override
    public void checkEmpty(){

    }

    @Override
    public void setPingPongStub(PingPongInterface pingPongStub){

    }

    @Override
    public void setControllerStub(ControllerInterface controllerStub){

    }

    @Override
    public void login(String nickname){
        serverHandler.sendServerMessage(new LoginMsg(nickname));
    }

    @Override
    public void createLobby(String gameName, int maxPlayerCount){
        serverHandler.sendServerMessage(new CreateLobbyMsg(gameName, maxPlayerCount));
    }

    @Override
    public void joinLobby(String lobbyName){
        serverHandler.sendServerMessage(new JoinLobbyMsg(lobbyName));
    }

    @Override
    public void disconnect(){
        serverHandler.sendServerMessage(new DisconnectMsg());
    }

    @Override
    public void leaveLobby(){
        serverHandler.sendServerMessage(new LeaveLobbyMsg());
    }

    @Override
    public void selectStartCardFace(CardFace cardFace){
        serverHandler.sendServerMessage(new SelectStartCardFaceMsg(cardFace));
    }

    @Override
    public void choseSecretObjective(LightCard objectiveCard){
        serverHandler.sendServerMessage(new ChoseSecretObjectiveMsg(objectiveCard));
    }

    @Override
    public void place(LightPlacement placement){
        serverHandler.sendServerMessage(new PlaceMsg(placement));
    }

    @Override
    public void draw(DrawableCard deckID, int cardID){
        serverHandler.sendServerMessage(new DrawMsg(deckID, cardID));
    }

    public ViewInterface getView() {
        return view;
    }
}
