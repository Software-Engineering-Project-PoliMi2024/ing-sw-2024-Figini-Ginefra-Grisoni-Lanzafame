package it.polimi.ingsw.controller;

import it.polimi.ingsw.OSRelated;
import it.polimi.ingsw.controller.PublicController.PublicGameController;
import it.polimi.ingsw.controller.PublicController.PublicLobbyController;
import it.polimi.ingsw.controller.PublicController.PublicLobbyGameListController;
import it.polimi.ingsw.controller.persistence.PersistenceFactory;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.ViewTest;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.model.playerReleted.Player;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.GameState;
import it.polimi.ingsw.view.ViewState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ResilienceTest {
    private LobbyGameListsController realLobbyGameListController;
    private PublicLobbyGameListController lobbyGameListController;

    @BeforeAll
    public static void setUpAll(){
        OSRelated.checkOrCreateDataFolderServer(); //Create the dataFolder if necessary. Normally this is done in the Server class
    }

    @BeforeEach
    public void setUp(){
        PersistenceFactory.eraseAllSaves();
        realLobbyGameListController = new LobbyGameListsController();
        lobbyGameListController = new PublicLobbyGameListController(realLobbyGameListController);

    }
    @Test
    void disconnectFromLobbyList(){
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

        assert lobbyGameListController.getViewMap().containsKey(view2.name);
        assert view2.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().contains(lobbyName1);

        controller2.disconnect();

        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert view2.lightLobbyList.getLobbies().isEmpty();
    }

    @Test
    void disconnectFromLobby(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";

        String lobbyName1 = "test1";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 3);
        controller2.joinLobby(lobbyName1);

        assert !lobbyGameListController.getViewMap().containsKey(view2.name);
        assert view2.lightLobbyList.getLobbies().isEmpty();
        assert view2.lightLobby.name().equals(lobbyName1);

        controller2.disconnect();

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

        String lobbyName1 = "test1";
        String lobbyName2 = "test2";
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
        Player player1 = game.getUserFromNick(view1.name);
        Player player2 = game.getUserFromNick(view2.name);
        Player player3 = game.getUserFromNick(view3.name);
        Player player4 = game.getUserFromNick(view4.name);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());

        assert gameController.getViewMap().containsKey(view2.name);
        assert game.getName().equals(lobbyName1);
        assert game.getUsersList().stream().map(Player::getNickname).toList().contains(view2.name);

        controller2.disconnect();

        assert !gameController.getViewMap().containsKey(view2.name);

        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        assert !gameController.getViewMap().containsKey(view2.name);
        assert game.getName().equals(lobbyName1);
        assert game.getUsersList().stream().map(Player::getNickname).toList().contains(view2.name);

        assert view2.lightGame.getCodexMap().isEmpty();
        assert view2.lightGame.getLightGameParty().getPlayerActiveList().isEmpty();
        Assertions.assertNull(view2.lightGame.getLightGameParty().getCurrentPlayer());
        assert view2.lightLobbyList.getLobbies().isEmpty();

        assert !view1.lightGame.getLightGameParty().getPlayerActiveList().get(view2.name);
        assert !view3.lightGame.getLightGameParty().getPlayerActiveList().get(view2.name);

        view2.state = null;
        controller2.login(view2.name);
        assert view2.state.equals(ViewState.CHOOSE_START_CARD);

        assert view1.lightGame.getLightGameParty().getPlayerActiveList().get(view2.name);
        assert view3.lightGame.getLightGameParty().getPlayerActiveList().get(view2.name);

        assert view2.lightGame.getLightGameParty().getGameName().equals(lobbyName1);

        assert !player2.hasPlacedStartCard();
        assert !player2.hasChosenPawnColor();
        assert !player2.hasChosenObjective();

        assert game.getGameState().equals(GameState.CHOOSE_START_CARD);
        assert !game.getGameState().equals(GameState.CHOOSE_PAWN);
        assert !game.getGameState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);

        StartCard startCard2 = player2.getUserHand().getStartCard();
        assert Arrays.stream(view2.lightGame.getHand().getCards()).toList().contains(Lightifier.lightifyToCard(startCard2));

        LightPlacement lightPlacement1 = new LightPlacement(new Position(0,0), Lightifier.lightifyToCard(player1.getUserHand().getStartCard()), CardFace.FRONT);
        controller1.place(lightPlacement1);

        LightPlacement lightPlacement4 = new LightPlacement(new Position(0,0), Lightifier.lightifyToCard(player4.getUserHand().getStartCard()), CardFace.FRONT);
        controller4.place(lightPlacement4);

        assert !lobbyGameListController.getViewMap().containsKey(view4.name);
        controller2.disconnect();
        controller4.disconnect();

        assert game.getGameState().equals(GameState.CHOOSE_START_CARD);

        LightPlacement lightPlacement3 = new LightPlacement(new Position(0,0), Lightifier.lightifyToCard(player3.getUserHand().getStartCard()), CardFace.FRONT);
        controller3.place(lightPlacement3);

        assert !game.getGameState().equals(GameState.CHOOSE_START_CARD);
        assert game.getGameState().equals(GameState.CHOOSE_PAWN);

        assert !game.getUsersList().contains(player2);
        assert game.getUsersList().contains(player4);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());
        System.out.println(gameController.gameController.getGamePlayers());
        controller2.login(view2.name);
        controller4.login(view4.name);

        assert game.getGameState().equals(GameState.CHOOSE_PAWN);
        assert !game.getUsersList().contains(player2);
        assert game.getUsersList().contains(player4);

        assert view2.state.equals(ViewState.JOIN_LOBBY);
        assert view4.state.equals(ViewState.CHOOSE_PAWN);

        assert !view1.lightGame.getLightGameParty().getPlayerActiveList().containsKey(view2.name);
        assert !view3.lightGame.getLightGameParty().getPlayerActiveList().containsKey(view2.name);
        assert !view4.lightGame.getLightGameParty().getPlayerActiveList().containsKey(view2.name);
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

        String lobbyName1 = "test1";
        String lobbyName2 = "test2";
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
        Player player1 = game.getUserFromNick(view1.name);
        Player player2 = game.getUserFromNick(view2.name);
        Player player3 = game.getUserFromNick(view3.name);
        Player player4 = game.getUserFromNick(view4.name);

        System.out.println(game.getGameParty().getCurrentPlayerIndex());

        assert gameController.getViewMap().containsKey(view2.name);
        assert game.getName().equals(lobbyName1);
        assert game.getUsersList().stream().map(Player::getNickname).toList().contains(view2.name);

        placeStartCard(player1, controller1);
        placeStartCard(player2, controller2);
        placeStartCard(player3, controller3);
        placeStartCard(player4, controller4);

        pawnChoicesSetupCorrectly(gameController, List.of(view1, view2, view3, view4));

        controller1.disconnect();
        controller2.choosePawn(PawnColors.BLUE);

        List<PawnColors> nonChosenColors = Arrays.stream(PawnColors.values()).filter(color->!color.equals(PawnColors.BLUE)).toList();
        assert game.getPawnChoices().containsAll(nonChosenColors);
        assert !game.getPawnChoices().contains(PawnColors.BLUE);
        assert player2.getPawnColor().equals(PawnColors.BLUE);

        controller1.login(view1.name);
        checkInitialization(view1, gameController);

        assert view1.lightGame.getLightGameParty().getPawnChoices().containsAll(nonChosenColors);
        assert !view1.lightGame.getLightGameParty().getPawnChoices().contains(PawnColors.BLUE);

        controller3.choosePawn(PawnColors.RED);

        controller1.disconnect();
        controller2.disconnect();

        controller4.choosePawn(PawnColors.GREEN);

        controller1.login(view1.name);
        controller2.login(view2.name);

        assert view1.state.equals(ViewState.JOIN_LOBBY);
        assert view2.state.equals(ViewState.SELECT_OBJECTIVE);

        assert view1.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().containsAll(lobbyGameListController.getLobbyMap().keySet());

        setupObjChoicesCorrectly(gameController, List.of(view2, view3, view4));
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

        String lobbyName1 = "test1";
        String lobbyName2 = "test2";
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
        Player player1 = game.getUserFromNick(view1.name);
        Player player2 = game.getUserFromNick(view2.name);
        Player player3 = game.getUserFromNick(view3.name);
        Player player4 = game.getUserFromNick(view4.name);

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

        controller1.disconnect();
        LightCard obj2 = Lightifier.lightifyToCard(player2.getUserHand().getSecretObjectiveChoices().getFirst());
        controller2.chooseSecretObjective(obj2);
        controller2.disconnect();

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
        controller1.disconnect();
        controller2.disconnect();
        controller4.chooseSecretObjective(Lightifier.lightifyToCard(player4.getUserHand().getSecretObjectiveChoices().getFirst()));

        game.getUsersList().forEach(user->{
            System.out.println(user.getNickname());
            System.out.println(user.getUserHand().getSecretObjective());
            System.out.println(user.getUserHand().getSecretObjectiveChoices());
        });

        assert !game.getGameState().equals(GameState.CHOOSE_START_CARD);
        assert !game.getGameState().equals(GameState.CHOOSE_PAWN);
        assert !game.getGameState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);

        controller1.login(view1.name);
        controller2.login(view2.name);

        assert view1.state.equals(ViewState.JOIN_LOBBY);
        setupActualGame(gameController, List.of(view2, view3, view4));
    }

    @Test
    void leaveAndRejoinActualGameIdle(){

    }

    @Test
    void leaveAndRejoinActualGamePlaceCard(){}

    @Test
    void leaveAndRejoinActualGameDrawCard(){}

    @Test
    void leaveAndRejoinActualGameEnding(){}

    private void checkInitialization(ViewTest view, PublicGameController gameController){
        Game game = gameController.getGame();
        assert view.lightLobbyList.getLobbies().isEmpty();

        assert view.lightLobby.name().isEmpty();
        assert view.lightLobby.numberMaxPlayer() == 0;
        assert view.lightLobby.nicknames().isEmpty();

        assert view.lightGame.getLightGameParty().getGameName().equals(game.getName());

        List<String> playerNick = gameController.gameController.getPlayerViewMap().keySet().stream().toList();
        List<String> playerNicksOnView = view.lightGame.getLightGameParty().getPlayerActiveList().keySet().stream().toList();
        System.out.println(playerNick);
        System.out.println(playerNicksOnView);
        assert playerNicksOnView.containsAll(playerNick);
        assert playerNick.containsAll(playerNicksOnView);
    }

    private void pawnChoicesSetupCorrectly(PublicGameController gameController, List<ViewTest> playerViews){
        Game game = gameController.getGame();

        assert game.getGameState().equals(GameState.CHOOSE_PAWN);
        assert !game.getGameState().equals(GameState.CHOOSE_START_CARD);
        assert !game.getGameState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);
        assert !game.getGameState().equals(GameState.END_GAME);

        assert game.getPawnChoices().containsAll(Arrays.stream(PawnColors.values()).toList());

        game.getUsersList().forEach(user->{
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

        assert game.getGameState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);
        assert !game.getGameState().equals(GameState.CHOOSE_PAWN);
        assert !game.getGameState().equals(GameState.CHOOSE_START_CARD);
        assert !game.getGameState().equals(GameState.END_GAME);

        List<String> nicksToCheck = playerViews.stream().map(view->view.name).toList();
        List<Player> usersToCheck = game.getUsersList().stream().filter(user->nicksToCheck.contains(user.getNickname())).toList();

        usersToCheck.forEach(user->{
            System.out.println(user.getNickname());
            assert user.hasPlacedStartCard();
            assert user.hasChosenPawnColor();
            assert !user.hasChosenObjective();
            List<ObjectiveCard> objOptions = user.getUserHand().getSecretObjectiveChoices();

            System.out.println(user.getUserHand().getSecretObjective());
            assert user.getUserHand().getSecretObjective() == null;
            Assertions.assertNotNull(objOptions);
            assert !objOptions.isEmpty();
            assert objOptions.size() == 2;
        });

        playerViews.forEach(view -> {
            System.out.println(view.name);
            List<ObjectiveCard> objOptions = game.getUserFromNick(view.name).getUserHand().getSecretObjectiveChoices();
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

        assert !game.getGameState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);
        assert !game.getGameState().equals(GameState.CHOOSE_PAWN);
        assert !game.getGameState().equals(GameState.CHOOSE_START_CARD);
        assert !game.getGameState().equals(GameState.END_GAME);

        List<String> nicksToCheck = playerViews.stream().map(view->view.name).toList();
        List<Player> usersToCheck = game.getUsersList().stream().filter(user->nicksToCheck.contains(user.getNickname())).toList();
        List<String> allUserInGameNick = game.getUsersList().stream().map(Player::getNickname).toList();

        usersToCheck.forEach(user->{
            System.out.println(user.getNickname());
            assert user.hasPlacedStartCard();
            assert user.hasChosenPawnColor();
            assert user.hasChosenObjective();

            assert user.getUserHand().getHand().stream().allMatch(Objects::nonNull);
            assert user.getUserHand().getHand().size() == 3;
        });

        playerViews.forEach(view -> {
            System.out.println(view.name);
            Player player = game.getUserFromNick(view.name);

            List<LightCard> yourHandOnView = Arrays.stream(view.lightGame.getHand().getCards()).toList();
            List<LightCard> yourHandOnModel = player.getUserHand().getHand().stream().map(Lightifier::lightifyToCard).toList();
            assert yourHandOnModel.containsAll(yourHandOnView);
            assert yourHandOnView.containsAll(yourHandOnModel);

            for(String nick : allUserInGameNick){
                if(!nick.equals(view.name)) {
                    System.out.println(view.name + ":" + nick);
                    List<LightBack> otherHandOnView = Arrays.stream(view.lightGame.getHandOthers().get(nick).getCards()).toList();
                    List<LightBack> otherHandOnModel = game.getUserFromNick(nick).getUserHand().getHand().stream().map(Lightifier::lightifyToBack).toList();
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
