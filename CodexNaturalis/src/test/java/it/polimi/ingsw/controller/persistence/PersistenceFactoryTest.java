package it.polimi.ingsw.controller.persistence;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.OSRelated;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.LobbyGameListController;
import it.polimi.ingsw.controller.PublicController.PublicGameController;
import it.polimi.ingsw.controller.PublicController.PublicLobbyGameListController;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.tableReleted.Deck;
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

public class PersistenceFactoryTest {
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
        CardTable cardTable = new CardTable(Configs.CardResourcesFolderPath, Configs.CardJSONFileName, OSRelated.cardFolderDataPath);
        Deck<ObjectiveCard> objectiveCardDeck = new Deck<>(0,cardTable.getCardLookUpObjective().getQueue());
        Deck<ResourceCard> resourceCardDeck = new Deck<>(2, cardTable.getCardLookUpResourceCard().getQueue());
        Deck<GoldCard> goldCardDeck = new Deck<>(2, cardTable.getCardLookUpGoldCard().getQueue());
        Deck<StartCard> startingCardDeck = new Deck<>(0, cardTable.getCardLookUpStartCard().getQueue());
        Lobby lobby = new Lobby(3, "saveGameTest");

        Game gameToSave = new Game(lobby, objectiveCardDeck, resourceCardDeck, goldCardDeck, startingCardDeck);
        PersistenceFactory.save(gameToSave);

        List<File> gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        Assertions.assertEquals(1, gameSaves.stream().filter(file -> file.getName().contains("saveGameTest")).count());
    }

    @Test
    void load() {
        CardTable cardTable = new CardTable(Configs.CardResourcesFolderPath, Configs.CardJSONFileName, OSRelated.cardFolderDataPath);
        Deck<ObjectiveCard> objectiveCardDeck = new Deck<>(0,cardTable.getCardLookUpObjective().getQueue());
        Deck<ResourceCard> resourceCardDeck = new Deck<>(2, cardTable.getCardLookUpResourceCard().getQueue());
        Deck<GoldCard> goldCardDeck = new Deck<>(2, cardTable.getCardLookUpGoldCard().getQueue());
        Deck<StartCard> startingCardDeck = new Deck<>(0, cardTable.getCardLookUpStartCard().getQueue());
        Lobby lobby = new Lobby(3, "loadGameTest");

        Game gameToSave = new Game(lobby, objectiveCardDeck, resourceCardDeck, goldCardDeck, startingCardDeck);
        PersistenceFactory.save(gameToSave);

        LobbyGameListController realLobbyGameListController = new LobbyGameListController();
        PublicLobbyGameListController publicController = new PublicLobbyGameListController(realLobbyGameListController);
        GameController gameController = publicController.getGameMap().get("loadGameTest");
        Game gameLoadedFromSave = new PublicGameController(gameController).getGame();

        assert Objects.equals(gameLoadedFromSave.getName(), "loadGameTest");
    }

    @Test
    void loadWithExpired() throws IOException {
        //create a game and save it
        CardTable cardTable = new CardTable(Configs.CardResourcesFolderPath, Configs.CardJSONFileName, OSRelated.cardFolderDataPath);
        Deck<ObjectiveCard> objectiveCardDeck = new Deck<>(0,cardTable.getCardLookUpObjective().getQueue());
        Deck<ResourceCard> resourceCardDeck = new Deck<>(2, cardTable.getCardLookUpResourceCard().getQueue());
        Deck<GoldCard> goldCardDeck = new Deck<>(2, cardTable.getCardLookUpGoldCard().getQueue());
        Deck<StartCard> startingCardDeck = new Deck<>(0, cardTable.getCardLookUpStartCard().getQueue());
        Lobby lobby = new Lobby(3, "loadGameExpiredTest");

        Game gameToSave = new Game(lobby, objectiveCardDeck, resourceCardDeck, goldCardDeck, startingCardDeck);
        PersistenceFactory.save(gameToSave);

        //Create a new gameSaveFile and set the timeStamp to NOW-(gameSaveExpirationTimeMinutes*2) time so it will be expired
        this.createMockGameSaves(gameToSave, LocalDateTime.now().minusMinutes(Configs.gameSaveExpirationTimeMinutes*2));

        List<File> gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        assert gameSaves.stream().filter(file -> file.getName().contains("loadGameExpiredTest")).count() == 2;

        LobbyGameListController realLobbyGameListController = new LobbyGameListController();
        PublicLobbyGameListController publicController = new PublicLobbyGameListController(realLobbyGameListController);
        GameController gameController = publicController.getGameMap().get("loadGameTest");
        Game gameLoadedFromSave = new PublicGameController(gameController).getGame();

        System.out.println(gameLoadedFromSave);
        gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        //The load method should have deleted the expired saves(s)
        Assertions.assertEquals(1, gameSaves.stream().filter(file -> file.getName().contains("loadGameExpiredTest")).count());
    }

    @Test
    void loadWithNonDeleted() throws IOException {
        //create a game and save it
        CardTable cardTable = new CardTable(Configs.CardResourcesFolderPath, Configs.CardJSONFileName, OSRelated.cardFolderDataPath);
        Deck<ObjectiveCard> objectiveCardDeck = new Deck<>(0,cardTable.getCardLookUpObjective().getQueue());
        Deck<ResourceCard> resourceCardDeck = new Deck<>(2, cardTable.getCardLookUpResourceCard().getQueue());
        Deck<GoldCard> goldCardDeck = new Deck<>(2, cardTable.getCardLookUpGoldCard().getQueue());
        Deck<StartCard> startingCardDeck = new Deck<>(0, cardTable.getCardLookUpStartCard().getQueue());
        Lobby lobby = new Lobby(3, "loadGameNotDeletedTest");

        Game gameToSave = new Game(lobby, objectiveCardDeck, resourceCardDeck, goldCardDeck, startingCardDeck);
        PersistenceFactory.save(gameToSave);

        //Create a new gameSaveFile and set the timeStamp to NOW-(gameSaveExpirationTimeMinutes/2) time, so it will be NOT expired
        this.createMockGameSaves(gameToSave, LocalDateTime.now().minusMinutes(Configs.gameSaveExpirationTimeMinutes/2));

        List<File> gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        assert gameSaves.stream().filter(file -> file.getName().contains("loadGameNotDeletedTest")).count() == 2;

        PersistenceFactory.load();
        gameSaves = Arrays.asList(Objects.requireNonNull(new File(OSRelated.gameDataFolderPath).listFiles()));
        //The load method should have deleted the oldest saves(s)assertEquals
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
