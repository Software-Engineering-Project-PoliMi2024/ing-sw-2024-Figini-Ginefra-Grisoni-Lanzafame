package it.polimi.ingsw.controller4.Interfaces;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.Remote;

public interface ControllerInterface extends Remote, Serializable {
    void login(String nickname) throws Exception;
    void createLobby(String gameName, int maxPlayerCount) throws Exception;
    void joinLobby(String lobbyName) throws Exception;
    void disconnect() throws Exception;
    void leaveLobby() throws Exception;
    void chooseSecretObjective(LightCard objectiveCard) throws Exception;
    void place(LightPlacement placement) throws Exception;
    void draw(DrawableCard deckID, int cardID) throws Exception;
}
