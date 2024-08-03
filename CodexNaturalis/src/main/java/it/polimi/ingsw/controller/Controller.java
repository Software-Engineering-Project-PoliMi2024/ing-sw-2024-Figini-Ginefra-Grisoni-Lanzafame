package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.Interfaces.ControllerInterface;
import it.polimi.ingsw.controller.Interfaces.GameControllerReceiver;
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
 * It is used to manage interaction between the player and the server
 */
public class Controller implements ControllerInterface, GameControllerReceiver {
    /** The nickname of the player linked to the controller*/
    private String nickname;
    /** The controller of the lobby and game lists to manage the player's interaction with the lobby and the game lists*/
    private final LobbyGameListsController lobbyGameListController;
    /** The controller of the game to manage the player's interaction with the game*/
    private GameController gameController;
    /** The view of the client to update*/
    private final ViewInterface view;

    /**
     * Constructor of the class
     * @param lobbyGameListController the controller of the lobby and game lists
     * @param view the view of the client
     */
    public Controller(LobbyGameListsController lobbyGameListController, ViewInterface view){
        this.lobbyGameListController = lobbyGameListController;
        this.view = view;
    }

    /**
     * The method forwards the call to the lobbyGameListController to check if the nickname is already taken
     * if it is not taken, the method sets the nickname of the player
     * @param nickname the nickname chosen by the player
     */
    @Override
    public void login(String nickname) {
        if(lobbyGameListController.login(nickname, view, this)) {
            this.nickname = nickname;
        }
    }

    /**
     * The method forwards the call to the lobbyGameListController to create a lobby
     * @param gameName the name of the lobby
     * @param maxPlayerCount the maximum number of players that can join the lobby
     */
    @Override
    public void createLobby(String gameName, int maxPlayerCount, int numberOfAgents) {
        lobbyGameListController.createLobby(nickname, gameName, maxPlayerCount, numberOfAgents, this);
    }

    /**
     * The method forwards the call to the lobbyGameListController to join a lobby
     * @param lobbyName the name of the lobby to join
     */
    @Override
    public void joinLobby(String lobbyName) {
        lobbyGameListController.joinLobby(nickname, lobbyName, this);
    }

    /**
     * The method forwards the call to the lobbyGameListController to leave the lobby
     */
    @Override
    public void leaveLobby() {
        lobbyGameListController.leaveLobby(nickname);
    }

    /**
     * The method checks if the gameController is not null (i.e. if the player is actually in a game)
     * if it is not null, the method forwards the call to the gameController,
     * to choose the secret objective card of the player,
     * otherwise it sets the player as malevolent
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

    /**
     * The method checks if the gameController is not null (i.e. if the player is actually in a game)
     * if it is not null, the method forwards the call to the gameController,
     * to choose the pawn color of the player,
     * otherwise it sets the player as malevolent
     * @param color the pawn color chosen by the player
     */
    @Override
    public void choosePawn(PawnColors color) {
        if(gameController != null)
            gameController.choosePawn(nickname, color);
        else {
            lobbyGameListController.manageMalevolentPlayer(nickname);
        }
    }

    /**
     * The method checks if the gameController is not null (i.e. if the player is actually in a game)
     * if it is not null, the method forwards the call to the gameController,
     * to place a card in from the hand to the codex,
     * @param placement the placement of the pawn
     */
    @Override
    public void place(LightPlacement placement) {
        if(gameController != null)
            gameController.place(nickname, placement);
        else {
            lobbyGameListController.manageMalevolentPlayer(nickname);
        }
    }

    /**
     * The method checks if the gameController is not null (i.e. if the player is actually in a game)
     * if it is not null, the method forwards the call to the gameController,
     * to draw a card from the deck,
     * otherwise it sets the player as malevolent
     * @param deckID the deck from which the card is drawn (either Resource or Gold)
     * @param cardID the position of the card to draw (0,1 for the buffer, 2 for the deck)
     */
    @Override
    public void draw(DrawableCard deckID, int cardID) {
        if(gameController != null)
            gameController.draw(nickname, deckID, cardID);
        else {
            lobbyGameListController.manageMalevolentPlayer(nickname);
        }
    }

    /**
     * The method checks if the gameController is not null (i.e. if the player is actually in a game)
     * if it is not null, the method forwards the call to the gameController,
     * to send a chat message,
     * otherwise it sets the player as malevolent
     * @param message the message to send
     */
    @Override
    public void sendChatMessage(ChatMessage message){
        if(gameController != null)
            gameController.sendChatMessage(message);
        else {
            lobbyGameListController.manageMalevolentPlayer(nickname);
        }
    }

    /**
     * The method checks if the gameController is not null (i.e. if the player is actually in a game)
     * if it is not null, the method forwards the call to the gameController,
     * to leave from the server,
     * otherwise it sets the player as malevolent
     */
    @Override
    public void leave() {
        lobbyGameListController.leave(nickname);
        this.eraseLightModel();
    }

    /**
     * The method is used at the start of the game to set the game controller
     * of the game the player is in
     * @param gameController the game controller to set
     */
    @Override
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * This method is used to erase the light model linked to the view
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
