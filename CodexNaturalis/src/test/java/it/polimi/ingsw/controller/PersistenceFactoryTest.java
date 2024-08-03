package it.polimi.ingsw.controller;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.utils.OSRelated;
import it.polimi.ingsw.controller.persistence.PersistenceFactory;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import org.junit.jupiter.api.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PersistenceFactoryTest {
    private final CardTable cardTable = new CardTable(Configs.CardResourcesFolderPath, Configs.CardJSONFileName, OSRelated.cardFolderDataPath);
    private final PersistenceFactory persistenceFactory = new PersistenceFactory(OSRelated.gameDataFolderPath);
    private final int delaySeconds = 50;

    @BeforeEach
    void setUp() { //emulate the serverStartUP
        OSRelated.checkOrCreateDataFolderServer();
        persistenceFactory.eraseAllSaves();
    }

    @BeforeEach
    void delay(){
//        try{
//            Thread.sleep(delaySeconds);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @AfterEach
    void tearDown() {
        System.out.println("---------Test finished---------");
    }

    @Test
    void saveTest(){
        Game gameToSave = getGame("saveGameTest");
        persistenceFactory.save(gameToSave);

        try {
            Thread.sleep(delaySeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<File> gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        System.out.println(gameSaves);
        Assertions.assertEquals(1, gameSaves.stream().filter(file -> file.getName().contains("saveGameTest")).count());
    }

    private Game getGame(String name) {
        Deck<ObjectiveCard> objectiveCardDeck = new Deck<>(0, cardTable.getCardLookUpObjective().getQueue());
        Deck<ResourceCard> resourceCardDeck = new Deck<>(2, cardTable.getCardLookUpResourceCard().getQueue());
        Deck<GoldCard> goldCardDeck = new Deck<>(2, cardTable.getCardLookUpGoldCard().getQueue());
        Deck<StartCard> startingCardDeck = new Deck<>(0, cardTable.getCardLookUpStartCard().getQueue());
        Lobby lobby = new Lobby(3, name, 0);

        return new Game(lobby, objectiveCardDeck, resourceCardDeck, goldCardDeck, startingCardDeck);
    }

    @Test
    void load() {
        Game gameToSave = getGame("loadGameTest");
        persistenceFactory.save(gameToSave);

        Future<HashSet<Game>> loadedGamesFuture = persistenceFactory.load();
        HashSet<Game> loadedGames = new HashSet<>();
        try {
            loadedGames = loadedGamesFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Game gameLoadedFromSave = loadedGames.stream().filter(game -> game.getName().equals("loadGameTest")).findFirst().orElse(null);
        assert gameLoadedFromSave != null;
        assert Objects.equals(gameLoadedFromSave.getName(), "loadGameTest");
    }

    @Test
    void loadWithExpired() throws IOException {
        Game gameToSave = getGame("loadGameExpiredTest");
        persistenceFactory.save(gameToSave);

        //Create a new gameSaveFile and set the timeStamp to NOW-(gameSaveExpirationTimeMinutes*2) time so it will be expired
        this.createMockGameSaves(gameToSave, LocalDateTime.now().minusMinutes(Configs.gameSaveExpirationTimeMinutes*2));

        List<File> gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        assert gameSaves.stream().filter(file -> file.getName().contains("loadGameExpiredTest")).count() == 2;

        Future<HashSet<Game>> loadedGamesFuture = persistenceFactory.load();
        HashSet<Game> loadedGames = new HashSet<>();
        try {
            loadedGames = loadedGamesFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        System.out.println(gameSaves);
        //The load method should have deleted the expired saves(s)
        Assertions.assertEquals(1, gameSaves.stream().filter(file -> file.getName().contains("loadGameExpiredTest")).count());
    }

    @Test
    void loadWithNonDeleted() throws IOException {


        Game gameToSave = getGame("loadGameNotDeletedTest");
        persistenceFactory.save(gameToSave);

        try {
            Thread.sleep(delaySeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Create a new gameSaveFile and set the timeStamp to NOW-(gameSaveExpirationTimeMinutes/2) time, so it will be NOT expired
        this.createMockGameSaves(gameToSave, LocalDateTime.now().minusMinutes(Configs.gameSaveExpirationTimeMinutes/2));

        List<File> gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        assert gameSaves.stream().filter(file -> file.getName().contains("loadGameNotDeletedTest")).count() == 2;

        Future<HashSet<Game>> loadedGamesFuture = persistenceFactory.load();
        HashSet<Game> loadedGames = new HashSet<>();
        try {
            loadedGames = loadedGamesFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        assert loadedGames.stream().anyMatch(game -> game.getName().equals("loadGameNotDeletedTest"));

        gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        //The load method should have deleted the oldest saves(s)assertEquals
        Assertions.assertEquals(1, gameSaves.stream().filter(file -> file.getName().contains("loadGameNotDeletedTest")).count());
    }

    private void createMockGameSaves(Game gameToSave, LocalDateTime fakeLocalTimeDate) throws IOException {
        DateTimeFormatter windowSucks = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String _ser = ".ser";
        String dateGameNameSeparator = "--";
        File expiredSave = new File(OSRelated.gameDataFolderPath + fakeLocalTimeDate.format(windowSucks) + dateGameNameSeparator + gameToSave.getName() + _ser);
        do {
            FileOutputStream fileOutputStream = new FileOutputStream(expiredSave);
            ObjectOutputStream outStream = new ObjectOutputStream(fileOutputStream);
            outStream.writeObject(gameToSave);
            outStream.flush();
            outStream.close();
            fileOutputStream.close();

            try {
                Thread.sleep(delaySeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while(expiredSave.createNewFile());

    }

    @Test
    void eraseAllSaves() {
        int numberOfSavesPreDelete = 10;

        for(int i = 0; i<numberOfSavesPreDelete; i++) {
            Game gameToSave = getGame(Integer.toString(i));
            persistenceFactory.save(gameToSave);

            try {
                Thread.sleep(delaySeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<File> gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        System.out.println(gameSaves);

        persistenceFactory.eraseAllSaves();

        try {
            Thread.sleep(delaySeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

        gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        System.out.println(gameSaves);
        Assertions.assertEquals(0, gameSaves.size());
    }
}
