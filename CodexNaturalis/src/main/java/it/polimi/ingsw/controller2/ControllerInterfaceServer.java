package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightPlacement;
import it.polimi.ingsw.lightModel.diffLists.DiffSubscriber;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;

public interface ControllerInterfaceServer {
    void login(String nickname, DiffSubscriber diffSubscriber);
    void getActiveLobbyList(DiffSubscriber diffSubscriber);
    void createLobby(String gameName, int maxPlayerCount, DiffSubscriber diffSubscriber);
    void joinLobby(String lobbyName, DiffSubscriber diffSubscriber);
    void disconnect();
    void leaveLobby(DiffSubscriber diffSubscriber);
    void joinGame(Game game, DiffSubscriber diffSubscriber, LogsFromServer log);
    void leaveGame();
    void selectStartCardFace(LightCard card, CardFace cardFace);
    void choseSecretObjective(LightCard objectiveCard);
    void place(LightPlacement placement);
    void draw(DrawableCard deckID, int cardID);

}
