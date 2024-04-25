package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.view.View;


public abstract class Controller {
    public abstract void connect(String ip, int port, String nickname, View view, Controller controller);
    public abstract void getActiveGameList();
    public abstract void joinGame(String gameName, String nickname);
    public abstract void disconnect();
    public abstract void leaveLobby();
    public abstract void createGame(String gameName, int maxPlayerCount);
    public abstract void selectStartCardFace(StartCard card, CardFace cardFace);
    public abstract void peek(String nickName);
    public abstract void choseSecretObjective(ObjectiveCard objectiveCard);
    public abstract void place(Placement placement);
    public abstract void draw(DrawableCard deckID, int cardID);
    public abstract void leaveGame();
}
