package it.polimi.ingsw.controller.socket;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.socket.client.SocketClient;
import it.polimi.ingsw.controller.socket.messages.actionMessages.LoginMsg;
import it.polimi.ingsw.model.cardReleted.Card;
import it.polimi.ingsw.model.cardReleted.CardFace;
import it.polimi.ingsw.model.cardReleted.ObjectiveCard;
import it.polimi.ingsw.model.playerReleted.Frontier;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.tableReleted.Game;

import java.util.List;


public class SocketController extends Controller {

    private SocketClient socketClient;

    public SocketController()
    {
        super();
    }

    @Override
    public void connect(String ip, int port, String nickname) {
        socketClient = new SocketClient(ip, port, nickname);
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

    @Override
    public List<Game> getActiveGameList() {
        return null;
    }

    @Override
    public void joinGame(String gameName) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void leaveLobby() {

    }

    @Override
    public void createGame(String gameName, int maxPlayerCount) {

    }

    @Override
    public void selectStartCardFace(CardFace cardFace) {

    }

    @Override
    public void peek(String nickName) {

    }

    @Override
    public void choseSecrectObjective(ObjectiveCard objectiveCard) {

    }

    @Override
    public Frontier place(Placement placement) {
        return null;
    }

    @Override
    public Card draw(int deckID, int cardID) {
        return null;
    }

    @Override
    public void leaveGame() {

    }

    public SocketClient getSocketClient()
    {
        return socketClient;
    }

}
