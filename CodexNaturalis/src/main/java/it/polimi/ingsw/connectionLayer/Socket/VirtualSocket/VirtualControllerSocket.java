package it.polimi.ingsw.connectionLayer.Socket.VirtualSocket;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.connectionLayer.HeartBeatInterface;
import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.*;
import it.polimi.ingsw.connectionLayer.Socket.ServerHandler;
import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.controller.Interfaces.ControllerInterface;
import it.polimi.ingsw.controller.LogsOnClient;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.FatManLobbyList;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.GadgetGame;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.LittleBoyLobby;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.ChatMessage;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.connectionLayer.Socket.ClientMsg.ChosePawnMsg;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.io.IOException;
import java.net.Socket;

/**
 * This class runs on the client, and it represents the controller on the Client.
 * Each action that the user does is translated into a message sent to the server,
 * and each action done by the server undergoes the same process.
 */
public class VirtualControllerSocket implements VirtualController {
    /**The view that is running on the Client*/
    private ViewInterface view;
    /**The object that runs the effective communication to the server*/
    private ServerHandler serverHandler;

    /**
     * Connects to the server. If the connection goes well, it creates a new ServerHandler and a new thread for it.
     * Set the owner of the serverHandler to this class and the view of the serverHandler to the view passed as parameter.
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
            this.view.logErr(LogsOnClient.CONNECTION_ERROR);
            this.view.transitionTo(ViewState.SERVER_CONNECTION);
            return;
        }
        //If the connection goes well
        if(serverHandler==null){
            this.serverHandler = new ServerHandler(server);
        }
        Thread serverHandlerThread = new Thread(serverHandler, "serverHandler of " + server.getInetAddress().getHostAddress());
        serverHandlerThread.start();
        while (!serverHandler.isReady()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Configs.printStackTrace(e);
            }
        }
        serverHandler.setOwner(this); //set the owner only when the serverHandler is ready to receive and send messages
        serverHandler.setView(view);
    }

    @Override
    public void heartBeat(){

    }

    @Override
    public void checkEmpty(){

    }

    @Override
    public void setHeartBeatStub(HeartBeatInterface heartBeatStub){

    }

    @Override
    public void setControllerStub(ControllerInterface controllerStub){

    }

    /**
     * Sends a login message to the server
     */
    @Override
    public void login(String nickname){
        serverHandler.sendClientMessage(new LoginMsg(nickname));
    }

    /**
     * Sends a crateLobby message to the server
     * @param gameName the name of the game
     * @param maxPlayerCount the maximum number of players that can join the lobby
     */
    @Override
    public void createLobby(String gameName, int maxPlayerCount){
        serverHandler.sendClientMessage(new CreateLobbyMsg(gameName, maxPlayerCount));
    }

    /**
     * Sends a joinLobby message to the server
     * @param lobbyName the name of the lobby that the player wants to join
     */
    @Override
    public void joinLobby(String lobbyName){
        serverHandler.sendClientMessage(new JoinLobbyMsg(lobbyName));
    }

    /**
     * Send a leave message to the server, stop the serverHandler and disconnect the client
     */
    @Override
    public void leave() {
        serverHandler.sendClientMessage(new LeaveMsg());
        serverHandler.stopListening();
        this.disconnect();
    }

    /**
     * Disconnect the client and erase the light model.
     * Could be called by the serverHandler when the connection is lost in a hard way
     */
    @Override
    public void disconnect(){
        this.eraseLightModel();
        try {
            view.transitionTo(ViewState.SERVER_CONNECTION);
        } catch (Exception ignored) {}
    }

    /**
     * Erase the light model through the NuclearDiff
     */
    private void eraseLightModel(){
        try {
            view.updateLobbyList(new FatManLobbyList());
            view.updateLobby(new LittleBoyLobby());
            view.updateGame(new GadgetGame());
        }catch (Exception ignored){}
    }

    /**
     * Sends a leaveLobby message to the server
     */
    @Override
    public void leaveLobby(){
        serverHandler.sendClientMessage(new LeaveLobbyMsg());
    }

    /**
     * Sends a chooseSecretObjective message to the server
     * @param objectiveCard the secret objective card chosen by the player
     */
    @Override
    public void chooseSecretObjective(LightCard objectiveCard){
        serverHandler.sendClientMessage(new ChoseSecretObjectiveMsg(objectiveCard));
    }

    /**
     * Send a choosePawn message to the server
     * @param color the pawn color chosen by the player
     */
    @Override
    public void choosePawn(PawnColors color) {
        serverHandler.sendClientMessage(new ChosePawnMsg(color));
    }

    /**
     * Send a place message to the server
     * @param placement the placement of the lightCard
     */
    @Override
    public void place(LightPlacement placement){
        serverHandler.sendClientMessage(new PlaceMsg(placement));
    }

    /**
     * Send a draw message to the server
     * @param deckID the deck from which the card is drawn (either Resource or Gold)
     * @param cardID the position of the card to draw (0,1 for the buffer, 2 for the deck)
     */
    @Override
    public void draw(DrawableCard deckID, int cardID){
        serverHandler.sendClientMessage(new DrawMsg(deckID, cardID));
    }

    /**
     * Send a chat message to the server
     * @param message the message to send
     */
    @Override
    public void sendChatMessage(ChatMessage message){
        serverHandler.sendClientMessage(new ChatMsg(message));
    }
}
