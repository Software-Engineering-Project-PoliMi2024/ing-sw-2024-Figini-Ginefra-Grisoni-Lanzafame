package it.polimi.ingsw.controller2.persistence;

import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;
import java.util.Objects;

class persistenceFactoryTest {

    @Test
    void save() {
        MultiGame multiGame = new MultiGame();
        Lobby lobby = new Lobby(3, "gianni", "testGame");
        lobby.addUserName("gianni1");
        lobby.addUserName("gianni2");
        PersistenceFactory.save(multiGame.createGame(lobby));

        File file = new File("gameSaves/testGame.ser");
        assert file.exists();
        file.delete();
    }

    @Test
    void load() {
        /*
        MultiGame multiGame = new MultiGame();
        Lobby lobby = new Lobby(3, "gianni", "testGame");
        lobby.addUserName("gianni1");
        lobby.addUserName("gianni2");
        PersistenceFactory.save(multiGame.createGame(lobby));

        MultiGame multiGame1 = new MultiGame();

        Game gameLoadedFromSave = multiGame1.getGameByName("testGame");
        System.out.println(gameLoadedFromSave);
        File file = new File("gameSaves/testGame.ser");
        assert file.exists();
        file.delete();
        assert Objects.equals(gameLoadedFromSave.getName(), "testGame");
        assert gameLoadedFromSave.getGameParty().getUsersList().size() == 3;
        assert gameLoadedFromSave.getGameParty().getUsersList().stream().map(User::getNickname).toList().contains("gianni");
        assert gameLoadedFromSave.getGameParty().getUsersList().stream().map(User::getNickname).toList().contains("gianni1");
        assert gameLoadedFromSave.getGameParty().getUsersList().stream().map(User::getNickname).toList().contains("gianni2");

        MultiGame multiGame2 = new MultiGame();
        assert multiGame2.getGameByName("testGame") == null;*/
    }

    @Test
    void deleteGameSave() {
        MultiGame multiGame = new MultiGame();
        Lobby lobby = new Lobby(3, "gianni", "testGame");
        lobby.addUserName("gianni1");
        lobby.addUserName("gianni2");
        Game gameCreated = multiGame.createGame(lobby);
        PersistenceFactory.save(gameCreated);
        PersistenceFactory.deleteGameSave(gameCreated);

        File file = new File("gameSaves/testGame.ser");
        assert !file.exists();
    }
}