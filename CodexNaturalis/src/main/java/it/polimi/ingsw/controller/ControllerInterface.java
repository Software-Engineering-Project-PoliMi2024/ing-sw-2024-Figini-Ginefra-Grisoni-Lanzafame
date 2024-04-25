package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.lightModel.LightCard;
import it.polimi.ingsw.model.lightModel.LightPlacement;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.view.ViewState;

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
