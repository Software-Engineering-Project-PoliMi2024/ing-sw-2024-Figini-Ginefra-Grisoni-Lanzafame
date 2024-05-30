package it.polimi.ingsw.controller4;

import it.polimi.ingsw.controller4.Interfaces.ControllerInterface;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

/**
 * This class is the controller linked to every client
 */
public class Controller implements ControllerInterface {
    @Override
    public void chooseSecretObjective(LightCard objectiveCard) throws Exception {

    }

    @Override
    public void place(LightPlacement placement) throws Exception {

    }

    @Override
    public void draw(DrawableCard deckID, int cardID) throws Exception {

    }

    @Override
    public void login(String nickname) throws Exception {

    }

    @Override
    public void createLobby(String gameName, int maxPlayerCount) throws Exception {

    }

    @Override
    public void joinLobby(String lobbyName) throws Exception {

    }

    @Override
    public void disconnect() throws Exception {

    }

    @Override
    public void leaveLobby() throws Exception {

    }
}
