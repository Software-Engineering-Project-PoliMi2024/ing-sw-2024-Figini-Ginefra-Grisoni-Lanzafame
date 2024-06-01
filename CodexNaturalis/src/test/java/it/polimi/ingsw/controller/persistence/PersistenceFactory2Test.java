package it.polimi.ingsw.controller.persistence;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.OSRelated;
import it.polimi.ingsw.model.Reception;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PersistenceFactory2Test {
    @BeforeEach
    void setUp() { //emulate the serverStartUP
        OSRelated.checkOrCreateDataFolderServer();
    }

    @AfterEach
    void tearDown() {
        System.out.println("---------Test finished---------");
    }

    @Test
    void saveTest(){
        Reception reception = new Reception();
        Lobby lobby = new Lobby(3, "gianni", "saveGameTest");
        lobby.addUserName("gianni1");
        lobby.addUserName("gianni2");
        Game gameToSave = reception.createGame(lobby);
        PersistenceFactory2.save(gameToSave);

        List<File> gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        Assertions.assertEquals(1, gameSaves.stream().filter(file -> file.getName().contains("saveGameTest")).count());
    }

    @Test
    void load() {
        Reception reception = new Reception();
        Lobby lobby = new Lobby(3, "gianni", "loadGameTest");
        lobby.addUserName("gianni1");
        lobby.addUserName("gianni2");
        Game gameToSave = reception.createGame(lobby);
        PersistenceFactory2.save(gameToSave);

        Reception reception1 = new Reception();
        Game gameLoadedFromSave = reception1.getGameByName("loadGameTest");

        assert Objects.equals(gameLoadedFromSave.getName(), "loadGameTest");
        assert gameLoadedFromSave.getGameParty().getUsersList().size() == 3;
        assert gameLoadedFromSave.getGameParty().getUsersList().stream().map(User::getNickname).toList().contains("gianni");
        assert gameLoadedFromSave.getGameParty().getUsersList().stream().map(User::getNickname).toList().contains("gianni1");
        assert gameLoadedFromSave.getGameParty().getUsersList().stream().map(User::getNickname).toList().contains("gianni2");
    }

    @Test
    void loadWithExpired() throws IOException {
        //create a game and save it
        Reception reception = new Reception();
        Lobby lobby = new Lobby(3, "gianni", "loadGameExpiredTest");
        lobby.addUserName("gianni1");
        lobby.addUserName("gianni2");
        Game gameToSave = reception.createGame(lobby);
        PersistenceFactory2.save(gameToSave);

        //Create a new gameSaveFile and set the timeStamp to NOW-(gameSaveExpirationTimeMinutes*2) time so it will be expired
        this.createMockGameSaves(gameToSave, LocalDateTime.now().minusMinutes(Configs.gameSaveExpirationTimeMinutes*2));

        List<File> gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        assert gameSaves.stream().filter(file -> file.getName().contains("loadGameExpiredTest")).count() == 2;

        Reception reception1 = new Reception();
        Game gameLoadedFromSave = reception1.getGameByName("loadGameExpiredTest");
        System.out.println(gameLoadedFromSave);
        gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        //The load method should have deleted the expired saves(s)
        Assertions.assertEquals(1, gameSaves.stream().filter(file -> file.getName().contains("loadGameExpiredTest")).count());
    }

    @Test
    void loadWithNonDeleted() throws IOException {
        //create a game and save it
        Reception reception = new Reception();
        Lobby lobby = new Lobby(3, "gianni", "loadGameNotDeletedTest");
        lobby.addUserName("gianni1");
        lobby.addUserName("gianni2");
        Game gameToSave = reception.createGame(lobby);
        PersistenceFactory2.save(gameToSave);

        //Create a new gameSaveFile and set the timeStamp to NOW-(gameSaveExpirationTimeMinutes/2) time, so it will be NOT expired
        this.createMockGameSaves(gameToSave, LocalDateTime.now().minusMinutes(Configs.gameSaveExpirationTimeMinutes/2));

        List<File> gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        assert gameSaves.stream().filter(file -> file.getName().contains("loadGameNotDeletedTest")).count() == 2;

        Reception reception1 = new Reception();
        gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        //The load method should have deleted the oldest saves(s)
        Assertions.assertEquals(1, gameSaves.stream().filter(file -> file.getName().contains("loadGameNotDeletedTest")).count());
    }

    private void createMockGameSaves(Game gameToSave, LocalDateTime fakeLocalTimeDate) throws IOException {
        DateTimeFormatter windowSucks = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String _bin = ".bin";
        String dateGameNameSeparator = "--";
        File expiredSave = new File(OSRelated.gameDataFolderPath + fakeLocalTimeDate.format(windowSucks) + dateGameNameSeparator + gameToSave.getName() + _bin);
        ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(expiredSave));
        outStream.writeObject(gameToSave);
        outStream.close();
    }
}
