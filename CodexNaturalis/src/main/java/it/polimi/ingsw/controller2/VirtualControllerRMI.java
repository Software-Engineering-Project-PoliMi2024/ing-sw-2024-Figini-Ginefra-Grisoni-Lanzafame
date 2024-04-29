package it.polimi.ingsw.controller2;

import it.polimi.ingsw.controller.RMI.LabelAPI;
import it.polimi.ingsw.controller.RMI.remoteInterfaces.LoginRMI;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class VirtualControllerRMI implements ControllerInterface, ControllerInterfaceClient {
    public void connect(String ip, int port,ViewInterface view) {
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            ConnectionServerRMI connect = (ConnectionServerRMI) registry.lookup("connect");
            connect.connect(view);
        } catch (Exception e) {
            e.printStackTrace();
            view.log(LogsFromServer.CONNECTION_ERROR.getMessage());
        }
    }
    @Override
    public void login(String nickname) {

    }

    @Override
    public void getActiveLobbyList() {

    }

    @Override
    public void createLobby(String gameName, int maxPlayerCount) {

    }

    @Override
    public void joinLobby(String lobbyName) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void leaveLobby() {

    }

    @Override
    public void selectStartCardFace(LightCard card, CardFace cardFace) {

    }

    @Override
    public void choseSecretObjective(LightCard objectiveCard) {

    }

    @Override
    public void place(LightPlacement placement) {

    }

    @Override
    public void draw(DrawableCard deckID, int cardID) {

    }

    @Override
    public void leaveGame() {

    }
}
