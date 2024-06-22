package it.polimi.ingsw.controller.Interfaces;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.persistence.PersistenceFactory;
import it.polimi.ingsw.model.cardReleted.cards.CardTable;
import it.polimi.ingsw.view.ViewInterface;

public interface LobbyControllerInterface {
    void addPlayer(String nickname, ViewInterface view, GameControllerReceiver gameControllerReceiver);
    ViewInterface removePlayer(String nickname);
    GameController startGame(CardTable cardTable, PersistenceFactory persistenceFactory, GameList gameList, MalevolentPlayerManager malevolentPlayerManager);
}


