package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

public class ClientRMI implements ControllerInterfaceClient{
    @Override
    public void connect(String ip, int port) {

    }

    public void login(String nickname) {

    }

    @Override
    public void getActiveLobbyList() {

    }

    @Override
    public void createLobby(String gameName, int maxPlayerCount) {

    }

    @Override
    public void joinLobby(String lobbyName, String nickname) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void leaveLobby() {

    }

    @Override
    public void selectStartCardFace(LightCard card, CardFace cardFace) {

    }

    @Override
    public void peek(String nickName) {

    }

    @Override
    public void choseSecretObjective(LightCard objectiveCard) {

    }

    @Override
    public void place(LightPlacement placement) {

    }

    @Override
    public void draw(DrawableCard deckID, int cardID) {

    }

    @Override
    public void leaveGame() {

    }
}
