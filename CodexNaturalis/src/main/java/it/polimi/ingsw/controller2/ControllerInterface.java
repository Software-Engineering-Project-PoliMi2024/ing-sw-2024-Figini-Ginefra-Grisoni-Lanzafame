package it.polimi.ingsw.controller2;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightPlacement;

public interface ControllerInterface {
    void login(String nickname); //Sam
    void getActiveGameList(); //io
    void createGame(String gameName, int maxPlayerCount); //io
    void joinLobby(String lobbyName, String nickname);
    void disconnect();
    void leaveLobby();
    void joinGame(String gameName, String nickname); //io
    void selectStartCardFace(LightCard card, CardFace cardFace); //Sam
    void peek(String nickName); //sam
    void choseSecretObjective(LightCard objectiveCard); //sam
    void place(LightPlacement placement); //io
    void draw(DrawableCard deckID, int cardID); //io
    void leaveGame(); //io
}