package it.polimi.ingsw.controller;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.utils.OSRelated;
import it.polimi.ingsw.controller.PublicController.PublicGameController;
import it.polimi.ingsw.controller.PublicController.PublicLobbyGameListController;
import it.polimi.ingsw.controller.persistence.PersistenceFactory;
import it.polimi.ingsw.lightModel.ViewTest;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.model.playerReleted.Player;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.GameState;
import it.polimi.ingsw.view.ViewState;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.HashMap;

public class ResilienceTimerTest {
    private LobbyGameListsController realLobbyGameListController;
    private PublicLobbyGameListController lobbyGameListController;
    private static int shorterTimerDurationSeconds = 1;
    private static int originalTimerDuration;
    private final PersistenceFactory persistenceFactory = new PersistenceFactory(OSRelated.gameDataFolderPath);
    private static int oldDelay;

    @BeforeAll
    public static void setUpAll(){
        OSRelated.checkOrCreateDataFolderServer(); //Create the dataFolder if necessary. Normally this is done in the Server class
        oldDelay =  Configs.delayBeforeLoadingGameSaves;
        Configs.delayBeforeLoadingGameSaves = 30;
    }

    @BeforeEach
    public void setUp(){
        persistenceFactory.eraseAllSaves();

        realLobbyGameListController = new LobbyGameListsController();
        lobbyGameListController = new PublicLobbyGameListController(realLobbyGameListController);
    }

    @BeforeAll
    public static void shortenDuration(){
        originalTimerDuration = Configs.lastInGameTimerSeconds;
        Configs.lastInGameTimerSeconds = shorterTimerDurationSeconds;
    }

    @AfterAll
    public static void resetDuration(){
        Configs.lastInGameTimerSeconds = originalTimerDuration;
    }

    @AfterAll
    public static void resetPersistence(){
        File dataFolder = new File(Configs.gameSaveFolderName);
        File[] saves = dataFolder.listFiles();
        if (saves != null) {
            for (File gameSave : saves) {
                gameSave.delete();
            }
        }
        Configs.delayBeforeLoadingGameSaves = oldDelay;
    }

    @Test
    void waitingTimerWin(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";

        String lobbyName1 = "waitingTimerWin";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.joinLobby(lobbyName1);

        LightCard startCard1 = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard1, CardFace.FRONT);

        controller1.place(startPlacement1);
        controller2.leave();
        try {
            Thread.sleep(Configs.lastInGameTimerSeconds * 1000L + 1000L);
        }catch (Exception ignored){}

        assert view1.state.equals(ViewState.GAME_ENDING);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        controller1.leave();
        //the save has been deleted
        assert !lobbyGameListController.getGameMap().containsKey(lobbyName1);
    }

    @Test
    void startCardTimerWin(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";

        String lobbyName1 = "startCardTimerWin";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.joinLobby(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();

        controller2.leave();
        assert !game.getState().equals(GameState.END_GAME);

        try {
            Thread.sleep(Configs.lastInGameTimerSeconds * 1000L + 1000L);
        }catch (Exception ignored){}

        assert game.getState().equals(GameState.END_GAME);
        assert view1.state.equals(ViewState.GAME_ENDING);

        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        //the save has been deleted
        controller1.leave();
        assert !lobbyGameListController.getGameMap().containsKey(lobbyName1);
    }
    @Test
    void pawnChoiceTimerWin(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";

        String lobbyName1 = "pawnChoiceTimerWin";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.joinLobby(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();

        LightCard startCard1 = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard1, CardFace.FRONT);
        LightCard startCard2 = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard2, CardFace.FRONT);

        controller1.place(startPlacement1);
        controller2.place(startPlacement2);

        assert game.getState().equals(GameState.CHOOSE_PAWN);
        controller2.leave();
        try {
            Thread.sleep(Configs.lastInGameTimerSeconds * 1000L + 1000L);
        }catch (Exception ignored){}

        assert view1.state.equals(ViewState.GAME_ENDING);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        //the save has been deleted
        controller1.leave();
        assert !lobbyGameListController.getGameMap().containsKey(lobbyName1);
    }
    @Test
    void secretObjectiveTimerWin(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";

        String lobbyName1 = "test1";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.joinLobby(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();

        LightCard startCard1 = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard1, CardFace.FRONT);
        LightCard startCard2 = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard2, CardFace.FRONT);

        controller1.place(startPlacement1);
        controller2.place(startPlacement2);

        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.RED);

        assert game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);
        controller2.leave();
        try {
            Thread.sleep(Configs.lastInGameTimerSeconds * 1000L + 1000L);
        }catch (Exception ignored){}

        assert view1.state.equals(ViewState.GAME_ENDING);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        //the save has been deleted
        controller1.leave();
        assert !lobbyGameListController.getGameMap().containsKey(lobbyName1);
    }
    @Test
    void actualGameTimerWin(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "duck";

        String lobbyName1 = "test1";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);

        HashMap<String, Controller> controllerMap = new HashMap<>();
        controllerMap.put(view1.name, controller1);
        controllerMap.put(view2.name, controller2);
        controllerMap.put(view3.name, controller3);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller3.login(view3.name);
        controller1.createLobby(lobbyName1, 3);
        controller2.joinLobby(lobbyName1);
        controller3.joinLobby(lobbyName1);

        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();

        assert game.getPlayersList().stream().map(Player::getNickname).toList().contains(view1.name);
        assert game.getPlayersList().stream().map(Player::getNickname).toList().contains(view2.name);
        assert game.getPlayersList().stream().map(Player::getNickname).toList().contains(view3.name);

        LightCard startCard1 = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard1, CardFace.FRONT);
        LightCard startCard2 = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard2, CardFace.FRONT);
        LightCard startCard3 = view3.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement3 = new LightPlacement(new Position(0,0), startCard3, CardFace.FRONT);

        controller1.place(startPlacement1);
        controller2.place(startPlacement2);
        controller3.place(startPlacement3);

        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.RED);
        controller3.choosePawn(PawnColors.YELLOW);

        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];
        LightCard secretObjective3 = view3.lightGame.getHand().getSecretObjectiveOptions()[0];

        HashMap<String, ViewTest> viewMap = new HashMap<>();
        viewMap.put(view1.name, view1);
        viewMap.put(view2.name, view2);
        viewMap.put(view3.name, view3);

        controller1.chooseSecretObjective(secretObjective1);
        controller2.chooseSecretObjective(secretObjective2);
        controller3.chooseSecretObjective(secretObjective3);

        assert game.getState().equals(GameState.ACTUAL_GAME);
        Player firstPlayer = game.getPlayerFromNick(game.getCurrentPlayer().getNickname());
        Player secondPlayer = game.getPlayersList().get(1);
        Player thirdPlayer = game.getPlayersList().get(2);

        Controller firstPlayerController = controllerMap.get(firstPlayer.getNickname());
        Controller secondPlayerController = controllerMap.get(secondPlayer.getNickname());
        Controller thirdPlayerController = controllerMap.get(thirdPlayer.getNickname());
        firstPlayerController.leave();
        assert game.getCurrentPlayer().equals(secondPlayer);
        assert game.getState().equals(GameState.ACTUAL_GAME);

        assert viewMap.get(secondPlayer.getNickname()).state.equals(ViewState.PLACE_CARD);
        secondPlayerController.leave();

        assert !game.getState().equals(GameState.END_GAME);
        assert game.getCurrentPlayer().equals(thirdPlayer);
        try {
            Thread.sleep(Configs.lastInGameTimerSeconds * 1000L + 1000L);
        }catch (Exception ignored){}

        assert viewMap.get(game.getCurrentPlayer().getNickname()).state.equals(ViewState.GAME_ENDING);

        assert game.getState().equals(GameState.END_GAME);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        //the save has been deleted
        thirdPlayerController.leave();
        assert !lobbyGameListController.getGameMap().containsKey(lobbyName1);
    }

    @Test
    void joinGameTimerReset(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";

        String lobbyName1 = "test1";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.joinLobby(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();

        LightCard startCard1 = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard1, CardFace.FRONT);
        LightCard startCard2 = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard2, CardFace.FRONT);

        controller1.place(startPlacement1);
        controller2.place(startPlacement2);

        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.RED);

        assert game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);
        controller1.leave();
        assert !game.getState().equals(GameState.END_GAME);
        controller1.login(view1.name);
        try {
            Thread.sleep(Configs.lastInGameTimerSeconds * 1000L + 1000L);
        }catch (Exception ignored){}

        assert !game.getState().equals(GameState.END_GAME);
        assert game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);
    }
    @Test
    void allPlayerLeaveReset(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";

        String lobbyName1 = "test1";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.joinLobby(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();

        LightCard startCard1 = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard1, CardFace.FRONT);
        LightCard startCard2 = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard2, CardFace.FRONT);

        controller1.place(startPlacement1);
        controller2.place(startPlacement2);

        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.RED);

        assert game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);
        controller1.leave();
        assert !game.getState().equals(GameState.END_GAME);
        controller2.leave();
        try {
            Thread.sleep(Configs.lastInGameTimerSeconds * 1000L + 1000L);
        }catch (Exception ignored){}

        assert !game.getState().equals(GameState.END_GAME);
        assert game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);
    }
}
