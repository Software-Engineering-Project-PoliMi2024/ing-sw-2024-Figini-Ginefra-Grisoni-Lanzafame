package it.polimi.ingsw.controller3;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.view.ViewInterface;

public class Controller3 implements ControllerInterface {
    private final ViewInterface view;
    private final ModelInteract modelInteract;
    private final MalevolentChecker malevolentChecker;

    public Controller3(MultiGame games, ViewInterface view) {
        this.view = view;
        this.modelInteract = new ModelInteract(games);
        this.malevolentChecker = new MalevolentChecker(games);
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

    @Override
    public void choseSecretObjective(LightCard objectiveCard) throws Exception {

    }

    @Override
    public void place(LightPlacement placement) throws Exception {

    }

    @Override
    public void draw(DrawableCard deckID, int cardID) throws Exception {

    }
}
