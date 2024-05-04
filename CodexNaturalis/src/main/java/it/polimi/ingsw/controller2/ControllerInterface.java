package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControllerInterface extends Serializable, Remote {
    void login(String nickname) throws RemoteException;
    void createLobby(String gameName, int maxPlayerCount) throws RemoteException;
    void joinLobby(String lobbyName) throws RemoteException;
    void disconnect() throws RemoteException;
    void leaveLobby() throws RemoteException;
    void selectStartCardFace(LightCard card, CardFace cardFace) throws RemoteException;
    void choseSecretObjective(LightCard objectiveCard) throws RemoteException;
    void place(LightPlacement placement) throws RemoteException;
    void draw(DrawableCard deckID, int cardID) throws RemoteException;
    void receiveHeartbeat(Boolean isOn) throws RemoteException;
}
