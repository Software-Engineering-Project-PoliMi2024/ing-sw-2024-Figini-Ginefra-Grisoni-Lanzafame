package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cardReleted.CardFace;
import it.polimi.ingsw.model.cardReleted.ObjectiveCard;
import it.polimi.ingsw.model.playerReleted.Placement;


public abstract class Controller {
    public abstract void connect(String ip, int port, String nickname);
    public abstract void getActiveGameList();
    public abstract void joinGame(String gameName);
    public abstract void disconnect();
    public abstract void leaveLobby();
    public abstract void createGame(String gameName, int maxPlayerCount);
    public abstract void selectStartCardFace(CardFace cardFace);
    public abstract void peek(String nickName);
    public abstract void choseSecretObjective(ObjectiveCard objectiveCard);
    public abstract void place(Placement placement);
    public abstract void draw(int deckID, int cardID);
    public abstract void leaveGame();
}
