package it.polimi.ingsw.controller3;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.controller3.mediatorSubscriber.FullMediatorSubscriber;
import it.polimi.ingsw.controller3.mediators.LobbyListMediator;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.view.ViewInterface;

public class Controller3 implements ControllerInterface, FullMediatorSubscriber{
    private final ViewInterface view;
    private final ModelInteract modelInteract;
    private final MalevolentChecker malevolentChecker;

    public Controller3(MultiGame games, ViewInterface view) {
        this.view = view;
        this.modelInteract = new ModelInteract(games);
        this.malevolentChecker = new MalevolentChecker(games);
    }
    @Override
    public void login(String nickname) {
        LobbyListMediator lobbyListMediator = new LobbyListMediator();
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
    public void choseSecretObjective(LightCard objectiveCard) {

    }

    @Override
    public void place(LightPlacement placement) {

    }

    @Override
    public void draw(DrawableCard deckID, int cardID) {

    }
}
