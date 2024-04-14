package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.cardReleted.Card;
import it.polimi.ingsw.model.cardReleted.CardFace;
import it.polimi.ingsw.model.cardReleted.ObjectiveCard;
import it.polimi.ingsw.model.playerReleted.Frontier;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.tableReleted.Game;

import java.util.List;

public abstract class Controller {
    public abstract void connect(String ip, int port, String nickname);
    public abstract List<Game> getActiveGameList();
    public abstract void joinGame(String gameName);
    public abstract void disconnect();
    public abstract void leaveLobby();
    public abstract void createGame(String gameName, int maxPlayerCount);
    public abstract void selectStartCardFace(CardFace cardFace);
    public abstract void peek(String nickName); //Maybe not void?
    public abstract void choseSecrectObjective(ObjectiveCard objectiveCard);
    public abstract Frontier place(Placement placement);
    public abstract Card draw(int deckID, int cardID);
    public abstract void leaveGame();
}
