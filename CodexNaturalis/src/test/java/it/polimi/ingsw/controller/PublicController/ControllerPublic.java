package it.polimi.ingsw.controller.PublicController;

import it.polimi.ingsw.controller4.GameController;
import it.polimi.ingsw.controller4.Interfaces.ControllerInterface;
import it.polimi.ingsw.controller4.Interfaces.GameControllerReceiver;
import it.polimi.ingsw.controller4.LobbyGameListController;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.view.ViewInterface;

public class ControllerPublic implements ControllerInterface, GameControllerReceiverPublicInterface {
    public String nickname;
    public final LobbyListControllerPublic lobbyGameListController;
    public final ViewInterface view;
    public GameControllerPublic gameController;

    public ControllerPublic(LobbyListControllerPublic lobbyGameListController, ViewInterface view) {
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
    public void place(LightPlacement placement) {
        gameController.place(nickname, placement);
    }

    @Override
    public void draw(DrawableCard deckID, int cardID) {
        gameController.draw(nickname, deckID, cardID);
    }

    @Override
    public void disconnect() throws Exception {
        lobbyGameListController.disconnect(nickname);
    }

    @Override
    public void setGameController(GameControllerPublic gameController) {
        this.gameController = gameController;
    }
}
