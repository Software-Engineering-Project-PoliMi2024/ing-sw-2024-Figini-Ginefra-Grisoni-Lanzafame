package it.polimi.ingsw.controller;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller.PublicController.PublicController;
import it.polimi.ingsw.utils.OSRelated;
import it.polimi.ingsw.controller.PublicController.PublicGameController;
import it.polimi.ingsw.controller.PublicController.PublicLobbyController;
import it.polimi.ingsw.controller.PublicController.PublicLobbyGameListController;
import it.polimi.ingsw.controller.persistence.PersistenceFactory;
import it.polimi.ingsw.lightModel.Heavifier;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.ViewTest;
import it.polimi.ingsw.lightModel.lightPlayerRelated.*;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.cardReleted.utilityEnums.*;
import it.polimi.ingsw.model.playerReleted.*;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.GameState;
import it.polimi.ingsw.view.ViewState;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class ResilienceTest {
    private LobbyGameListsController realLobbyGameListController;
    private PublicLobbyGameListController lobbyGameListController;
    private final PersistenceFactory persistenceFactory = new PersistenceFactory(OSRelated.gameDataFolderPath);
    private static int oldDelay;

    @BeforeAll
    public static void setUpAll(){
        OSRelated.checkOrCreateDataFolderServer(); //Create the dataFolder if necessary. Normally this is done in the Server class
        oldDelay =  Configs.delayBeforeLoadingGameSaves;
        Configs.delayBeforeLoadingGameSaves = 30;
    }

    @BeforeEach
    public void setUp() throws ExecutionException, InterruptedException {
        realLobbyGameListController = new LobbyGameListsController();
        lobbyGameListController = new PublicLobbyGameListController(realLobbyGameListController);

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
    void leaveFromLobbyList(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";

        String lobbyName1 = "leaveFromLobbyList";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 2);

        assert lobbyGameListController.getViewMap().containsKey(view2.name);
        assert view2.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().contains(lobbyName1);

        controller2.leave();

        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert view2.lightLobbyList.getLobbies().isEmpty();
    }

    @Test
    void leaveFromLobby(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";

        String lobbyName1 = "leaveFromLobby";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 3);
        controller2.joinLobby(lobbyName1);

        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert view2.lightLobbyList.getLobbies().isEmpty();
        assert view2.lightLobby.name().equals(lobbyName1);

        controller2.leave();

        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert view2.lightLobby.name().isEmpty();
        PublicLobbyController lobbyController = new PublicLobbyController(lobbyGameListController.getLobbyMap().get(lobbyName1));
        assert lobbyController.getViewMap().containsKey(view1.name);
        assert !lobbyController.getViewMap().containsKey(view2.name);
        assert lobbyController.getGameReceiverMap().containsKey(view1.name);
        assert !lobbyController.getGameReceiverMap().containsKey(view2.name);
        assert lobbyController.getLobby().getLobbyPlayerList().contains(view1.name);
        assert !lobbyController.getLobby().getLobbyPlayerList().contains(view2.name);
        assert lobbyController.getLobby().getLobbyName().equals(lobbyName1);
        assert lobbyController.getLobby().getNumberOfMaxPlayer() == 3;
    }

    @Test
    void leaveAndRejoinGameChooseStartCardPhase() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "duck";
        view4.name = "mickey";
        view5.name = "minnie";

        String lobbyName1 = "leaveAndRejoinGameChooseStartCardPhase1";
        String lobbyName2 = "leaveAndRejoinGameChooseStartCardPhase2";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);
        Controller controller4 = new Controller(realLobbyGameListController, view4);
        Controller controller5 = new Controller(realLobbyGameListController, view5);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller3.login(view3.name);
        controller4.login(view4.name);
        controller5.login(view5.name);
        controller1.createLobby(lobbyName1, 4);
        controller2.joinLobby(lobbyName1);
        controller3.joinLobby(lobbyName1);
        controller4.joinLobby(lobbyName1);
        controller5.createLobby(lobbyName2, 2);

        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert !lobbyGameListController.getViewMap().containsKey(view3.name);
        assert !lobbyGameListController.getViewMap().containsKey(view4.name);

        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();
        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);
        Player player3 = game.getPlayerFromNick(view3.name);
        Player player4 = game.getPlayerFromNick(view4.name);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());

        assert gameController.getViewMap().containsKey(view2.name);
        assert game.getName().equals(lobbyName1);
        assert game.getPlayersList().stream().map(Player::getNickname).toList().contains(view2.name);

        controller2.leave();

        assert !gameController.getViewMap().containsKey(view2.name);

        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        assert !gameController.getViewMap().containsKey(view2.name);
        assert game.getName().equals(lobbyName1);
        assert game.getPlayersList().stream().map(Player::getNickname).toList().contains(view2.name);

        assert view2.lightGame.getCodexMap().isEmpty();
        assert view2.lightGame.getLightGameParty().getPlayerActiveMap().isEmpty();
        Assertions.assertNull(view2.lightGame.getLightGameParty().getCurrentPlayer());
        assert view2.lightLobbyList.getLobbies().isEmpty();

        assert !view1.lightGame.getLightGameParty().getPlayerActiveMap().get(view2.name);
        assert !view3.lightGame.getLightGameParty().getPlayerActiveMap().get(view2.name);

        view2.state = null;
        controller2.login(view2.name);
        assert view2.state.equals(ViewState.CHOOSE_START_CARD);

        assert view1.lightGame.getLightGameParty().getPlayerActiveMap().get(view2.name);
        assert view3.lightGame.getLightGameParty().getPlayerActiveMap().get(view2.name);

        assert view2.lightGame.getLightGameParty().getGameName().equals(lobbyName1);

        assert player2.getState().equals(PlayerState.CHOOSE_START_CARD);

        assert game.getState().equals(GameState.CHOOSE_START_CARD);
        assert !game.getState().equals(GameState.CHOOSE_PAWN);
        assert !game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);

        StartCard startCard2 = player2.getUserHand().getStartCard();
        assert Arrays.stream(view2.lightGame.getHand().getCards()).toList().contains(Lightifier.lightifyToCard(startCard2));

        LightPlacement lightPlacement1 = new LightPlacement(new Position(0,0), Lightifier.lightifyToCard(player1.getUserHand().getStartCard()), CardFace.FRONT);
        controller1.place(lightPlacement1);

        LightPlacement lightPlacement4 = new LightPlacement(new Position(0,0), Lightifier.lightifyToCard(player4.getUserHand().getStartCard()), CardFace.FRONT);
        controller4.place(lightPlacement4);

        assert !lobbyGameListController.getViewMap().containsKey(view4.name);
        controller2.leave();
        controller4.leave();

        assert game.getState().equals(GameState.CHOOSE_START_CARD);

        LightPlacement lightPlacement3 = new LightPlacement(new Position(0,0), Lightifier.lightifyToCard(player3.getUserHand().getStartCard()), CardFace.FRONT);
        controller3.place(lightPlacement3);

        assert !game.getState().equals(GameState.CHOOSE_START_CARD);
        assert game.getState().equals(GameState.CHOOSE_PAWN);

        assert !game.getPlayersList().contains(player2);
        assert game.getPlayersList().contains(player4);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());
        System.out.println(gameController.gameController.getGamePlayers());
        controller2.login(view2.name);
        controller4.login(view4.name);

        assert game.getState().equals(GameState.CHOOSE_PAWN);
        assert !game.getPlayersList().contains(player2);
        assert game.getPlayersList().contains(player4);

        assert view2.state.equals(ViewState.JOIN_LOBBY);
        assert view4.state.equals(ViewState.CHOOSE_PAWN);

        assert !view1.lightGame.getLightGameParty().getPlayerActiveMap().containsKey(view2.name);
        assert !view3.lightGame.getLightGameParty().getPlayerActiveMap().containsKey(view2.name);
        assert !view4.lightGame.getLightGameParty().getPlayerActiveMap().containsKey(view2.name);
    }

    @Test
    void leaveAndRejoinGameChooseStartCardPhaseTheLeaverTriggersNextPhase() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "duck";
        view4.name = "mickey";
        view5.name = "minnie";

        String lobbyName1 = "leaveAndRejoinGameChooseStartCardPhaseTheLeaverTriggersNextPhase1";
        String lobbyName2 = "leaveAndRejoinGameChooseStartCardPhaseTheLeaverTriggersNextPhase2";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);
        Controller controller4 = new Controller(realLobbyGameListController, view4);
        Controller controller5 = new Controller(realLobbyGameListController, view5);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller3.login(view3.name);
        controller4.login(view4.name);
        controller5.login(view5.name);
        controller1.createLobby(lobbyName1, 4);
        controller2.joinLobby(lobbyName1);
        controller3.joinLobby(lobbyName1);
        controller4.joinLobby(lobbyName1);
        controller5.createLobby(lobbyName2, 2);

        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert !lobbyGameListController.getViewMap().containsKey(view3.name);
        assert !lobbyGameListController.getViewMap().containsKey(view4.name);

        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();
        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);
        Player player3 = game.getPlayerFromNick(view3.name);
        Player player4 = game.getPlayerFromNick(view4.name);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());

        assert gameController.getViewMap().containsKey(view2.name);
        assert game.getName().equals(lobbyName1);
        assert game.getPlayersList().stream().map(Player::getNickname).toList().contains(view2.name);

        assert player2.getState().equals(PlayerState.CHOOSE_START_CARD);

        assert game.getState().equals(GameState.CHOOSE_START_CARD);
        assert !game.getState().equals(GameState.CHOOSE_PAWN);
        assert !game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);

        LightPlacement lightPlacement1 = new LightPlacement(new Position(0,0), Lightifier.lightifyToCard(player1.getUserHand().getStartCard()), CardFace.FRONT);
        controller1.place(lightPlacement1);

        LightPlacement lightPlacement4 = new LightPlacement(new Position(0,0), Lightifier.lightifyToCard(player4.getUserHand().getStartCard()), CardFace.FRONT);
        controller4.place(lightPlacement4);

        assert !lobbyGameListController.getViewMap().containsKey(view4.name);
        controller4.leave();
        assert game.getState().equals(GameState.CHOOSE_START_CARD);

        controller3.leave();
        controller2.leave();
        assert !game.getState().equals(GameState.CHOOSE_START_CARD);
        assert game.getState().equals(GameState.CHOOSE_PAWN);

        assert !game.getPlayersList().contains(player2);
        assert !game.getPlayersList().contains(player3);
        assert game.getPlayersList().contains(player4);

        controller2.login(view2.name);
        controller3.login(view3.name);
        controller4.login(view4.name);

        assert view2.state.equals(ViewState.JOIN_LOBBY);
        assert view3.state.equals(ViewState.JOIN_LOBBY);
        assert view4.state.equals(ViewState.CHOOSE_PAWN);

        assert !view1.lightGame.getLightGameParty().getPlayerActiveMap().containsKey(view2.name);
        assert !view3.lightGame.getLightGameParty().getPlayerActiveMap().containsKey(view2.name);
        assert !view4.lightGame.getLightGameParty().getPlayerActiveMap().containsKey(view2.name);
    }

    @Test
    void leaveAndRejoinGameChoosePawn(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "duck";
        view4.name = "mickey";
        view5.name = "minnie";

        String lobbyName1 = "leaveAndRejoinGameChoosePawn1";
        String lobbyName2 = "leaveAndRejoinGameChoosePawn2";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);
        Controller controller4 = new Controller(realLobbyGameListController, view4);
        Controller controller5 = new Controller(realLobbyGameListController, view5);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller3.login(view3.name);
        controller4.login(view4.name);
        controller5.login(view5.name);
        controller1.createLobby(lobbyName1, 4);
        controller2.joinLobby(lobbyName1);
        controller3.joinLobby(lobbyName1);
        controller4.joinLobby(lobbyName1);
        controller5.createLobby(lobbyName2, 2);

        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert !lobbyGameListController.getViewMap().containsKey(view3.name);
        assert !lobbyGameListController.getViewMap().containsKey(view4.name);

        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();
        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);
        Player player3 = game.getPlayerFromNick(view3.name);
        Player player4 = game.getPlayerFromNick(view4.name);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());

        assert gameController.getViewMap().containsKey(view2.name);
        assert game.getName().equals(lobbyName1);
        assert game.getPlayersList().stream().map(Player::getNickname).toList().contains(view2.name);

        placeStartCard(player1, controller1);
        placeStartCard(player2, controller2);
        placeStartCard(player3, controller3);
        placeStartCard(player4, controller4);

        pawnChoicesSetupCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.leave();
        controller2.choosePawn(PawnColors.BLUE);

        List<PawnColors> nonChosenColors = Arrays.stream(PawnColors.values()).filter(color->!color.equals(PawnColors.BLUE)).toList();
        assert game.getPawnChoices().containsAll(nonChosenColors);
        assert !game.getPawnChoices().contains(PawnColors.BLUE);
        assert player2.getPawnColor().equals(PawnColors.BLUE);

        controller1.login(view1.name);
        System.out.println(view1.state);
        assert view1.state.equals(ViewState.CHOOSE_PAWN);
        checkInitialization(view1, gameController);

        assert view1.lightGame.getLightGameParty().getPawnChoices().containsAll(nonChosenColors);
        assert !view1.lightGame.getLightGameParty().getPawnChoices().contains(PawnColors.BLUE);

        controller3.choosePawn(PawnColors.RED);

        controller1.leave();
        controller2.leave();

        controller4.choosePawn(PawnColors.GREEN);

        controller1.login(view1.name);
        controller2.login(view2.name);

        assert view1.state.equals(ViewState.JOIN_LOBBY);
        assert view2.state.equals(ViewState.SELECT_OBJECTIVE);

        assert view1.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().containsAll(lobbyGameListController.getLobbyMap().keySet());

        setupObjChoicesCorrectly(gameController, List.of(view2, view3, view4));
    }

    @Test
    void leaveAndRejoinGameChoosePawnTheLeaverTriggersNextPhase(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "duck";
        view4.name = "mickey";
        view5.name = "minnie";

        String lobbyName1 = "leaveAndRejoinGameChoosePawnTheLeaverTriggersNextPhase1";
        String lobbyName2 = "leaveAndRejoinGameChoosePawnTheLeaverTriggersNextPhase2";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);
        Controller controller4 = new Controller(realLobbyGameListController, view4);
        Controller controller5 = new Controller(realLobbyGameListController, view5);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller3.login(view3.name);
        controller4.login(view4.name);
        controller5.login(view5.name);
        controller1.createLobby(lobbyName1, 4);
        controller2.joinLobby(lobbyName1);
        controller3.joinLobby(lobbyName1);
        controller4.joinLobby(lobbyName1);
        controller5.createLobby(lobbyName2, 2);

        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert !lobbyGameListController.getViewMap().containsKey(view3.name);
        assert !lobbyGameListController.getViewMap().containsKey(view4.name);

        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();
        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);
        Player player3 = game.getPlayerFromNick(view3.name);
        Player player4 = game.getPlayerFromNick(view4.name);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());

        assert gameController.getViewMap().containsKey(view2.name);
        assert game.getName().equals(lobbyName1);
        assert game.getPlayersList().stream().map(Player::getNickname).toList().contains(view2.name);

        placeStartCard(player1, controller1);
        placeStartCard(player2, controller2);
        placeStartCard(player3, controller3);
        placeStartCard(player4, controller4);

        pawnChoicesSetupCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller3.choosePawn(PawnColors.RED);
        controller4.choosePawn(PawnColors.GREEN);

        controller4.leave();
        controller1.leave();
        assert game.getState().equals(GameState.CHOOSE_PAWN);

        controller2.leave();
        assert game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);
        assert game.getPlayersList().contains(player3);
        assert game.getPlayersList().contains(player4);
        assert !game.getPlayersList().contains(player1);
        assert !game.getPlayersList().contains(player2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller4.login(view4.name);

        assert view1.state.equals(ViewState.JOIN_LOBBY);
        assert view2.state.equals(ViewState.JOIN_LOBBY);
        assert view4.state.equals(ViewState.SELECT_OBJECTIVE);

        setupObjChoicesCorrectly(gameController, List.of(view3, view4));
    }

    @Test
    void leaveAndRejoinChooseSecretObj(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "duck";
        view4.name = "mickey";
        view5.name = "minnie";

        String lobbyName1 = "leaveAndRejoinChooseSecretObj1";
        String lobbyName2 = "leaveAndRejoinChooseSecretObj2";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);
        Controller controller4 = new Controller(realLobbyGameListController, view4);
        Controller controller5 = new Controller(realLobbyGameListController, view5);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller3.login(view3.name);
        controller4.login(view4.name);
        controller5.login(view5.name);
        controller1.createLobby(lobbyName1, 4);
        controller2.joinLobby(lobbyName1);
        controller3.joinLobby(lobbyName1);
        controller4.joinLobby(lobbyName1);
        controller5.createLobby(lobbyName2, 2);

        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert !lobbyGameListController.getViewMap().containsKey(view3.name);
        assert !lobbyGameListController.getViewMap().containsKey(view4.name);

        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();
        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);
        Player player3 = game.getPlayerFromNick(view3.name);
        Player player4 = game.getPlayerFromNick(view4.name);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());

        placeStartCard(player1, controller1);
        placeStartCard(player2, controller2);
        placeStartCard(player3, controller3);
        placeStartCard(player4, controller4);

        pawnChoicesSetupCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.YELLOW);
        controller3.choosePawn(PawnColors.RED);
        controller4.choosePawn(PawnColors.GREEN);

        setupObjChoicesCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.leave();
        LightCard obj2 = Lightifier.lightifyToCard(player2.getUserHand().getSecretObjectiveChoices().getFirst());
        controller2.chooseSecretObjective(obj2);
        controller2.leave();

        controller1.login(view1.name);
        checkInitialization(view1, gameController);
        controller2.login(view2.name);
        checkInitialization(view2, gameController);

        assert view1.state.equals(ViewState.SELECT_OBJECTIVE);
        assert view2.state.equals(ViewState.WAITING_STATE);

        setupObjChoicesCorrectly(gameController, List.of(view1));
        assert Arrays.stream(view2.lightGame.getHand().getSecretObjectiveOptions()).allMatch(Objects::isNull);
        assert view2.lightGame.getHand().getSecretObjective().equals(obj2);

        controller3.chooseSecretObjective(Lightifier.lightifyToCard(player3.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller1.leave();
        controller2.leave();
        controller4.chooseSecretObjective(Lightifier.lightifyToCard(player4.getUserHand().getSecretObjectiveChoices().getFirst()));

        game.getPlayersList().forEach(user->{
            System.out.println(user.getNickname());
            System.out.println(user.getUserHand().getSecretObjective());
            System.out.println(user.getUserHand().getSecretObjectiveChoices());
        });

        assert !game.getState().equals(GameState.CHOOSE_START_CARD);
        assert !game.getState().equals(GameState.CHOOSE_PAWN);
        assert !game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);

        controller1.login(view1.name);
        controller2.login(view2.name);

        assert view1.state.equals(ViewState.JOIN_LOBBY);
        setupActualGame(gameController, List.of(view2, view3, view4));
    }

    @Test
    void leaveAndRejoinChooseSecretObjTheLeaverTriggersNextPhase(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "duck";
        view4.name = "mickey";
        view5.name = "minnie";

        String lobbyName1 = "leaveAndRejoinChooseSecretObjTheLeaverTriggersNextPhase1";
        String lobbyName2 = "leaveAndRejoinChooseSecretObjTheLeaverTriggersNextPhase2";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);
        Controller controller4 = new Controller(realLobbyGameListController, view4);
        Controller controller5 = new Controller(realLobbyGameListController, view5);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller3.login(view3.name);
        controller4.login(view4.name);
        controller5.login(view5.name);
        controller1.createLobby(lobbyName1, 4);
        controller2.joinLobby(lobbyName1);
        controller3.joinLobby(lobbyName1);
        controller4.joinLobby(lobbyName1);
        controller5.createLobby(lobbyName2, 2);

        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert !lobbyGameListController.getViewMap().containsKey(view3.name);
        assert !lobbyGameListController.getViewMap().containsKey(view4.name);

        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();
        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);
        Player player3 = game.getPlayerFromNick(view3.name);
        Player player4 = game.getPlayerFromNick(view4.name);

        placeStartCard(player1, controller1);
        placeStartCard(player2, controller2);
        placeStartCard(player3, controller3);
        placeStartCard(player4, controller4);

        pawnChoicesSetupCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.YELLOW);
        controller3.choosePawn(PawnColors.RED);
        controller4.choosePawn(PawnColors.GREEN);

        setupObjChoicesCorrectly(gameController, List.of(view1, view2, view3, view4));

        LightCard obj2 = Lightifier.lightifyToCard(player2.getUserHand().getSecretObjectiveChoices().getFirst());
        controller2.chooseSecretObjective(obj2);
        controller3.chooseSecretObjective(Lightifier.lightifyToCard(player3.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller2.leave();
        controller4.leave();
        assert game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);
        controller1.leave();
        assert game.getState().equals(GameState.ACTUAL_GAME);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller4.login(view4.name);
        assert view1.state.equals(ViewState.JOIN_LOBBY);
        assert view2.state.equals(ViewState.IDLE) || view2.state.equals(ViewState.PLACE_CARD);
        assert view4.state.equals(ViewState.JOIN_LOBBY);
    }

    @Test
    void leaveAndRejoinActualGameIdle(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "duck";
        view4.name = "mickey";
        view5.name = "minnie";

        String lobbyName1 = "leaveAndRejoinActualGameIdle1";
        String lobbyName2 = "leaveAndRejoinActualGameIdle2";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);
        Controller controller4 = new Controller(realLobbyGameListController, view4);
        Controller controller5 = new Controller(realLobbyGameListController, view5);

        HashMap<String, Controller> controllerMap = new HashMap<>();
        controllerMap.put(view1.name, controller1);
        controllerMap.put(view2.name, controller2);
        controllerMap.put(view3.name, controller3);
        controllerMap.put(view4.name, controller4);

        HashMap<String, ViewTest> viewMap = new HashMap<>();
        viewMap.put(view1.name, view1);
        viewMap.put(view2.name, view2);
        viewMap.put(view3.name, view3);
        viewMap.put(view4.name, view4);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller3.login(view3.name);
        controller4.login(view4.name);
        controller5.login(view5.name);
        controller1.createLobby(lobbyName1, 4);
        controller2.joinLobby(lobbyName1);
        controller3.joinLobby(lobbyName1);
        controller4.joinLobby(lobbyName1);
        controller5.createLobby(lobbyName2, 2);

        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert !lobbyGameListController.getViewMap().containsKey(view3.name);
        assert !lobbyGameListController.getViewMap().containsKey(view4.name);

        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();
        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);
        Player player3 = game.getPlayerFromNick(view3.name);
        Player player4 = game.getPlayerFromNick(view4.name);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());

        placeStartCard(player1, controller1);
        placeStartCard(player2, controller2);
        placeStartCard(player3, controller3);
        placeStartCard(player4, controller4);

        pawnChoicesSetupCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.YELLOW);
        controller3.choosePawn(PawnColors.RED);
        controller4.choosePawn(PawnColors.GREEN);

        setupObjChoicesCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.chooseSecretObjective(Lightifier.lightifyToCard(player1.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller2.chooseSecretObjective(Lightifier.lightifyToCard(player2.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller3.chooseSecretObjective(Lightifier.lightifyToCard(player3.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller4.chooseSecretObjective(Lightifier.lightifyToCard(player4.getUserHand().getSecretObjectiveChoices().getFirst()));

        setupActualGame(gameController, List.of(view1, view2, view3, view4));

        Player firstPlayer = game.getPlayersList().getFirst();
        Player playerleaveed = game.getPlayersList().get(3);

        Controller controllerFirst = controllerMap.get(firstPlayer.getNickname());
        Controller controllerleaveed = controllerMap.get(playerleaveed.getNickname());

        ViewTest viewFirst = viewMap.get(firstPlayer.getNickname());
        ViewTest viewleaveed = viewMap.get(playerleaveed.getNickname());

        controllerleaveed.leave();

        controllerFirst.place(new LightPlacement(new Position(1,1), Lightifier.lightifyToCard(firstPlayer.getUserHand().getHand().stream().findFirst().get()), CardFace.BACK));

        controllerleaveed.login(playerleaveed.getNickname());
        checkInitialization(viewleaveed, gameController);
        checkGame(viewleaveed, gameController);
        for(ViewTest view : viewMap.values()){
            Assertions.assertTrue(view.lightGame.getLightGameParty().getPlayerActiveMap().get(viewleaveed.name));
        }

        controllerleaveed.leave();
        controllerFirst.draw(DrawableCard.RESOURCECARD, 0);

        controllerleaveed.login(playerleaveed.getNickname());
        checkInitialization(viewleaveed, gameController);
        checkGame(viewleaveed, gameController);
        for(ViewTest view : viewMap.values()){
            Assertions.assertTrue(view.lightGame.getLightGameParty().getPlayerActiveMap().get(viewleaveed.name));
        }
    }

    @Test
    void leaveAndRejoinActualGamePlaceCard(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "duck";
        view4.name = "mickey";
        view5.name = "minnie";

        String lobbyName1 = "leaveAndRejoinActualGamePlaceCard1";
        String lobbyName2 = "leaveAndRejoinActualGamePlaceCard2";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);
        Controller controller4 = new Controller(realLobbyGameListController, view4);
        Controller controller5 = new Controller(realLobbyGameListController, view5);

        HashMap<String, Controller> controllerMap = new HashMap<>();
        controllerMap.put(view1.name, controller1);
        controllerMap.put(view2.name, controller2);
        controllerMap.put(view3.name, controller3);
        controllerMap.put(view4.name, controller4);

        HashMap<String, ViewTest> viewMap = new HashMap<>();
        viewMap.put(view1.name, view1);
        viewMap.put(view2.name, view2);
        viewMap.put(view3.name, view3);
        viewMap.put(view4.name, view4);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller3.login(view3.name);
        controller4.login(view4.name);
        controller5.login(view5.name);
        controller1.createLobby(lobbyName1, 4);
        controller2.joinLobby(lobbyName1);
        controller3.joinLobby(lobbyName1);
        controller4.joinLobby(lobbyName1);
        controller5.createLobby(lobbyName2, 2);

        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert !lobbyGameListController.getViewMap().containsKey(view3.name);
        assert !lobbyGameListController.getViewMap().containsKey(view4.name);

        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();
        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);
        Player player3 = game.getPlayerFromNick(view3.name);
        Player player4 = game.getPlayerFromNick(view4.name);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());

        placeStartCard(player1, controller1);
        placeStartCard(player2, controller2);
        placeStartCard(player3, controller3);
        placeStartCard(player4, controller4);

        pawnChoicesSetupCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.YELLOW);
        controller3.choosePawn(PawnColors.RED);
        controller4.choosePawn(PawnColors.GREEN);

        setupObjChoicesCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.chooseSecretObjective(Lightifier.lightifyToCard(player1.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller2.chooseSecretObjective(Lightifier.lightifyToCard(player2.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller3.chooseSecretObjective(Lightifier.lightifyToCard(player3.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller4.chooseSecretObjective(Lightifier.lightifyToCard(player4.getUserHand().getSecretObjectiveChoices().getFirst()));

        setupActualGame(gameController, List.of(view1, view2, view3, view4));

        Player firstPlayer = game.getPlayersList().getFirst();
        Player secondPlayer = game.getPlayersList().get(1);

        Controller controllerFirst = controllerMap.get(firstPlayer.getNickname());
        Controller secondController = controllerMap.get(secondPlayer.getNickname());

        ViewTest viewFirst = viewMap.get(firstPlayer.getNickname());
        ViewTest viewSecond = viewMap.get(secondPlayer.getNickname());

        controllerFirst.leave();

        assert game.getCurrentPlayer().getNickname().equals(secondPlayer.getNickname());
        assert viewSecond.state == ViewState.PLACE_CARD;

        for(ViewTest view : viewMap.values()){
            if(!view.name.equals(viewFirst.name)) {
                assert view.lightGame.getLightGameParty().getCurrentPlayer().equals(viewSecond.name);
                if(!view.name.equals(viewSecond.name)){
                    assert view.state.equals(ViewState.IDLE);
                }
            }
        }

        controllerFirst.login(firstPlayer.getNickname());
        checkInitialization(viewFirst, gameController);
        checkGame(viewFirst, gameController);
        assert viewFirst.state.equals(ViewState.IDLE);
    }

    @Test
    void leaveAndRejoinActualGameDrawCard(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "duck";
        view4.name = "mickey";
        view5.name = "minnie";

        String lobbyName1 = "leaveAndRejoinActualGameDrawCard1";
        String lobbyName2 = "leaveAndRejoinActualGameDrawCard2";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);
        Controller controller4 = new Controller(realLobbyGameListController, view4);
        Controller controller5 = new Controller(realLobbyGameListController, view5);

        HashMap<String, Controller> controllerMap = new HashMap<>();
        controllerMap.put(view1.name, controller1);
        controllerMap.put(view2.name, controller2);
        controllerMap.put(view3.name, controller3);
        controllerMap.put(view4.name, controller4);

        HashMap<String, ViewTest> viewMap = new HashMap<>();
        viewMap.put(view1.name, view1);
        viewMap.put(view2.name, view2);
        viewMap.put(view3.name, view3);
        viewMap.put(view4.name, view4);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller3.login(view3.name);
        controller4.login(view4.name);
        controller5.login(view5.name);
        controller1.createLobby(lobbyName1, 4);
        controller2.joinLobby(lobbyName1);
        controller3.joinLobby(lobbyName1);
        controller4.joinLobby(lobbyName1);
        controller5.createLobby(lobbyName2, 2);

        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert !lobbyGameListController.getViewMap().containsKey(view3.name);
        assert !lobbyGameListController.getViewMap().containsKey(view4.name);

        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();
        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);
        Player player3 = game.getPlayerFromNick(view3.name);
        Player player4 = game.getPlayerFromNick(view4.name);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());

        placeStartCard(player1, controller1);
        placeStartCard(player2, controller2);
        placeStartCard(player3, controller3);
        placeStartCard(player4, controller4);

        pawnChoicesSetupCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.YELLOW);
        controller3.choosePawn(PawnColors.RED);
        controller4.choosePawn(PawnColors.GREEN);

        setupObjChoicesCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.chooseSecretObjective(Lightifier.lightifyToCard(player1.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller2.chooseSecretObjective(Lightifier.lightifyToCard(player2.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller3.chooseSecretObjective(Lightifier.lightifyToCard(player3.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller4.chooseSecretObjective(Lightifier.lightifyToCard(player4.getUserHand().getSecretObjectiveChoices().getFirst()));

        setupActualGame(gameController, List.of(view1, view2, view3, view4));

        Player firstPlayer = game.getPlayersList().getFirst();
        Player secondPlayer = game.getPlayersList().get(1);

        Controller controllerFirst = controllerMap.get(firstPlayer.getNickname());
        Controller secondController = controllerMap.get(secondPlayer.getNickname());

        ViewTest viewFirst = viewMap.get(firstPlayer.getNickname());
        ViewTest viewSecond = viewMap.get(secondPlayer.getNickname());

        controllerFirst.place(new LightPlacement(new Position(1,1), Lightifier.lightifyToCard(firstPlayer.getUserHand().getHand().stream().findFirst().get()), CardFace.BACK));
        assert firstPlayer.getHandSize() == 2;
        ArrayList<CardInHand> cardsBeforeleave = new ArrayList<>(firstPlayer.getUserHand().getHand().stream().toList());
        controllerFirst.leave();
        assert firstPlayer.getHandSize() == 3;
        ArrayList<CardInHand> cardsAfterleave = new ArrayList<>(firstPlayer.getUserHand().getHand().stream().toList());
        CardInHand diffCard = cardsAfterleave.stream().filter(card -> !cardsBeforeleave.contains(card)).findFirst().get();

        System.out.println(game.getCurrentPlayer().getNickname());
        System.out.println(game.getPlayersList().stream().map(Player::getNickname).toList());
        assert game.getCurrentPlayer().getNickname().equals(secondPlayer.getNickname());
        assert viewSecond.state == ViewState.PLACE_CARD;

        for(ViewTest view : viewMap.values()){
            if(!view.name.equals(viewFirst.name)) {
                assert view.lightGame.getLightGameParty().getCurrentPlayer().equals(viewSecond.name);
                assert Arrays.stream(view.lightGame.getHandOthers().get(viewFirst.name).getCards()).toList().contains(Lightifier.lightifyToBack(diffCard));
                if(!view.name.equals(viewSecond.name)){
                    assert view.state.equals(ViewState.IDLE);
                }
            }
        }

        controllerFirst.login(firstPlayer.getNickname());
        checkInitialization(viewFirst, gameController);
        checkGame(viewFirst, gameController);
        assert viewFirst.state.equals(ViewState.IDLE);
    }

    @Test
    void nextPlayerInTurnLeaves(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "duck";
        view4.name = "mickey";
        view5.name = "minnie";

        String lobbyName1 = "leaveAndRejoinActualGameDrawCard1";
        String lobbyName2 = "leaveAndRejoinActualGameDrawCard2";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);
        Controller controller4 = new Controller(realLobbyGameListController, view4);
        Controller controller5 = new Controller(realLobbyGameListController, view5);

        HashMap<String, Controller> controllerMap = new HashMap<>();
        controllerMap.put(view1.name, controller1);
        controllerMap.put(view2.name, controller2);
        controllerMap.put(view3.name, controller3);
        controllerMap.put(view4.name, controller4);

        HashMap<String, ViewTest> viewMap = new HashMap<>();
        viewMap.put(view1.name, view1);
        viewMap.put(view2.name, view2);
        viewMap.put(view3.name, view3);
        viewMap.put(view4.name, view4);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller3.login(view3.name);
        controller4.login(view4.name);
        controller5.login(view5.name);
        controller1.createLobby(lobbyName1, 4);
        controller2.joinLobby(lobbyName1);
        controller3.joinLobby(lobbyName1);
        controller4.joinLobby(lobbyName1);
        controller5.createLobby(lobbyName2, 2);

        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert !lobbyGameListController.getViewMap().containsKey(view3.name);
        assert !lobbyGameListController.getViewMap().containsKey(view4.name);

        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();
        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);
        Player player3 = game.getPlayerFromNick(view3.name);
        Player player4 = game.getPlayerFromNick(view4.name);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());

        placeStartCard(player1, controller1);
        placeStartCard(player2, controller2);
        placeStartCard(player3, controller3);
        placeStartCard(player4, controller4);

        pawnChoicesSetupCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.YELLOW);
        controller3.choosePawn(PawnColors.RED);
        controller4.choosePawn(PawnColors.GREEN);

        setupObjChoicesCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.chooseSecretObjective(Lightifier.lightifyToCard(player1.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller2.chooseSecretObjective(Lightifier.lightifyToCard(player2.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller3.chooseSecretObjective(Lightifier.lightifyToCard(player3.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller4.chooseSecretObjective(Lightifier.lightifyToCard(player4.getUserHand().getSecretObjectiveChoices().getFirst()));

        setupActualGame(gameController, List.of(view1, view2, view3, view4));

        Player firstPlayer = game.getPlayersList().getFirst();
        Player secondPlayer = game.getPlayersList().get(1);
        Player thirdPlayer = game.getPlayersList().get(2);
        Player lastPlayer = game.getPlayersList().getLast();

        Controller firstController = controllerMap.get(firstPlayer.getNickname());
        Controller secondController = controllerMap.get(secondPlayer.getNickname());
        Controller thirdController = controllerMap.get(thirdPlayer.getNickname());
        Controller lastController = controllerMap.get(lastPlayer.getNickname());


        ViewTest viewFirst = viewMap.get(firstPlayer.getNickname());
        ViewTest viewSecond = viewMap.get(secondPlayer.getNickname());
        ViewTest viewThird = viewMap.get(thirdPlayer.getNickname());
        ViewTest viewLast = viewMap.get(lastPlayer.getNickname());

        secondController.leave();
        firstController.place(new LightPlacement(new Position(1,1), Lightifier.lightifyToCard(firstPlayer.getUserHand().getHand().stream().findFirst().get()), CardFace.BACK));
        firstController.draw(DrawableCard.RESOURCECARD, 0);

        assert game.getCurrentPlayer().getNickname().equals(thirdPlayer.getNickname());
    }

    @Test
    void allPlayerLeavesNextTurn(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "duck";
        view4.name = "mickey";
        view5.name = "minnie";

        String lobbyName1 = "leaveAndRejoinActualGameDrawCard1";
        String lobbyName2 = "leaveAndRejoinActualGameDrawCard2";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);
        Controller controller4 = new Controller(realLobbyGameListController, view4);
        Controller controller5 = new Controller(realLobbyGameListController, view5);

        HashMap<String, Controller> controllerMap = new HashMap<>();
        controllerMap.put(view1.name, controller1);
        controllerMap.put(view2.name, controller2);
        controllerMap.put(view3.name, controller3);
        controllerMap.put(view4.name, controller4);

        HashMap<String, ViewTest> viewMap = new HashMap<>();
        viewMap.put(view1.name, view1);
        viewMap.put(view2.name, view2);
        viewMap.put(view3.name, view3);
        viewMap.put(view4.name, view4);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller3.login(view3.name);
        controller4.login(view4.name);
        controller5.login(view5.name);
        controller1.createLobby(lobbyName1, 4);
        controller2.joinLobby(lobbyName1);
        controller3.joinLobby(lobbyName1);
        controller4.joinLobby(lobbyName1);
        controller5.createLobby(lobbyName2, 2);

        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert !lobbyGameListController.getViewMap().containsKey(view3.name);
        assert !lobbyGameListController.getViewMap().containsKey(view4.name);

        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();
        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);
        Player player3 = game.getPlayerFromNick(view3.name);
        Player player4 = game.getPlayerFromNick(view4.name);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());

        placeStartCard(player1, controller1);
        placeStartCard(player2, controller2);
        placeStartCard(player3, controller3);
        placeStartCard(player4, controller4);

        pawnChoicesSetupCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.YELLOW);
        controller3.choosePawn(PawnColors.RED);
        controller4.choosePawn(PawnColors.GREEN);

        setupObjChoicesCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.chooseSecretObjective(Lightifier.lightifyToCard(player1.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller2.chooseSecretObjective(Lightifier.lightifyToCard(player2.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller3.chooseSecretObjective(Lightifier.lightifyToCard(player3.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller4.chooseSecretObjective(Lightifier.lightifyToCard(player4.getUserHand().getSecretObjectiveChoices().getFirst()));

        setupActualGame(gameController, List.of(view1, view2, view3, view4));

        Player firstPlayer = game.getPlayersList().getFirst();
        Player secondPlayer = game.getPlayersList().get(1);
        Player thirdPlayer = game.getPlayersList().get(2);
        Player lastPlayer = game.getPlayersList().getLast();

        Controller firstController = controllerMap.get(firstPlayer.getNickname());
        Controller secondController = controllerMap.get(secondPlayer.getNickname());
        Controller thirdController = controllerMap.get(thirdPlayer.getNickname());
        Controller lastController = controllerMap.get(lastPlayer.getNickname());


        ViewTest viewFirst = viewMap.get(firstPlayer.getNickname());
        ViewTest viewSecond = viewMap.get(secondPlayer.getNickname());
        ViewTest viewThird = viewMap.get(thirdPlayer.getNickname());
        ViewTest viewLast = viewMap.get(lastPlayer.getNickname());

        secondController.leave();
        thirdController.leave();
        lastController.leave();
        firstController.leave();

        assert game.getCurrentPlayer().equals(firstPlayer);
    }

    /*
    leave and rejoin of a player when winning condition are met,
    and the player is the last player in game
     */
    @Test
    void leaveAndRejoinPlaceWinning(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "duck";
        view4.name = "mickey";
        view5.name = "minnie";

        String lobbyName1 = "leaveAndRejoinPlaceWinning1";
        String lobbyName2 = "leaveAndRejoinPlaceWinning2";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);
        Controller controller4 = new Controller(realLobbyGameListController, view4);
        Controller controller5 = new Controller(realLobbyGameListController, view5);

        HashMap<String, Controller> controllerMap = new HashMap<>();
        controllerMap.put(view1.name, controller1);
        controllerMap.put(view2.name, controller2);
        controllerMap.put(view3.name, controller3);
        controllerMap.put(view4.name, controller4);

        HashMap<String, ViewTest> viewMap = new HashMap<>();
        viewMap.put(view1.name, view1);
        viewMap.put(view2.name, view2);
        viewMap.put(view3.name, view3);
        viewMap.put(view4.name, view4);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller3.login(view3.name);
        controller4.login(view4.name);
        controller5.login(view5.name);
        controller1.createLobby(lobbyName1, 4);
        controller2.joinLobby(lobbyName1);
        controller3.joinLobby(lobbyName1);
        controller4.joinLobby(lobbyName1);
        controller5.createLobby(lobbyName2, 2);

        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert !lobbyGameListController.getViewMap().containsKey(view3.name);
        assert !lobbyGameListController.getViewMap().containsKey(view4.name);

        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();
        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);
        Player player3 = game.getPlayerFromNick(view3.name);
        Player player4 = game.getPlayerFromNick(view4.name);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());

        placeStartCard(player1, controller1);
        placeStartCard(player2, controller2);
        placeStartCard(player3, controller3);
        placeStartCard(player4, controller4);

        pawnChoicesSetupCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.YELLOW);
        controller3.choosePawn(PawnColors.RED);
        controller4.choosePawn(PawnColors.GREEN);

        setupObjChoicesCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.chooseSecretObjective(Lightifier.lightifyToCard(player1.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller2.chooseSecretObjective(Lightifier.lightifyToCard(player2.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller3.chooseSecretObjective(Lightifier.lightifyToCard(player3.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller4.chooseSecretObjective(Lightifier.lightifyToCard(player4.getUserHand().getSecretObjectiveChoices().getFirst()));

        setupActualGame(gameController, List.of(view1, view2, view3, view4));

        Player firstPlayer = game.getPlayersList().getFirst();
        Player secondPlayer = game.getPlayersList().get(1);
        Player thirdPlayer = game.getPlayersList().get(2);
        Player lastPlayer = game.getPlayersList().getLast();

        Controller controllerFirst = controllerMap.get(firstPlayer.getNickname());
        Controller controllerSecond = controllerMap.get(secondPlayer.getNickname());
        Controller controllerThird = controllerMap.get(thirdPlayer.getNickname());
        Controller controllerLast = controllerMap.get(lastPlayer.getNickname());

        //trigger end game
        HashMap<CardCorner, Collectable> cornerMap1 = new HashMap<>();
        for (CardCorner corner : CardCorner.values()) {
            cornerMap1.put(corner, Resource.PLANT);
        }
        cornerMap1.put(CardCorner.TL, SpecialCollectable.EMPTY);
        ResourceCard exodiaTheForbidden = new ResourceCard(1, 1, Resource.PLANT, 100, cornerMap1);
        Placement exodiaPlacement = new Placement(new Position(1,1), exodiaTheForbidden, CardFace.FRONT);

        Hand handFirst = firstPlayer.getUserHand();
        CardInHand removedCard = handFirst.getHand().stream().reduce((a,b)->a).get();
        handFirst.removeCard(removedCard);
        handFirst.addCard(exodiaTheForbidden);
        firstPlayer.playCard(exodiaPlacement);
        handFirst.addCard(removedCard);

        placeRandom(firstPlayer, controllerFirst);
        //move on with turns
        controllerFirst.draw(DrawableCard.RESOURCECARD, 0);

        placeRandom(secondPlayer, controllerSecond);
        controllerSecond.draw(DrawableCard.RESOURCECARD, 0);

        placeRandom(thirdPlayer, controllerThird);
        controllerThird.draw(DrawableCard.RESOURCECARD, 0);

        assert game.getCurrentPlayer().getNickname().equals(lastPlayer.getNickname());

        controllerLast.leave();
        controllerLast.login(lastPlayer.getNickname());

        assert game.getCurrentPlayer().getNickname().equals(firstPlayer.getNickname());

        placeRandom(firstPlayer, controllerFirst);
        controllerFirst.draw(DrawableCard.RESOURCECARD, 0);

        placeRandom(secondPlayer, controllerSecond);
        controllerSecond.draw(DrawableCard.RESOURCECARD, 0);

        placeRandom(thirdPlayer, controllerThird);
        controllerThird.draw(DrawableCard.RESOURCECARD, 0);

        controllerLast.leave();

        assert game.getState().equals(GameState.END_GAME);

        controllerLast.login(lastPlayer.getNickname());
        assert viewMap.get(lastPlayer.getNickname()).state.equals(ViewState.LOGIN_FORM);
    }

    private void placeRandom(Player player, Controller controller){
        Hand hand = player.getUserHand();
        Position position = player.getUserCodex().getFrontier().getFrontier().getFirst();
        LightCard lightCard = Lightifier.lightifyToCard(hand.getHand().stream().reduce((a,b)->a).get());
        LightPlacement lightPlacement = new LightPlacement(position, lightCard, CardFace.BACK);
        controller.place(lightPlacement);
    }

    @Test
    void leaveAndRejoinActualGameEnding(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "duck";
        view4.name = "mickey";
        view5.name = "minnie";

        String lobbyName1 = "leaveAndRejoinActualGameEnding1";
        String lobbyName2 = "leaveAndRejoinActualGameEnding2";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);
        Controller controller4 = new Controller(realLobbyGameListController, view4);
        Controller controller5 = new Controller(realLobbyGameListController, view5);

        HashMap<String, Controller> controllerMap = new HashMap<>();
        controllerMap.put(view1.name, controller1);
        controllerMap.put(view2.name, controller2);
        controllerMap.put(view3.name, controller3);
        controllerMap.put(view4.name, controller4);

        HashMap<String, ViewTest> viewMap = new HashMap<>();
        viewMap.put(view1.name, view1);
        viewMap.put(view2.name, view2);
        viewMap.put(view3.name, view3);
        viewMap.put(view4.name, view4);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller3.login(view3.name);
        controller4.login(view4.name);
        controller5.login(view5.name);
        controller1.createLobby(lobbyName1, 4);
        controller2.joinLobby(lobbyName1);
        controller3.joinLobby(lobbyName1);
        controller4.joinLobby(lobbyName1);
        controller5.createLobby(lobbyName2, 2);

        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert !lobbyGameListController.getViewMap().containsKey(view3.name);
        assert !lobbyGameListController.getViewMap().containsKey(view4.name);

        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();
        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);
        Player player3 = game.getPlayerFromNick(view3.name);
        Player player4 = game.getPlayerFromNick(view4.name);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());

        placeStartCard(player1, controller1);
        placeStartCard(player2, controller2);
        placeStartCard(player3, controller3);
        placeStartCard(player4, controller4);

        pawnChoicesSetupCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.YELLOW);
        controller3.choosePawn(PawnColors.RED);
        controller4.choosePawn(PawnColors.GREEN);

        setupObjChoicesCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.chooseSecretObjective(Lightifier.lightifyToCard(player1.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller2.chooseSecretObjective(Lightifier.lightifyToCard(player2.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller3.chooseSecretObjective(Lightifier.lightifyToCard(player3.getUserHand().getSecretObjectiveChoices().getFirst()));
        controller4.chooseSecretObjective(Lightifier.lightifyToCard(player4.getUserHand().getSecretObjectiveChoices().getFirst()));

        setupActualGame(gameController, List.of(view1, view2, view3, view4));

        Player firstPlayer = game.getPlayersList().getFirst();
        Player secondPlayer = game.getPlayersList().get(1);
        Player thirdPlayer = game.getPlayersList().get(2);
        Player lastPlayer = game.getPlayersList().getLast();

        Controller controllerFirst = controllerMap.get(firstPlayer.getNickname());
        Controller controllerSecond = controllerMap.get(secondPlayer.getNickname());
        Controller controllerThird = controllerMap.get(thirdPlayer.getNickname());
        Controller controllerLast = controllerMap.get(lastPlayer.getNickname());

        //trigger end game
        HashMap<CardCorner, Collectable> cornerMap1 = new HashMap<>();
        for (CardCorner corner : CardCorner.values()) {
            cornerMap1.put(corner, Resource.PLANT);
        }
        cornerMap1.put(CardCorner.TL, SpecialCollectable.EMPTY);
        ResourceCard exodiaTheForbidden = new ResourceCard(1, 1, Resource.PLANT, 100, cornerMap1);
        Placement exodiaPlacement = new Placement(new Position(1,1), exodiaTheForbidden, CardFace.FRONT);

        Hand handFirst = firstPlayer.getUserHand();
        CardInHand removedCard = handFirst.getHand().stream().reduce((a,b)->a).get();
        handFirst.removeCard(removedCard);
        handFirst.addCard(exodiaTheForbidden);
        firstPlayer.playCard(exodiaPlacement);
        handFirst.addCard(removedCard);

        //move on with turns
        placeRandom(firstPlayer, controllerFirst);

        controllerFirst.draw(DrawableCard.RESOURCECARD, 0);

        Hand handSecond = secondPlayer.getUserHand();
        Hand handThird = thirdPlayer.getUserHand();

        placeRandom(secondPlayer, controllerSecond);
        controllerSecond.draw(DrawableCard.RESOURCECARD, 0);

        placeRandom(thirdPlayer, controllerThird);
        controllerThird.draw(DrawableCard.RESOURCECARD, 0);

        assert game.getCurrentPlayer().getNickname().equals(lastPlayer.getNickname());

        controllerLast.leave();
        controllerLast.login(lastPlayer.getNickname());

        assert game.getCurrentPlayer().getNickname().equals(firstPlayer.getNickname());



        placeRandom(firstPlayer, controllerFirst);
        controllerFirst.draw(DrawableCard.RESOURCECARD, 0);

        placeRandom(secondPlayer, controllerSecond);
        controllerSecond.draw(DrawableCard.RESOURCECARD, 0);

        placeRandom(thirdPlayer, controllerThird);
        controllerThird.draw(DrawableCard.RESOURCECARD, 0);

        controllerLast.leave();

        assert game.getState().equals(GameState.END_GAME);

        controllerLast.login(lastPlayer.getNickname());
        assert viewMap.get(lastPlayer.getNickname()).state.equals(ViewState.LOGIN_FORM);
    }

    @Test
    void leaveDeckFinishedPlaceCard() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        int numberOfPlayers = 2;
        int totalCardsInDeck = 40;
        int resourceCardsInHandAtStart = 2;
        int goldCardsInHandAtStart = 1;

        String lobbyName1 = "leaveDeckFinishedPlaceCard1";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.joinLobby(lobbyName1);

        LightCard startCard1 = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0, 0), startCard1, CardFace.FRONT);
        LightCard startCard2 = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0, 0), startCard2, CardFace.FRONT);

        controller1.place(startPlacement1);
        controller2.place(startPlacement2);
        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.RED);

        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];

        controller1.chooseSecretObjective(secretObjective1);
        controller2.chooseSecretObjective(secretObjective2);

        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        PublicGameController publicController = new PublicGameController(gameController);
        Game game = publicController.getGame();

        PublicController firstPlayerController = new PublicController(view1.name.equals(game.getCurrentPlayer().getNickname()) ? controller1 : controller2);
        PublicController secondPlayerController = new PublicController(view1.name.equals(game.getCurrentPlayer().getNickname()) ? controller2 : controller1);
        Player firstPlayer = game.getPlayerFromNick(firstPlayerController.getNickname());
        Player secondPlayer = game.getPlayerFromNick(secondPlayerController.getNickname());

        //empty resourceDeck
        for (int i = 0; i < (totalCardsInDeck - resourceCardsInHandAtStart * numberOfPlayers) / numberOfPlayers; i++) {
            LightCard cardPlaced1 = Lightifier.lightifyToCard(firstPlayer.getUserHand().getHand().stream().toList().getFirst());
            Position position1 = firstPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
            LightPlacement placement1 = new LightPlacement(position1, cardPlaced1, CardFace.BACK);

            firstPlayerController.controller.place(placement1);
            firstPlayerController.controller.draw(DrawableCard.RESOURCECARD, 0);

            LightCard cardPlaced2 = Lightifier.lightifyToCard(secondPlayer.getUserHand().getHand().stream().toList().getFirst());
            Position position2 = secondPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
            LightPlacement placement2 = new LightPlacement(position2, cardPlaced2, CardFace.BACK);

            secondPlayerController.controller.place(placement2);
            secondPlayerController.controller.draw(DrawableCard.RESOURCECARD, 0);
        }

        assert game.getResourceCardDeck().isEmpty();

        //empty goldDeck
        for (int i = 0; i < (totalCardsInDeck - goldCardsInHandAtStart * numberOfPlayers) / numberOfPlayers; i++) {
            LightCard cardPlaced1 = Lightifier.lightifyToCard(firstPlayer.getUserHand().getHand().stream().toList().getFirst());
            Position position1 = firstPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
            LightPlacement placement1 = new LightPlacement(position1, cardPlaced1, CardFace.BACK);

            firstPlayerController.controller.place(placement1);
            firstPlayerController.controller.draw(DrawableCard.GOLDCARD, 0);

            LightCard cardPlaced2 = Lightifier.lightifyToCard(secondPlayer.getUserHand().getHand().stream().toList().getFirst());
            Position position2 = secondPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
            LightPlacement placement2 = new LightPlacement(position2, cardPlaced2, CardFace.BACK);

            secondPlayerController.controller.place(placement2);
            secondPlayerController.controller.draw(DrawableCard.GOLDCARD, 0);
        }

        assert game.getGoldCardDeck().isEmpty();

        assert game.duringEndingTurns();
        assert game.getState().equals(GameState.LAST_TURNS);

        assert game.getCurrentPlayer().equals(firstPlayer);

        LightCard cardPlaced1 = Lightifier.lightifyToCard(firstPlayer.getUserHand().getHand().stream().toList().getFirst());
        Position position1 = firstPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement1 = new LightPlacement(position1, cardPlaced1, CardFace.BACK);

        firstPlayerController.controller.place(placement1);

        secondPlayerController.controller.leave();

        assert game.getState().equals(GameState.END_GAME);
    }

    @Test
    void leaveLastCardBeforeDeckFinished() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        int numberOfPlayers = 2;
        int totalCardsInDeck = 40;
        int resourceCardsInHandAtStart = 2;
        int goldCardsInHandAtStart = 1;

        String lobbyName1 = "leaveLastCardBeforeDeckFinished1";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.joinLobby(lobbyName1);

        LightCard startCard1 = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0, 0), startCard1, CardFace.FRONT);
        LightCard startCard2 = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0, 0), startCard2, CardFace.FRONT);

        controller1.place(startPlacement1);
        controller2.place(startPlacement2);
        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.RED);

        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];

        controller1.chooseSecretObjective(secretObjective1);
        controller2.chooseSecretObjective(secretObjective2);

        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        PublicGameController publicController = new PublicGameController(gameController);
        Game game = publicController.getGame();

        PublicController firstPlayerController = new PublicController(view1.name.equals(game.getCurrentPlayer().getNickname()) ? controller1 : controller2);
        PublicController secondPlayerController = new PublicController(view1.name.equals(game.getCurrentPlayer().getNickname()) ? controller2 : controller1);
        Player firstPlayer = game.getPlayerFromNick(firstPlayerController.getNickname());
        Player secondPlayer = game.getPlayerFromNick(secondPlayerController.getNickname());

        //empty resourceDeck
        for (int i = 0; i < (totalCardsInDeck - resourceCardsInHandAtStart * numberOfPlayers) / numberOfPlayers; i++) {
            LightCard cardPlaced1 = Lightifier.lightifyToCard(firstPlayer.getUserHand().getHand().stream().toList().getFirst());
            Position position1 = firstPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
            LightPlacement placement1 = new LightPlacement(position1, cardPlaced1, CardFace.BACK);

            firstPlayerController.controller.place(placement1);
            firstPlayerController.controller.draw(DrawableCard.RESOURCECARD, 0);

            LightCard cardPlaced2 = Lightifier.lightifyToCard(secondPlayer.getUserHand().getHand().stream().toList().getFirst());
            Position position2 = secondPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
            LightPlacement placement2 = new LightPlacement(position2, cardPlaced2, CardFace.BACK);

            secondPlayerController.controller.place(placement2);
            secondPlayerController.controller.draw(DrawableCard.RESOURCECARD, 0);
        }

        assert game.getResourceCardDeck().isEmpty();

        //empty goldDeck leaving a card
        for (int i = 0; i < (totalCardsInDeck - goldCardsInHandAtStart * numberOfPlayers) / numberOfPlayers; i++) {
            LightCard cardPlaced1 = Lightifier.lightifyToCard(firstPlayer.getUserHand().getHand().stream().toList().getFirst());
            Position position1 = firstPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
            LightPlacement placement1 = new LightPlacement(position1, cardPlaced1, CardFace.BACK);

            firstPlayerController.controller.place(placement1);
            firstPlayerController.controller.draw(DrawableCard.GOLDCARD, 0);

            LightCard cardPlaced2 = Lightifier.lightifyToCard(secondPlayer.getUserHand().getHand().stream().toList().getFirst());
            Position position2 = secondPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
            LightPlacement placement2 = new LightPlacement(position2, cardPlaced2, CardFace.BACK);

            secondPlayerController.controller.place(placement2);

            if (i < (totalCardsInDeck - goldCardsInHandAtStart * numberOfPlayers) / numberOfPlayers - 1) {
                secondPlayerController.controller.draw(DrawableCard.GOLDCARD, 0);
            }
        }

        assert game.getGoldCardDeck().getBuffer().stream().filter(Objects::nonNull).toList().size() == 1;
        assert game.getCurrentPlayer().equals(secondPlayer);
        assert secondPlayer.getState().equals(PlayerState.DRAW);

        secondPlayerController.controller.leave();
        assert game.getGoldCardDeck().isEmpty();
        secondPlayerController.controller.login(secondPlayer.getNickname());

        assert game.duringEndingTurns();
        assert game.getState().equals(GameState.LAST_TURNS);

        assert game.getCurrentPlayer().equals(firstPlayer);

        placeRandom(firstPlayer, firstPlayerController.controller);

        assert game.getCurrentPlayer().equals(secondPlayer);

        placeRandom(secondPlayer, secondPlayerController.controller);

        assert game.getState().equals(GameState.END_GAME);
    }

    private void checkInitialization(ViewTest view, PublicGameController gameController){
        Game game = gameController.getGame();
        assert view.lightLobbyList.getLobbies().isEmpty();

        assert view.lightLobby.name().isEmpty();
        assert view.lightLobby.numberMaxPlayer() == 0;
        assert view.lightLobby.nicknames().isEmpty();

        assert view.lightGame.getLightGameParty().getGameName().equals(game.getName());

        List<String> playerNick = game.getGameParty().getPlayersList().stream().map(Player::getNickname).toList();
        List<String> playerNicksOnView = view.lightGame.getLightGameParty().getPlayerActiveMap().keySet().stream().toList();
        System.out.println(playerNick);
        System.out.println(playerNicksOnView);
        assert playerNicksOnView.containsAll(playerNick);
        assert playerNick.containsAll(playerNicksOnView);
    }

    private void checkGame(ViewTest view, PublicGameController gameController){
        Game game = gameController.getGame();
        assert view.lightGame.getLightGameParty().getPlayerActiveMap().keySet().containsAll(game.getPlayersList().stream().map(Player::getNickname).toList());
        for(Map.Entry<String, Boolean> playerActivity : view.lightGame.getLightGameParty().getPlayerActiveMap().entrySet()){
            assert gameController.getViewMap().containsKey(playerActivity.getKey()) == playerActivity.getValue();
        }
        for(Map.Entry<String, LightCodex> codexPlayer : view.lightGame.getCodexMap().entrySet()){
            Player player = game.getPlayerFromNick(codexPlayer.getKey());
            assert codexPlayer.getValue().getPoints() == player.getUserCodex().getPoints();
            assert codexPlayer.getValue().getFrontier().frontier().equals(player.getUserCodex().getFrontier().getFrontier().stream().toList());
            assert codexPlayer.getValue().getEarnedCollectables().equals(player.getUserCodex().getEarnedCollectables());
        }
        assert Arrays.stream(view.lightGame.getHand().getCards()).map(c -> Heavifier.heavifyCardInHand(c, lobbyGameListController.getCardTable())).toList().containsAll(game.getPlayerFromNick(view.name).getUserHand().getHand().stream().toList());
        assert game.getPlayerFromNick(view.name).getUserHand().getHand().stream().toList().containsAll(Arrays.stream(view.lightGame.getHand().getCards()).map(c -> Heavifier.heavifyCardInHand(c, lobbyGameListController.getCardTable())).toList());
        assert view.lightGame.getHand().getSecretObjective().equals(Lightifier.lightifyToCard(game.getPlayerFromNick(view.name).getUserHand().getSecretObjective()));
        for(Map.Entry<String, LightHandOthers> otherHand : view.lightGame.getHandOthers().entrySet()){
            Player player = game.getPlayerFromNick(otherHand.getKey());
            System.out.println(Arrays.toString(otherHand.getValue().getCards()));
            ArrayList<Integer> otherCardsOnView = new ArrayList<>();
            for(LightBack cardBack : otherHand.getValue().getCards()){
                if(cardBack != null)
                    otherCardsOnView.add(cardBack.idBack());
                else
                    otherCardsOnView.add(null);
            }
            ArrayList<Integer> otherCardsOnModel = new ArrayList<>();
            for(Card card : player.getUserHand().getHand()){
                if(card != null)
                    otherCardsOnModel.add(card.getIdBack());
            }
            for(int i = 3 - player.getHandSize(); i>0; i--){
                otherCardsOnModel.add(null);
            }
            assert otherCardsOnView.containsAll(otherCardsOnModel);
            assert otherCardsOnModel.containsAll(otherCardsOnView);
        }
        assert game.getGoldCardDeck().showTopCardOfDeck().getIdBack() == view.lightGame.getGoldDeck().getDeckBack().idBack();
        assert game.getResourceCardDeck().showTopCardOfDeck().getIdBack() == view.lightGame.getResourceDeck().getDeckBack().idBack();

        assert game.getGoldCardDeck().getBuffer().stream().map(Lightifier::lightifyToCard).toList().containsAll(Arrays.stream(view.lightGame.getGoldDeck().getCardBuffer()).toList());
        assert Arrays.stream(view.lightGame.getGoldDeck().getCardBuffer()).toList().containsAll(game.getGoldCardDeck().getBuffer().stream().map(Lightifier::lightifyToCard).toList());

        assert game.getResourceCardDeck().getBuffer().stream().map(Lightifier::lightifyToCard).toList().containsAll(Arrays.stream(view.lightGame.getResourceDeck().getCardBuffer()).toList());
        assert Arrays.stream(view.lightGame.getResourceDeck().getCardBuffer()).toList().containsAll(game.getResourceCardDeck().getBuffer().stream().map(Lightifier::lightifyToCard).toList());

        assert game.getCommonObjective().stream().map(Lightifier::lightifyToCard).toList().containsAll(Arrays.stream(view.lightGame.getPublicObjective()).toList());
        assert Arrays.stream(view.lightGame.getPublicObjective()).toList().containsAll(game.getCommonObjective().stream().map(Lightifier::lightifyToCard).toList());
    }

    private void pawnChoicesSetupCorrectly(PublicGameController gameController, List<ViewTest> playerViews){
        Game game = gameController.getGame();

        assert game.getState().equals(GameState.CHOOSE_PAWN);
        assert !game.getState().equals(GameState.CHOOSE_START_CARD);
        assert !game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);
        assert !game.getState().equals(GameState.END_GAME);

        assert game.getPawnChoices().containsAll(Arrays.stream(PawnColors.values()).toList());

        game.getPlayersList().forEach(user->{
            System.out.println(user.getNickname());
            assert user.getPawnColor() == null;
        });

        playerViews.forEach(view -> {
            System.out.println(view.name);
            assert view.state.equals(ViewState.CHOOSE_PAWN);
            assert view.lightGame.getLightGameParty().getPawnChoices().containsAll(Arrays.stream(PawnColors.values()).toList());
            assert view.lightGame.getLightGameParty().getPlayersColor().isEmpty();
        });
    }

    private void setupObjChoicesCorrectly(PublicGameController gameController, List<ViewTest> playerViews){
        Game game = gameController.getGame();

        assert game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);
        assert !game.getState().equals(GameState.CHOOSE_PAWN);
        assert !game.getState().equals(GameState.CHOOSE_START_CARD);
        assert !game.getState().equals(GameState.END_GAME);

        List<String> nicksToCheck = playerViews.stream().map(view->view.name).toList();
        List<Player> usersToCheck = game.getPlayersList().stream().filter(user->nicksToCheck.contains(user.getNickname())).toList();

        usersToCheck.forEach(player->{
            System.out.println(player.getNickname());
            assert player.getState().equals(PlayerState.CHOOSE_SECRET_OBJECTIVE);
            List<ObjectiveCard> objOptions = player.getUserHand().getSecretObjectiveChoices();

            System.out.println(player.getUserHand().getSecretObjective());
            assert player.getUserHand().getSecretObjective() == null;
            Assertions.assertNotNull(objOptions);
            assert !objOptions.isEmpty();
            assert objOptions.size() == 2;
        });

        playerViews.forEach(view -> {
            System.out.println(view.name);
            List<ObjectiveCard> objOptions = game.getPlayerFromNick(view.name).getUserHand().getSecretObjectiveChoices();
            List<LightCard> objOnModel = objOptions.stream().map(Lightifier::lightifyToCard).toList();
            List<LightCard> objOnView = Arrays.stream(view.lightGame.getHand().getSecretObjectiveOptions()).toList();

            assert view.state.equals(ViewState.SELECT_OBJECTIVE);
            assert view.lightGame.getHand().getSecretObjective() == null;
            assert objOnView.containsAll(objOnModel);
            assert objOnModel.containsAll(objOnView);
        });
    }

    private void setupActualGame(PublicGameController gameController, List<ViewTest> playerViews){
        Game game = gameController.getGame();

        assert !game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);
        assert !game.getState().equals(GameState.CHOOSE_PAWN);
        assert !game.getState().equals(GameState.CHOOSE_START_CARD);
        assert !game.getState().equals(GameState.END_GAME);

        List<String> nicksToCheck = playerViews.stream().map(view->view.name).toList();
        List<Player> usersToCheck = game.getPlayersList().stream().filter(user->nicksToCheck.contains(user.getNickname())).toList();
        List<String> allUserInGameNick = game.getPlayersList().stream().map(Player::getNickname).toList();

        usersToCheck.forEach(player->{
            System.out.println(player.getNickname());
            assert player.getState().equals(PlayerState.PLACE) || player.getState().equals(PlayerState.IDLE);
            assert player.getUserHand().getHand().stream().allMatch(Objects::nonNull);
            assert player.getUserHand().getHand().size() == 3;
        });

        playerViews.forEach(view -> {
            System.out.println(view.name);
            Player player = game.getPlayerFromNick(view.name);

            List<LightCard> yourHandOnView = Arrays.stream(view.lightGame.getHand().getCards()).toList();
            List<LightCard> yourHandOnModel = player.getUserHand().getHand().stream().map(Lightifier::lightifyToCard).toList();
            assert yourHandOnModel.containsAll(yourHandOnView);
            assert yourHandOnView.containsAll(yourHandOnModel);

            for(String nick : allUserInGameNick){
                if(!nick.equals(view.name)) {
                    System.out.println(view.name + ":" + nick);
                    List<LightBack> otherHandOnView = Arrays.stream(view.lightGame.getHandOthers().get(nick).getCards()).toList();
                    List<LightBack> otherHandOnModel = game.getPlayerFromNick(nick).getUserHand().getHand().stream().map(Lightifier::lightifyToBack).toList();
                    assert otherHandOnModel.containsAll(otherHandOnView);
                    assert otherHandOnView.containsAll(otherHandOnModel);
                }
            }

            if(game.getCurrentPlayer().equals(player))
                assert view.state.equals(ViewState.PLACE_CARD);
            else
                assert view.state.equals(ViewState.IDLE);
        });
    }

    private void placeStartCard(Player player, Controller controller){
        LightPlacement lightPlacement4 = new LightPlacement(new Position(0,0), Lightifier.lightifyToCard(player.getUserHand().getStartCard()), CardFace.FRONT);
        controller.place(lightPlacement4);
    }
}
