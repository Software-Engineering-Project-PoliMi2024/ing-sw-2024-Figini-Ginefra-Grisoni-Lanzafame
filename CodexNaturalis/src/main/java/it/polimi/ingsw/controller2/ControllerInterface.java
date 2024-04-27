package it.polimi.ingsw.controller2;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightPlacement;

public interface ControllerInterface {
    void login(String nickname);
    void getActiveGameList();
    void createGame(String gameName, int maxPlayerCount);
    void joinLobby(String gameName, String nickname);
    void disconnect();
    void leaveLobby();
    void joinGame(String gameName, String nickname);
    void selectStartCardFace(LightCard card, CardFace cardFace);
    void peek(String nickName);
    void choseSecretObjective(LightCard objectiveCard);
    void place(LightPlacement placement);
    void draw(DrawableCard deckID, int cardID);
    void leaveGame();
}
