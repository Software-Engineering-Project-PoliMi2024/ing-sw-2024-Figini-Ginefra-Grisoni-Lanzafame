package it.polimi.ingsw.controller4.Interfaces;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

public interface ControllerInterface {
    void login(String nickname) throws Exception;
    void createLobby(String gameName, int maxPlayerCount) throws Exception;
    void joinLobby(String lobbyName) throws Exception;
    void disconnect() throws Exception;
    void leaveLobby() throws Exception;
    void chooseSecretObjective(LightCard objectiveCard) throws Exception;
    void place(LightPlacement placement) throws Exception;
    void draw(DrawableCard deckID, int cardID) throws Exception;
}
