package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.Interfaces.ControllerInterface;
import it.polimi.ingsw.controller.Interfaces.GameControllerReceiver;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.ViewInterface;

/**
 * This class is the controller linked to every client
 */
public class Controller implements ControllerInterface, GameControllerReceiver {
    private String nickname;
    private final LobbyGameListController lobbyGameListController;
    private final ViewInterface view;
    private GameController gameController;

    public Controller(LobbyGameListController lobbyGameListController, ViewInterface view){
        this.lobbyGameListController = lobbyGameListController;
        this.view = view;
    }

    @Override
    public void login(String nickname) {
        this.nickname = nickname;
        lobbyGameListController.login(nickname, view);
    }

    @Override
    public void createLobby(String gameName, int maxPlayerCount) {
        lobbyGameListController.createLobby(nickname, gameName, maxPlayerCount, this);
    }

    @Override
    public void joinLobby(String lobbyName) {
        lobbyGameListController.joinLobby(nickname, lobbyName, this);
    }

    @Override
    public void leaveLobby() {
        lobbyGameListController.leaveLobby(nickname);
    }

    @Override
    public void chooseSecretObjective(LightCard objectiveCard) {
        gameController.chooseSecretObjective(nickname, objectiveCard);
    }

    @Override
    public void choosePawn(PawnColors color) {
        gameController.choosePawn(nickname, color);
    }

    @Override
    public void place(LightPlacement placement) {
        gameController.place(nickname, placement);
    }

    @Override
    public void draw(DrawableCard deckID, int cardID) {
        gameController.draw(nickname, deckID, cardID);
    }

    @Override
    public void disconnect() {
        lobbyGameListController.disconnect(nickname);
    }

    @Override
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
