package it.polimi.ingsw.controller2.persistence;

import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.tableReleted.Lobby;
import org.junit.jupiter.api.Test;

class persistenceFactoryTest {

    @Test
    void save() {
        MultiGame multiGame = new MultiGame();
        Lobby lobby = new Lobby(3, "gianni", "testGame");
        lobby.addUserName("gianni1");
        lobby.addUserName("gianni2");
        PersistenceFactory.save(multiGame.createGame(lobby));
    }

    @Test
    void load() {
    }

    @Test
    void deleteGameSave() {
    }
}