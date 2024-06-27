package it.polimi.ingsw.controller.Interfaces;

import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.rmi.Remote;

/**
 * Interface that defines the methods of the controller of the lobby list and of the game list
 * and manage the players interaction with the server
 */
public interface LobbyGameListsController extends GameList, MalevolentPlayerManager, Serializable, Remote {
    /**
     * Logs a player in the Lobby and Game List
     * If the nickname is already in use, the player is prompted to choose another one
     * If the nickname is already in a game, the player is reconnected to the game
     * If the nickname is not in a game, the player is reconnected to the lobby List
     * where he can create or join a lobby
     * @param nickname the nickname chosen by the player
     * @param view the view of the player
     * @param controllerReceiver the controller receiver of the player used to
     *                           set the gameController if the player is in a game
     * @return true if the player is successfully logged in, false otherwise
     */
    boolean login(String nickname, ViewInterface view, GameControllerReceiver controllerReceiver);

    /**
     * This method is used to create a lobby in the lobby list
     * If the lobby name is already taken, the player is prompted to choose another one
     * If the player is already in a lobby or a game, the player is considered malevolent
     * @param nickname the nickname of the player creating the lobby
     * @param gameName the name of the lobby
     * @param maxPlayerCount the maximum number of players that can join the lobby
     * @param gameReceiver the game controller receiver of the player
     */
    void createLobby(String nickname, String gameName, int maxPlayerCount, GameControllerReceiver gameReceiver);

    /**
     * This method is used to join a lobby in the lobby list
     * If the lobby name is not in the lobby list, the player is prompted to choose another one
     * If the player is already in a lobby or a game, the player is considered malevolent
     * If the lobby is full before joining, the player is prompted to choose another one
     * If the lobby is full after joining, the game starts
     * @param nickname the nickname of the player joining the lobby
     * @param lobbyName the name of the lobby to join
     * @param gameReceiver the game controller receiver of the player
     */
    void joinLobby(String nickname, String lobbyName, GameControllerReceiver gameReceiver);

    /**
     * This method is used to leave from the lobby or game List;
     * from whichever state the player is in, it moves him back to the main menu
     * @param nickname the nickname of the player leaving
     */
    void leave(String nickname);

    /**
     * This method is used to leave the lobby the player is in
     * and moves him back to the lobbyList
     * If the player is not in a lobby, the player is considered malevolent
     * @param nickname the nickname of the player leaving the lobby
     */
    void leaveLobby(String nickname);
}
