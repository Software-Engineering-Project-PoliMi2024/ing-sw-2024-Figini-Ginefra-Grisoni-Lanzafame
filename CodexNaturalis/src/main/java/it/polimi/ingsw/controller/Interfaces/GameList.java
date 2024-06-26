package it.polimi.ingsw.controller.Interfaces;

public interface GameList {
    /**
     * This method is used to remove a game to the list of games
     * in order to free the resources used by the game
     * @param gameName is the name of the game to be removed
     */
    void deleteGame(String gameName);
}
