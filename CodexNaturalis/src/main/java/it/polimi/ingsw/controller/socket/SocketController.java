package it.polimi.ingsw.controller.socket;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.socket.client.SocketClient;
import it.polimi.ingsw.controller.socket.messages.actionMessages.*;
import it.polimi.ingsw.model.cardReleted.CardFace;
import it.polimi.ingsw.model.cardReleted.ObjectiveCard;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.view.View;


public class SocketController extends Controller {

    private SocketClient socketClient;

    public SocketController()
    {
        super();
    }

    /**
     * Create a new Thread with a socketClient.
     * Send a LoginMsg to the server
     *
     * @param ip         the IP address of the server
     * @param port       the socketPort of the server
     * @param nickname   of the User
     * @param view       set by the Client during the setup
     * @param controller set by the Client during the setup
     */
    @Override
    public void connect(String ip, int port, String nickname, View view, Controller controller) {
        //client Creation
        socketClient = new SocketClient(ip, port, nickname, view, controller);
        Thread clientThread = new Thread(socketClient, "client");
        clientThread.start();

        while(socketClient.getServerHandler()==null || !socketClient.getServerHandler().isReady()){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        socketClient.getServerHandler().sendActionMessage(new LoginMsg(nickname));

        //Update view composition
    }

    /**
     * Send a GetActiveGameListActionMsg to the server
     */
    @Override
    public void getActiveGameList() {
        socketClient.getServerHandler().sendActionMessage(new GetActiveGameListActionMsg());
    }

    @Override
    public void joinGame(String gameName, String nickname) {
        socketClient.getServerHandler().sendActionMessage(new JoinGameMsg(gameName, nickname));
    }

    @Override
    public void disconnect() {
        socketClient.getServerHandler().sendActionMessage(new DisconnectMsg());
    }

    @Override
    public void leaveLobby() {
        socketClient.getServerHandler().sendActionMessage(new LeaveLobbyMsg());
    }

    @Override
    public void createGame(String gameName, int maxPlayerCount) {
        socketClient.getServerHandler().sendActionMessage(new CreateGameMsg(gameName, maxPlayerCount));
    }

    @Override
    public void selectStartCardFace(CardFace cardFace) {

    }

    @Override
    public void peek(String nickName) {

    }

    @Override
    public void choseSecretObjective(ObjectiveCard objectiveCard) {

    }

    @Override
    public void place(Placement placement) {

    }

    @Override
    public void draw(int deckID, int cardID) {

    }

    @Override
    public void leaveGame() {

    }

    public SocketClient getSocketClient()
    {
        return socketClient;
    }

}
