package it.polimi.ingsw.controller.Interfaces;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.persistence.PersistenceFactory;
import it.polimi.ingsw.model.cardReleted.cards.CardTable;
import it.polimi.ingsw.view.ViewInterface;

/**
 * Interface that defines the methods that a lobby controller should implement
 * in order to manage the addition and removal players in the lobby and start the game
 */
public interface LobbyControllerInterface {
    /**
     * Add a player to the lobby
     * notify all the players views of the join
     * @param nickname player's nickname
     * @param view player's view
     * @param gameControllerReceiver player's game controller receiver
     */
    void addPlayer(String nickname, ViewInterface view, GameControllerReceiver gameControllerReceiver);

    /**
     * Remove a player from the lobby
     * notify all the players views of the leave
     * @param nickname player's nickname
     * @return the view of the player removed
     */
    ViewInterface removePlayer(String nickname);

    /**
     * Start the game with the players in the lobby
     * create the game adding to it all the players that were in lobby
     * and creates and bind to the gameController the newly created game
     * @param cardTable the card table used to map cards to their ids
     * @param persistenceFactory the persistence factory used to save the game
     * @param gameList the game list used to remove the game when it ends
     * @param malevolentPlayerManager the malevolent player manager used to manage malevolent players
     * @return the game controller created
     */
    GameController startGame(CardTable cardTable, PersistenceFactory persistenceFactory, GameList gameList, MalevolentPlayerManager malevolentPlayerManager);
}


