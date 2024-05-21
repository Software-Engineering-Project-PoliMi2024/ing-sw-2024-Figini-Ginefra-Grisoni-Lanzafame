package it.polimi.ingsw.controller;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

import java.io.Serializable;
import java.rmi.Remote;

public interface ControllerInterface extends Serializable, Remote {
    void login(String nickname) throws Exception;
    void createLobby(String gameName, int maxPlayerCount) throws Exception;
    void joinLobby(String lobbyName) throws Exception;
    void disconnect() throws Exception;
    void leaveLobby() throws Exception;
    void choseSecretObjective(LightCard objectiveCard) throws Exception;
    void place(LightPlacement placement) throws Exception;
    void draw(DrawableCard deckID, int cardID) throws Exception;
}
