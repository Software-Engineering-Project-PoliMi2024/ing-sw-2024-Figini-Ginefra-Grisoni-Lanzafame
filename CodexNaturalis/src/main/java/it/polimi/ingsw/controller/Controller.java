package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.Interfaces.ControllerInterface;
import it.polimi.ingsw.controller.Interfaces.GameControllerReceiver;
import it.polimi.ingsw.controller.Interfaces.MalevolentPlayerManager;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.FatManLobbyList;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.GadgetGame;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.LittleBoyLobby;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.ChatMessage;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.view.ViewInterface;

/**
 * This class is the controller linked to every client
 */
public class Controller implements ControllerInterface, GameControllerReceiver {
    private String nickname;
    private final LobbyGameListsController lobbyGameListController;
    private final ViewInterface view;
    private GameController gameController;

    /**
     * Constructor of the class
     * @param lobbyGameListController the controller of the lobby and game lists
     * @param view the view of the client
     */
    public Controller(LobbyGameListsController lobbyGameListController, ViewInterface view){
        this.lobbyGameListController = lobbyGameListController;
        this.view = view;
    }

    @Override
    public void login(String nickname) {
        if(lobbyGameListController.login(nickname, view, this)) {
            this.nickname = nickname;
        }
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

    /**
     * The method checks if the gameController is not null (i.e. if the player is actually in a game)
     * @param objectiveCard the secret objective card chosen by the player
     */
    @Override
    public void chooseSecretObjective(LightCard objectiveCard) {
        if(gameController != null)
            gameController.chooseSecretObjective(nickname, objectiveCard);
        else {
            lobbyGameListController.manageMalevolentPlayer(nickname);
        }
    }

    @Override
    public void choosePawn(PawnColors color) {
        if(gameController != null)
            gameController.choosePawn(nickname, color);
        else {
            lobbyGameListController.manageMalevolentPlayer(nickname);
        }
    }

    @Override
    public void place(LightPlacement placement) {
        if(gameController != null)
            gameController.place(nickname, placement);
        else {
            lobbyGameListController.manageMalevolentPlayer(nickname);
        }
    }

    @Override
    public void draw(DrawableCard deckID, int cardID) {
        if(gameController != null)
            gameController.draw(nickname, deckID, cardID);
        else {
            lobbyGameListController.manageMalevolentPlayer(nickname);
        }
    }

    @Override
    public void sendChatMessage(ChatMessage message){
        if(gameController != null)
            gameController.sendChatMessage(message);
        else {
            lobbyGameListController.manageMalevolentPlayer(nickname);
        }
    }

    @Override
    public void leave() {
        lobbyGameListController.leave(nickname);
        this.eraseLightModel();
    }

    @Override
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * This method is used to erase the light model on the view
     * when the player leaves and returns to the main menu
     */
    private void eraseLightModel(){
        try {
            view.updateLobbyList(new FatManLobbyList());
            view.updateLobby(new LittleBoyLobby());
            view.updateGame(new GadgetGame());
        }catch (Exception ignored){}
    }
}
