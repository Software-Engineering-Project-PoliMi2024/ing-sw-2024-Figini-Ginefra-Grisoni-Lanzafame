package it.polimi.ingsw.controller.socket;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.socket.client.SocketClient;
import it.polimi.ingsw.controller.socket.messages.actionMessages.LoginMsg;
import it.polimi.ingsw.model.cardReleted.CardFace;
import it.polimi.ingsw.model.cardReleted.ObjectiveCard;
import it.polimi.ingsw.model.playerReleted.Placement;


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
    public void getActiveGameList() {

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
