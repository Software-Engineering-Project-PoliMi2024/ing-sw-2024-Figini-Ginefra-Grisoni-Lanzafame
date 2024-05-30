package it.polimi.ingsw.controller4;

import it.polimi.ingsw.controller4.Interfaces.ControllerInterface;
import it.polimi.ingsw.controller4.Interfaces.GameControllerInterface;
import it.polimi.ingsw.controller4.Interfaces.ReceptionControllerInterface;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;

/**
 * This class is the controller linked to every client
 */
public class Controller implements ControllerInterface {
    private final ReceptionController receptionController;
    private GameControllerInterface gameController;

    private String nickname;

    public Controller(ReceptionController receptionController){
        this.receptionController = receptionController;
    }

    public void setGameController(GameControllerInterface gameController) {
        this.gameController = gameController;
    }

    @Override
    public void chooseSecretObjective(LightCard objectiveCard) throws Exception {
        if(gameController == null)
            throw new Exception("Couldn't choose the Objective card because GameController is not set");

        if(nickname == null)
            throw new Exception("Couldn't choose the Objective card because nickname is not set");

        gameController.chooseSecretObjective(nickname, objectiveCard);
    }

    @Override
    public void place(LightPlacement placement) throws Exception {
        if(gameController == null)
            throw new Exception("Couldn't place the card because GameController is not set");

        if(nickname == null)
            throw new Exception("Couldn't place the card because nickname is not set");

        gameController.place(nickname, placement);
    }

    @Override
    public void draw(DrawableCard deckID, int cardID) throws Exception {
        if(gameController == null)
            throw new Exception("Couldn't draw the card because GameController is not set");

        if(nickname == null)
            throw new Exception("Couldn't draw the card because nickname is not set");

        gameController.draw(nickname, deckID, cardID);
    }

    @Override
    public void login(String nickname) throws Exception {
        if(receptionController.login(nickname))
            this.nickname = nickname;
    }

    @Override
    public void createLobby(String gameName, int maxPlayerCount) throws Exception {
        if(nickname == null)
            throw new Exception("Couldn't create the lobby because nickname is not set");
        receptionController.createLobby(nickname, gameName, maxPlayerCount);
    }

    @Override
    public void joinLobby(String lobbyName) throws Exception {
        if(nickname == null)
            throw new Exception("Couldn't join the lobby because nickname is not set");
        receptionController.joinLobby(nickname, lobbyName);
    }

    @Override
    public void disconnect() throws Exception {
        if(nickname == null)
            throw new Exception("Couldn't disconnect because nickname is not set");
        receptionController.disconnect(nickname);
    }

    @Override
    public void leaveLobby() throws Exception {
        if(nickname == null)
            throw new Exception("Couldn't leave the lobby because nickname is not set");
        receptionController.leaveLobby(nickname);
    }
}
