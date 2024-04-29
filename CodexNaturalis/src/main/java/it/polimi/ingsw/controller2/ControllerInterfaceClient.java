package it.polimi.ingsw.controller2;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightPlacement;

public interface ControllerInterfaceClient {
    void connect(String ip, int port);
    void login(String nickname);
    void getActiveLobbyList();
    void createLobby(String gameName, int maxPlayerCount);
    void joinLobby(String lobbyName);
    void disconnect();
    void leaveLobby();
    void selectStartCardFace(LightCard card, CardFace cardFace);
    void peek(String nickName);
    void choseSecretObjective(LightCard objectiveCard);
    void place(LightPlacement placement);
    void draw(DrawableCard deckID, int cardID);
    void leaveGame();
}