package it.polimi.ingsw.controller2;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightPlacement;
import it.polimi.ingsw.view.View;

import java.io.Serializable;
import java.rmi.Remote;

public interface ControllerInterface extends Remote {
    void login(String nickname);
    void getActiveLobbyList();
    void createLobby(String gameName, int maxPlayerCount);
    void joinLobby(String lobbyName);
    void disconnect();
    void leaveLobby();
    void selectStartCardFace(LightCard card, CardFace cardFace);
    void choseSecretObjective(LightCard objectiveCard);
    void place(LightPlacement placement);
    void draw(DrawableCard deckID, int cardID);
    void leaveGame();
}