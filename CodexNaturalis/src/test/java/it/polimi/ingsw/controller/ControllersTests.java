package it.polimi.ingsw.controller;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.lightModel.Heavifier;
import it.polimi.ingsw.utils.OSRelated;
import it.polimi.ingsw.controller.PublicController.*;
import it.polimi.ingsw.controller.PublicModelClass.PublicGame;
import it.polimi.ingsw.controller.persistence.PersistenceFactory;
import it.polimi.ingsw.lightModel.LightModelConfig;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.ViewTest;
import it.polimi.ingsw.lightModel.lightPlayerRelated.*;
import it.polimi.ingsw.lightModel.lightTableRelated.LightDeck;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.CollectableCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.utilityEnums.*;
import it.polimi.ingsw.model.playerReleted.*;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.GameState;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewState;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.*;

class ControllersTests {
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
    public void setUp() {
        persistenceFactory.eraseAllSaves();

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
    void loginJoiningLobbyList() {
        ViewTest view1 = new ViewTest();
        view1.name = "pippo";
        ViewTest view2 = new ViewTest();
        view2.name = "pluto";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        //view joins before adding lobbies
        controller1.login(view1.name);
        System.out.println(view1.state);
        //add lobby
        Lobby lobby = new Lobby(2, "loginJoiningLobbyList");
        controller1.createLobby(lobby.getLobbyName(), lobby.getNumberOfMaxPlayer());
        System.out.println(view1.state);
        //view joins after adding lobbies
        controller2.login(view2.name);
        System.out.println(view1.state);

        System.out.println(view1.name);
        assert !view1.lightLobbyList.getLobbies().contains(Lightifier.lightify(lobby));
        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
        System.out.println(view1.state);
        assert view1.state.equals(ViewState.LOBBY);
        assert view2.lightLobbyList.getLobbies().contains(Lightifier.lightify(lobby));
        assert lobbyGameListController.getViewMap().containsKey(view2.name);
        assert view2.state.equals(ViewState.JOIN_LOBBY);

    }

    @Test
    void createLobby() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);

        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "mickey";
        String lobbyName1 = "createLobby1";
        String lobbyName2 = "createLobby2";

        controller1.login(view1.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.login(view2.name);
        controller3.login(view3.name);
        controller2.createLobby(lobbyName2, 2);

        assert lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getLobbyMap().containsKey(lobbyName2);
        assert view1.lightLobbyList.getLobbies().isEmpty();
        assert view2.lightLobbyList.getLobbies().isEmpty();
        assert view3.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().contains(lobbyName1);
        assert view3.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().contains(lobbyName2);
    }

    @Test
    void joinLobby() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        view1.name = "pippo";
        view2.name = "pluto";
        String lobbyName1 = "joinLobby";
        controller1.login(view1.name);
        controller1.createLobby(lobbyName1, 3);
        controller2.login(view2.name);
        controller2.joinLobby(lobbyName1);

        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
        assert lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getLobbyMap().get(lobbyName1).getLobby().getLobbyPlayerList().contains(view1.name);
        assert lobbyGameListController.getLobbyMap().get(lobbyName1).getLobby().getLobbyPlayerList().contains(view2.name);
        assert lobbyGameListController.getLobbyMap().get(lobbyName1).getLobby().getLobbyPlayerList().size() == 2;

        for(ViewTest view : Arrays.stream(new ViewTest[]{view1, view2}).toList()){
            System.out.println(view.name);
            assert view.lightLobbyList.getLobbies().isEmpty();
            assert view.lightLobby.nicknames().contains(view1.name);
            assert view.lightLobby.nicknames().contains(view2.name);
            System.out.println(view.lightLobby.nicknames());
            assert view.lightLobby.nicknames().size() == 2;
            assert view.lightLobby.numberMaxPlayer() == 3;
            assert view.lightLobby.name().equals(lobbyName1);
            assert view.state.equals(ViewState.LOBBY);
        }
    }

    @Test
    void joinLobbyAndStartGame(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "mickey";
        String lobbyName1 = "joinLobbyAndStartGame1";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);

        controller3.login(view3.name);
        controller1.login(view1.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.login(view2.name);
        controller2.joinLobby(lobbyName1);

        //game created and created correctly
        assert !lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        assert lobbyGameListController.getGameMap().containsKey(lobbyName1);

        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        PublicGameController publicController = new PublicGameController(gameController);
        Game game = publicController.getGame();
        assert game.getGameParty().getNumberOfMaxPlayer() == 2;
        assert game.getGameParty().getPlayersList().stream().map(Player::getNickname).toList().contains(view1.name);
        assert game.getGameParty().getPlayersList().stream().map(Player::getNickname).toList().contains(view2.name);
        assert game.getGameParty().getPlayersList().size() == 2;
        assert game.getGameParty().getCurrentPlayer() != null;
        assert game.getName().equals(lobbyName1);
        assert game.getGoldCardDeck() != null;
        assert game.getResourceCardDeck() != null;
        assert game.getStartingCardDeck() != null;
        assert game.getObjectiveCardDeck() != null;
        //user have a start card in hand
        assert game.getGameParty().getPlayersList().stream().map(Player::getUserHand).map(Hand::getStartCard).allMatch(Objects::nonNull);

        //lobby removed from lobbyList
        assert !view3.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().contains(lobbyName1);

        //lightLobbyList on view is empty
        assert view1.lightLobbyList.getLobbies().isEmpty();
        //lobbyList on view has been reset
        assert view1.lightLobby.nicknames().isEmpty();
        assert view1.lightLobby.numberMaxPlayer() == 0;
        assert view1.lightLobby.name().isEmpty();
        //gameParty correctly set
        assert view1.lightGame.getLightGameParty().getGameName().equals(lobbyName1);
        assert view1.lightGame.getLightGameParty().getPlayerActiveMap().get(view1.name) != null;
        assert view1.lightGame.getLightGameParty().getPlayerActiveMap().get(view2.name) != null;
        assert view1.lightGame.getLightGameParty().getPlayerActiveMap().size() == 2;
        assert view1.lightGame.getLightGameParty().getYourName().equals(view1.name);
        assert view1.lightGame.getLightGameParty().getCurrentPlayer() != null;
        assert view1.lightGame.getLightGameParty().getCurrentPlayer().equals(game.getGameParty().getCurrentPlayer().getNickname());
        //codex correctly set
        assert view1.lightGame.getCodexMap().keySet().size() == 2;
        assert view1.lightGame.getCodexMap().values().size() == 2;
        assert view1.lightGame.getCodexMap().values().stream().allMatch(lc -> lc.getPoints() == 0);
        assert view1.lightGame.getCodexMap().values().stream().allMatch(lc -> lc.getEarnedCollectables().values().stream().allMatch(i -> i == 0));
        assert view1.lightGame.getCodexMap().values().stream().allMatch(lc -> lc.getFrontier().frontier().contains(new Position(0,0)));
        assert view1.lightGame.getCodexMap().values().stream().allMatch(lc -> lc.getPlacementHistory().isEmpty());
        //hand correctly set
        assert Arrays.stream(view1.lightGame.getHand().getSecretObjectiveOptions()).allMatch(Objects::isNull);
        assert view1.lightGame.getHand().getSecretObjective() == null;
        assert !Arrays.stream(view1.lightGame.getHand().getCards()).allMatch(Objects::isNull);
        assert view1.lightGame.getHand().getCards().length == 3;
        assert Arrays.stream(view1.lightGame.getHand().getCards()).toList().containsAll(Arrays.asList(null, null));
        LightCard cardInHandThatIsNotNull = Arrays.stream(view1.lightGame.getHand().getCards()).filter(Objects::nonNull).toList().getFirst();
        assert cardInHandThatIsNotNull != null;
        assert cardInHandThatIsNotNull.idFront() == game.getPlayerFromNick(view1.name).getUserHand().getStartCard().getIdFront();
        assert cardInHandThatIsNotNull.idBack() == game.getPlayerFromNick(view1.name).getUserHand().getStartCard().getIdBack();
        assert view1.lightGame.getHand().getCardPlayability().values().size() == 1;
        Assertions.assertTrue(view1.lightGame.getHand().getCardPlayability().get(cardInHandThatIsNotNull));
        //handOthers correctly set
        assert view1.lightGame.getHandOthers().values().size() == 1;
        assert view1.lightGame.getHandOthers().get(view1.name) == null;
        assert view1.lightGame.getHandOthers().get(view2.name) != null;
        assert view1.lightGame.getHandOthers().get(view2.name).getCards().length == 3;
        assert Arrays.stream(view1.lightGame.getHandOthers().get(view2.name).getCards()).allMatch(Objects::isNull);
        //decks correctly set
        assert view1.lightGame.getDecks().keySet().size() == 2;
        assert view1.lightGame.getDecks().values().stream().allMatch(Objects::nonNull);

        LightDeck goldDeck = view1.lightGame.getGoldDeck();
        assert goldDeck.getDeckBack().idBack() == Objects.requireNonNull(game.getGoldCardDeck().showTopCardOfDeck()).getIdBack();
        assert goldDeck.getCardBuffer().length == 2;
        System.out.println(game.getGoldCardDeck().getBuffer());
        assert Arrays.stream(goldDeck.getCardBuffer()).allMatch(Objects::nonNull);
        assert game.getGoldCardDeck().getBuffer().stream().map(Card::getIdBack).toList().containsAll(Arrays.stream(goldDeck.getCardBuffer()).map(LightCard::idBack).toList());
        assert game.getGoldCardDeck().getBuffer().stream().map(Card::getIdFront).toList().containsAll(Arrays.stream(goldDeck.getCardBuffer()).map(LightCard::idFront).toList());
        LightDeck resourceDeck = view1.lightGame.getResourceDeck();
        assert resourceDeck.getDeckBack().idBack() == Objects.requireNonNull(game.getResourceCardDeck().showTopCardOfDeck()).getIdBack();
        assert resourceDeck.getCardBuffer().length == 2;
        assert Arrays.stream(resourceDeck.getCardBuffer()).allMatch(Objects::nonNull);
        assert game.getResourceCardDeck().getBuffer().stream().map(Card::getIdBack).toList().containsAll(Arrays.stream(resourceDeck.getCardBuffer()).map(LightCard::idBack).toList());
        assert game.getResourceCardDeck().getBuffer().stream().map(Card::getIdFront).toList().containsAll(Arrays.stream(resourceDeck.getCardBuffer()).map(LightCard::idFront).toList());
        //public objective are empty
        assert view1.lightGame.getPublicObjective().length == 2;
        System.out.println(Arrays.toString(view1.lightGame.getPublicObjective()));
        assert Arrays.stream(view1.lightGame.getPublicObjective()).allMatch(Objects::isNull);
        //winners are empty
        assert view1.lightGame.getWinners().isEmpty();

        //lightLobbyList on view is empty
        assert view2.lightLobbyList.getLobbies().isEmpty();
        //lobbyList on view has been reset
        assert view2.lightLobby.nicknames().isEmpty();
        assert view2.lightLobby.numberMaxPlayer() == 0;
        assert view2.lightLobby.name().isEmpty();
        //gameParty correctly set
        assert view2.lightGame.getLightGameParty().getGameName().equals(lobbyName1);
        assert view2.lightGame.getLightGameParty().getPlayerActiveMap().get(view1.name) != null;
        assert view2.lightGame.getLightGameParty().getPlayerActiveMap().get(view2.name) != null;
        assert view2.lightGame.getLightGameParty().getPlayerActiveMap().size() == 2;
        assert view2.lightGame.getLightGameParty().getYourName().equals(view2.name);
        assert view2.lightGame.getLightGameParty().getCurrentPlayer() != null;
        assert view2.lightGame.getLightGameParty().getCurrentPlayer().equals(game.getGameParty().getCurrentPlayer().getNickname());
        //codex correctly set
        assert view2.lightGame.getCodexMap().keySet().size() == 2;
        assert view2.lightGame.getCodexMap().values().size() == 2;
        assert view2.lightGame.getCodexMap().values().stream().allMatch(lc -> lc.getPoints() == 0);
        assert view2.lightGame.getCodexMap().values().stream().allMatch(lc -> lc.getEarnedCollectables().values().stream().allMatch(i -> i == 0));
        assert view2.lightGame.getCodexMap().values().stream().allMatch(lc -> lc.getFrontier().frontier().contains(new Position(0,0)));
        assert view2.lightGame.getCodexMap().values().stream().allMatch(lc -> lc.getPlacementHistory().isEmpty());
        //hand correctly set
        assert Arrays.stream(view2.lightGame.getHand().getSecretObjectiveOptions()).allMatch(Objects::isNull);
        assert view2.lightGame.getHand().getSecretObjective() == null;
        assert !Arrays.stream(view2.lightGame.getHand().getCards()).allMatch(Objects::isNull);
        assert view2.lightGame.getHand().getCards().length == 3;
        assert Arrays.stream(view2.lightGame.getHand().getCards()).toList().containsAll(Arrays.asList(null, null));
        cardInHandThatIsNotNull = Arrays.stream(view2.lightGame.getHand().getCards()).filter(Objects::nonNull).toList().getFirst();
        assert cardInHandThatIsNotNull != null;
        assert cardInHandThatIsNotNull.idFront() == game.getPlayerFromNick(view2.name).getUserHand().getStartCard().getIdFront();
        assert cardInHandThatIsNotNull.idBack() == game.getPlayerFromNick(view2.name).getUserHand().getStartCard().getIdBack();
        assert view2.lightGame.getHand().getCardPlayability().values().size() == 1;
        Assertions.assertTrue(view2.lightGame.getHand().getCardPlayability().get(cardInHandThatIsNotNull));
        //handOthers correctly set
        assert view2.lightGame.getHandOthers().values().size() == 1;
        assert view2.lightGame.getHandOthers().get(view2.name) == null;
        assert view2.lightGame.getHandOthers().get(view1.name) != null;
        assert view2.lightGame.getHandOthers().get(view1.name).getCards().length == 3;
        assert Arrays.stream(view2.lightGame.getHandOthers().get(view1.name).getCards()).allMatch(Objects::isNull);
        //decks correctly set
        assert view2.lightGame.getDecks().keySet().size() == 2;
        assert view2.lightGame.getDecks().values().stream().allMatch(Objects::nonNull);

        goldDeck = view2.lightGame.getGoldDeck();
        assert goldDeck.getDeckBack().idBack() == Objects.requireNonNull(game.getGoldCardDeck().showTopCardOfDeck()).getIdBack();
        assert goldDeck.getCardBuffer().length == 2;
        assert Arrays.stream(goldDeck.getCardBuffer()).allMatch(Objects::nonNull);
        assert game.getGoldCardDeck().getBuffer().stream().map(Card::getIdBack).toList().containsAll(Arrays.stream(goldDeck.getCardBuffer()).map(LightCard::idBack).toList());
        assert game.getGoldCardDeck().getBuffer().stream().map(Card::getIdFront).toList().containsAll(Arrays.stream(goldDeck.getCardBuffer()).map(LightCard::idFront).toList());
        resourceDeck = view2.lightGame.getResourceDeck();
        assert resourceDeck.getDeckBack().idBack() == Objects.requireNonNull(game.getResourceCardDeck().showTopCardOfDeck()).getIdBack();
        assert resourceDeck.getCardBuffer().length == 2;
        assert Arrays.stream(resourceDeck.getCardBuffer()).allMatch(Objects::nonNull);
        assert game.getResourceCardDeck().getBuffer().stream().map(Card::getIdBack).toList().containsAll(Arrays.stream(resourceDeck.getCardBuffer()).map(LightCard::idBack).toList());
        assert game.getResourceCardDeck().getBuffer().stream().map(Card::getIdFront).toList().containsAll(Arrays.stream(resourceDeck.getCardBuffer()).map(LightCard::idFront).toList());
        //public objective are empty
        assert view2.lightGame.getPublicObjective().length == 2;
        System.out.println(Arrays.toString(view2.lightGame.getPublicObjective()));
        assert Arrays.stream(view2.lightGame.getPublicObjective()).allMatch(Objects::isNull);
        //winners are empty
        assert view2.lightGame.getWinners().isEmpty();
    }


    @Test
    void leaveLobby() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "mickey";
        view4.name = "duck";
        String lobbyName1 = "leaveLobby1";
        String lobbyName2 = "leaveLobby2";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);
        Controller controller4 = new Controller(realLobbyGameListController, view4);

        controller1.login(view1.name);
        controller1.createLobby(lobbyName1, 3);
        controller2.login(view2.name);
        controller2.joinLobby(lobbyName1);
        controller2.leaveLobby();
        controller3.login(view3.name);
        controller3.joinLobby(lobbyName1);
        controller4.login(view4.name);
        controller4.createLobby(lobbyName2, 2);
        controller4.leaveLobby();

        assert view1.lightLobbyList.getLobbies().isEmpty();
        assert view2.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().contains(lobbyName1);
        assert !view2.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().contains(lobbyName2);
        assert view3.lightLobbyList.getLobbies().isEmpty();
        assert view4.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().contains(lobbyName1);
        assert !view4.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().contains(lobbyName2);

        assert view1.lightLobby.nicknames().contains(view1.name);
        assert !view1.lightLobby.nicknames().contains(view2.name);
        assert view1.lightLobby.nicknames().contains(view3.name);
        assert view1.lightLobby.numberMaxPlayer() == 3;
        assert Objects.equals(view1.lightLobby.name(), lobbyName1);

        assert view3.lightLobby.nicknames().contains(view1.name);
        assert !view3.lightLobby.nicknames().contains(view2.name);
        assert view3.lightLobby.nicknames().contains(view3.name);
        assert view3.lightLobby.numberMaxPlayer() == 3;
        assert Objects.equals(view3.lightLobby.name(), lobbyName1);

        assert view2.lightLobby.nicknames().isEmpty();
        assert view2.lightLobby.numberMaxPlayer() == LightModelConfig.defaultNumberMaxPlayer;
        assert Objects.equals(view2.lightLobby.name(), LightModelConfig.defaultLobbyName);

        assert view4.lightLobby.nicknames().isEmpty();
        assert view4.lightLobby.numberMaxPlayer() == LightModelConfig.defaultNumberMaxPlayer;
        assert Objects.equals(view4.lightLobby.name(), LightModelConfig.defaultLobbyName);

        LobbyController lobbyController = lobbyGameListController.getLobbyMap().get(lobbyName1);
        PublicLobbyController publicController = new PublicLobbyController(lobbyController);
        Lobby lobby1 = publicController.getLobby();

        assert lobby1.getLobbyPlayerList().contains(view1.name);
        assert lobby1.getLobbyPlayerList().contains(view3.name);
        assert !lobby1.getLobbyPlayerList().contains(view2.name);
        assert lobbyGameListController.getGameMap().get(lobbyName2) == null;
    }

    @Test
    void selectStartCardFace() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        String lobbyName1 = "selectStartCardFace";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.login(view2.name);
        controller2.joinLobby(lobbyName1);
        //game started
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller1.place(startPlacement);

        //check model
        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        PublicGameController publicController = new PublicGameController(gameController);
        Game game = publicController.getGame();

        assert game != null;
        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);
        assert game.getState().equals(GameState.CHOOSE_START_CARD);

        //user1
        assert player1.getState().equals(PlayerState.WAIT);
        assert player1.getUserCodex().getPlacementAt(new Position(0,0)) != null;
        assert player1.getUserHand().getHand().stream().allMatch(Objects::nonNull);
        assert player1.getUserHand().getStartCard() == null;
        //user2
        assert player2.getState().equals(PlayerState.CHOOSE_START_CARD);
        assert player2.getUserHand().getHand().stream().allMatch(Objects::isNull);
        assert player2.getUserHand().getStartCard() != null;

        //view 1 lightModel updated correctly
        assert view1.lightGame.getCodexMap().get(view1.name).getPlacementHistory().contains(startPlacement);
        assert Arrays.stream(view1.lightGame.getHand().getCards()).allMatch(Objects::isNull);
        assert Arrays.stream(view1.lightGame.getHand().getSecretObjectiveOptions()).allMatch(Objects::isNull);
        assert Arrays.stream(view1.lightGame.getHandOthers().get(view2.name).getCards()).allMatch(Objects::isNull);
        //view 2 lightModel isn't updated
        assert view2.lightGame.getCodexMap().get(view1.name).getPlacementHistory().isEmpty();
        assert Arrays.stream(view2.lightGame.getHandOthers().get(view1.name).getCards()).allMatch(Objects::isNull);
    }

    @Test
    void lastPlayerSelectStartCard(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        String lobbyName1 = "lastPlayerSelectStartCard";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.login(view2.name);
        controller2.joinLobby(lobbyName1);
        //game started
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller1.place(startPlacement1);
        startCard = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller2.place(startPlacement2);

        //check model
        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        PublicGameController publicController = new PublicGameController(gameController);
        Game game = publicController.getGame();

        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);
        Hand hand1 = player1.getUserHand();
        Hand hand2 = player2.getUserHand();

        //user1
        assert player1.getState().equals(PlayerState.CHOOSE_PAWN);
        Assertions.assertNull(player1.getUserHand().getSecretObjectiveChoices());
        //user2
        assert player2.getState().equals(PlayerState.CHOOSE_PAWN);;
        Assertions.assertNull(player2.getUserHand().getSecretObjectiveChoices());

        //view 1 lightModel updated correctly
        LightHand lightHand1 = view1.lightGame.getHand();
        List<LightCard> cardsOnClient1 = Arrays.stream(lightHand1.getCards()).toList();
        List<LightCard> cardsOnServer1 = hand1.getHand().stream().map(Lightifier::lightifyToCard).toList();


        assert view1.lightGame.getCodexMap().get(view2.name).getPlacementHistory().isEmpty();
        assert cardsOnServer1.stream().allMatch(Objects::isNull);
        assert cardsOnClient1.stream().anyMatch(Objects::isNull);
        assert Arrays.stream(view1.lightGame.getHandOthers().get(view2.name).getCards()).allMatch(Objects::isNull);

        int resourceActualDeckOnViewBackID1 = view1.lightGame.getResourceDeck().getDeckBack().idBack();
        int goldActualDeckBackOnViewID1 = view1.lightGame.getGoldDeck().getDeckBack().idBack();
        List<Integer> resourceDeckBufferOnViewID1 = Arrays.stream(view1.lightGame.getResourceDeck().getCardBuffer()).map(LightCard::idFront).toList();
        List<Integer> goldDeckBufferOnViewID1 = Arrays.stream(view1.lightGame.getGoldDeck().getCardBuffer()).map(LightCard::idFront).toList();

        assert resourceActualDeckOnViewBackID1 == game.getResourceCardDeck().showTopCardOfDeck().getIdBack();
        assert goldActualDeckBackOnViewID1 == game.getGoldCardDeck().showTopCardOfDeck().getIdBack();
        assert resourceDeckBufferOnViewID1.containsAll(game.getResourceCardDeck().getBuffer().stream().map(Card::getIdFront).toList());
        assert goldDeckBufferOnViewID1.containsAll(game.getGoldCardDeck().getBuffer().stream().map(Card::getIdFront).toList());

        Assertions.assertNull(view1.lightGame.getHand().getSecretObjectiveOptions()[0]);
        Assertions.assertNull(view1.lightGame.getHand().getSecretObjectiveOptions()[1]);

        //view 2 lightModel updated correctly
        LightHand lightHand2 = view2.lightGame.getHand();

        List<LightCard> cardsOnClient2 = Arrays.stream(lightHand2.getCards()).toList();
        List<LightCard> cardsOnServer2 = hand2.getHand().stream().map(Lightifier::lightifyToCard).toList();

        assert view2.lightGame.getCodexMap().get(view1.name).getPlacementHistory().isEmpty();
        assert cardsOnServer2.stream().allMatch(Objects::isNull);
        assert cardsOnClient2.stream().anyMatch(Objects::isNull);
        assert Arrays.stream(view2.lightGame.getHandOthers().get(view1.name).getCards()).allMatch(Objects::isNull);


        Assertions.assertNull(view2.lightGame.getHand().getSecretObjectiveOptions()[0]);
        Assertions.assertNull(view2.lightGame.getHand().getSecretObjectiveOptions()[1]);

        assert Arrays.stream(view2.lightGame.getHandOthers().get(view1.name).getCards()).allMatch(Objects::isNull);
        assert !view2.lightGame.getCodexMap().get(view1.name).getPlacementHistory().contains(startPlacement1);

        //pawns arrives correctly
        assert game.getPawnChoices().containsAll(Arrays.stream(PawnColors.values()).toList());
        Assertions.assertNull(player1.getPawnColor());
        Assertions.assertNull(player2.getPawnColor());

        assert view1.lightGame.getLightGameParty().getPlayersColor().values().isEmpty();
    }

    @Test
    void choosePawn(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        String lobbyName1 = "choosePawn";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.login(view2.name);
        controller2.joinLobby(lobbyName1);
        //game started
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller1.place(startPlacement1);
        startCard = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller2.place(startPlacement2);
        controller1.choosePawn(PawnColors.BLUE);

        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        PublicGameController publicController = new PublicGameController(gameController);
        Game game = publicController.getGame();

        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);

        assert !game.getPawnChoices().contains(PawnColors.BLUE);
        assert player1.getPawnColor() == PawnColors.BLUE;
        assert player2.getPawnColor() == null;

        //view 1 lightModel updated correctly
        assert view1.lightGame.getLightGameParty().getPlayersColor().get(view1.name) == PawnColors.BLUE;
        assert view1.lightGame.getLightGameParty().getPlayersColor().get(view2.name) == null;

        //view 2 lightModel updated correctly
        assert view2.lightGame.getLightGameParty().getPlayersColor().get(view1.name) == PawnColors.BLUE;
        assert view2.lightGame.getLightGameParty().getPlayersColor().get(view2.name) == null;

        List<LightCard> handOnView = Arrays.stream(view1.lightGame.getHand().getCards()).toList();
        List<LightCard> handOnServer = player1.getUserHand().getHand().stream().map(Lightifier::lightifyToCard).toList();

        assert handOnServer.containsAll(handOnView);
        assert handOnView.containsAll(handOnServer);

        Deck<GoldCard> goldCardDeckOnServer = game.getGoldCardDeck();
        Deck<ResourceCard> resourceCardDeckOnServer = game.getResourceCardDeck();
        Map<DrawableCard, LightDeck> deckMapOnView1 = view1.lightGame.getDecks();
        Map<DrawableCard, LightDeck> deckMapOnView2 = view2.lightGame.getDecks();

        assert deckMapOnView1.get(DrawableCard.GOLDCARD).getDeckBack().idBack() == goldCardDeckOnServer.showTopCardOfDeck().getIdBack();
        assert deckMapOnView1.get(DrawableCard.RESOURCECARD).getDeckBack().idBack() == resourceCardDeckOnServer.showTopCardOfDeck().getIdBack();

        assert deckMapOnView2.get(DrawableCard.GOLDCARD).getDeckBack().idBack() == goldCardDeckOnServer.showTopCardOfDeck().getIdBack();
        assert deckMapOnView2.get(DrawableCard.RESOURCECARD).getDeckBack().idBack() == resourceCardDeckOnServer.showTopCardOfDeck().getIdBack();
    }

    @Test
    void lastPlayerChoosePawn(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        String lobbyName1 = "lastPlayerChoosePawn";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.login(view2.name);
        controller2.joinLobby(lobbyName1);
        //game started
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller1.place(startPlacement1);
        startCard = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller2.place(startPlacement2);
        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.RED);

        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        PublicGameController publicController = new PublicGameController(gameController);
        Game game = publicController.getGame();

        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);

        //pawnChoices setUp
        assert player1.getPawnColor() == PawnColors.BLUE;
        assert player2.getPawnColor() == PawnColors.RED;

        //view 1 lightModel updated correctly
        assert view1.lightGame.getLightGameParty().getPlayersColor().get(view1.name) == PawnColors.BLUE;
        assert view1.lightGame.getLightGameParty().getPlayersColor().get(view2.name) == PawnColors.RED;

        //view 2 lightModel updated correctly
        assert view2.lightGame.getLightGameParty().getPlayersColor().get(view1.name) == PawnColors.BLUE;
        assert view2.lightGame.getLightGameParty().getPlayersColor().get(view2.name) == PawnColors.RED;

        //check transition to secret objective choice state
        assert game.getPawnChoices().isEmpty();

        assert player1.getState().equals(PlayerState.CHOOSE_SECRET_OBJECTIVE);
        assert player2.getState().equals(PlayerState.CHOOSE_SECRET_OBJECTIVE);

        assert player1.getUserHand().getSecretObjectiveChoices().stream().allMatch(Objects::nonNull);
        assert player2.getUserHand().getSecretObjectiveChoices().stream().allMatch(Objects::nonNull);
        assert player1.getUserHand().getSecretObjectiveChoices().size() == 2;
        assert player2.getUserHand().getSecretObjectiveChoices().size() == 2;

        assert view1.lightGame.getHand().getSecretObjectiveOptions().length == 2;
        assert view2.lightGame.getHand().getSecretObjectiveOptions().length == 2;
    }

    @Test
    void choseSecretObjective() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        String lobbyName1 = "choseSecretObjective";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.login(view2.name);
        controller2.joinLobby(lobbyName1);
        //choose StartCard
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller1.place(startPlacement1);
        startCard = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller2.place(startPlacement2);
        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.RED);
        //choose secret objective
        LightCard secretObjective = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller1.chooseSecretObjective(secretObjective);

        //model
        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        PublicGameController publicController = new PublicGameController(gameController);
        Game game = publicController.getGame();

        Player player1 = game.getPlayerFromNick(view1.name);
        Hand hand1 = player1.getUserHand();
        Player player2 = game.getPlayerFromNick(view2.name);

        //user1
        assert player1.getState().equals(PlayerState.WAIT);
        assert hand1.getSecretObjective() != null;
        assert hand1.getSecretObjective().getIdFront() == secretObjective.idFront();
        assert hand1.getSecretObjective().getIdBack() == secretObjective.idBack();
        assert hand1.getSecretObjectiveChoices() == null;

        //user2
        assert player2.getState().equals(PlayerState.CHOOSE_SECRET_OBJECTIVE);

        LightHand lightHand = view1.lightGame.getHand();
        //view 1 lightModel updated correctly
        assert lightHand.getSecretObjective() != null;
        assert lightHand.getSecretObjective().idFront() == secretObjective.idFront();
        assert lightHand.getSecretObjective().idBack() == secretObjective.idBack();
        assert Arrays.stream(lightHand.getSecretObjectiveOptions()).allMatch(Objects::isNull);
        //view 2 lightModel updated correctly
        lightHand = view2.lightGame.getHand();
        assert lightHand.getSecretObjective() == null;
        assert Arrays.stream(lightHand.getSecretObjectiveOptions()).allMatch(Objects::nonNull);
    }

    @Test
    void lastToChoseSecretObjective() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        String lobbyName1 = "lastToChoseSecretObjective";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);


        controller1.login(view1.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.login(view2.name);
        controller2.joinLobby(lobbyName1);
        //choose StartCard
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller1.place(startPlacement1);
        startCard = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller2.place(startPlacement2);
        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.RED);
        //choose secret objective
        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller1.chooseSecretObjective(secretObjective1);
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller2.chooseSecretObjective(secretObjective2);
        //model
        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        PublicGameController publicController = new PublicGameController(gameController);
        Game game = publicController.getGame();

        Player player1 = game.getPlayerFromNick(view1.name);
        Hand hand1 = player1.getUserHand();
        Player player2 = game.getPlayerFromNick(view2.name);
        Hand hand2 = player2.getUserHand();
        //player1
        assert player1.getState().equals(PlayerState.PLACE) || player1.getState().equals(PlayerState.IDLE);
        assert hand1.getSecretObjective() != null;
        assert hand1.getSecretObjective().getIdFront() == secretObjective1.idFront();
        assert hand1.getSecretObjective().getIdBack() == secretObjective1.idBack();
        assert hand1.getSecretObjectiveChoices() == null;
        //player2
        assert player2.getState().equals(PlayerState.PLACE) || player2.getState().equals(PlayerState.IDLE);
        assert hand2.getSecretObjective() != null;
        assert hand2.getSecretObjective().getIdFront() == secretObjective2.idFront();
        assert hand2.getSecretObjective().getIdBack() == secretObjective2.idBack();
        assert hand2.getSecretObjectiveChoices() == null;

        LightHand lightHand = view1.lightGame.getHand();
        //view 1 lightModel updated correctly
        assert lightHand.getSecretObjective() != null;
        assert lightHand.getSecretObjective().idFront() == secretObjective1.idFront();
        assert lightHand.getSecretObjective().idBack() == secretObjective1.idBack();
        assert Arrays.stream(lightHand.getSecretObjectiveOptions()).allMatch(Objects::isNull);
        List<LightCard> publicObjective1 = game.getCommonObjective().stream().map(Lightifier::lightifyToCard).toList();
        assert Arrays.stream(view1.lightGame.getPublicObjective()).toList().containsAll(publicObjective1);
        //view 2 lightModel updated correctly
        lightHand = view2.lightGame.getHand();
        assert lightHand.getSecretObjective() != null;
        assert lightHand.getSecretObjective().idFront() == secretObjective2.idFront();
        assert lightHand.getSecretObjective().idBack() == secretObjective2.idBack();
        assert Arrays.stream(lightHand.getSecretObjectiveOptions()).allMatch(Objects::isNull);
        List<LightCard> publicObjective2 = game.getCommonObjective().stream().map(Lightifier::lightifyToCard).toList();
        assert Arrays.stream(view2.lightGame.getPublicObjective()).toList().containsAll(publicObjective2);
    }

    @Test
    void place() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        String lobbyName1 = "place";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.login(view2.name);
        controller2.joinLobby(lobbyName1);
        //choose StartCard
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller1.place(startPlacement1);
        startCard = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller2.place(startPlacement2);
        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.RED);
        //choose secret objective
        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller1.chooseSecretObjective(secretObjective1);
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller2.chooseSecretObjective(secretObjective2);
        //place
        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        PublicGameController publicController = new PublicGameController(gameController);
        Game game = publicController.getGame();

        Controller controllerFirst = view1.name.equals(game.getCurrentPlayer().getNickname()) ? controller1 : controller2;

        Player player1 = game.getCurrentPlayer();
        assert player1.getState().equals(PlayerState.PLACE);
        CardInHand cardPlaced = player1.getUserHand().getHand().stream().filter(Objects::nonNull).toList().getFirst();
        Position position = player1.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement = new LightPlacement(position, Lightifier.lightifyToCard(cardPlaced), CardFace.BACK);
        controllerFirst.place(placement);

        //model
        assert !player1.getUserHand().getHand().contains(cardPlaced);
        assert player1.getUserCodex().getPlacementAt(position) != null;
        assert player1.getUserCodex().getPlacementAt(position).card().getIdFront() == cardPlaced.getIdFront();
        assert player1.getUserCodex().getPlacementAt(position).card().getIdBack() == cardPlaced.getIdBack();

        //lightModel1
        ViewTest viewFirst = view1.name.equals(player1.getNickname()) ? view1 : view2;
        LightCodex lightCodexFirst = viewFirst.lightGame.getCodexMap().get(viewFirst.name);
        LightHand lightHandFirst = viewFirst.lightGame.getHand();
        assert !Arrays.stream(lightHandFirst.getCards()).toList().contains(Lightifier.lightifyToCard(cardPlaced));
        assert lightCodexFirst.getPlacementHistory().contains(placement);
        assert !lightCodexFirst.getFrontier().frontier().contains(position);
        assert lightCodexFirst.getEarnedCollectables().equals(player1.getUserCodex().getEarnedCollectables());
        assert lightCodexFirst.getFrontier().frontier().equals(player1.getUserCodex().getFrontier().getFrontier());
        for(CardInHand card : player1.getUserHand().getHand()){
            assert card == null || lightHandFirst.getCardPlayability().get(Lightifier.lightifyToCard(card)) == card.canBePlaced(player1.getUserCodex());
        }
        //lightModel2
        ViewTest viewSecond = view1.name.equals(player1.getNickname()) ? view2 : view1;
        LightCodex lightCodex1on2 = viewSecond.lightGame.getCodexMap().get(viewFirst.name);
        List<LightBack> lightHand1on2 = new ArrayList<>(Arrays.stream(viewSecond.lightGame.getHandOthers().get(viewFirst.name).getCards()).toList());
        System.out.println(lightHand1on2);
        assert lightHand1on2.contains(null);
        lightHand1on2.remove(null);
        assert !lightHand1on2.contains(null);
        assert lightCodex1on2.getPlacementHistory().contains(placement);
        assert !lightCodex1on2.getFrontier().frontier().contains(position);
    }

    @Test
    void drawFromDeck() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        String lobbyName1 = "drawFromDeck";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.login(view2.name);
        controller2.joinLobby(lobbyName1);
        //choose StartCard
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller1.place(startPlacement1);
        startCard = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller2.place(startPlacement2);
        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.RED);
        //choose secret objective
        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller1.chooseSecretObjective(secretObjective1);
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller2.chooseSecretObjective(secretObjective2);
        //place
        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        PublicGameController publicController = new PublicGameController(gameController);
        Game game = publicController.getGame();

        Player firstPlayer = game.getCurrentPlayer();
        Player secondPlayer = game.getPlayerFromNick(view1.name.equals(firstPlayer.getNickname()) ? view2.name : view1.name);

        Controller firstController = view1.name.equals(firstPlayer.getNickname()) ? controller1 : controller2;
        Controller secondController = view1.name.equals(firstPlayer.getNickname()) ? controller2 : controller1;

        ViewTest firstView = view1.name.equals(firstPlayer.getNickname()) ? view1 : view2;
        ViewTest secondView = view1.name.equals(firstPlayer.getNickname()) ? view2 : view1;

        CardInHand cardPlaced = firstPlayer.getUserHand().getHand().stream().filter(Objects::nonNull).toList().getFirst();
        Position position = firstPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement = new LightPlacement(position, Lightifier.lightifyToCard(cardPlaced), CardFace.BACK);
        firstController.place(placement);
        assert firstPlayer.getUserHand().getHand().size() == 2;
        CardInHand card = game.getGoldCardDeck().showTopCardOfDeck();
        firstController.draw(DrawableCard.GOLDCARD, Configs.actualDeckPos);

        //model
        List<Integer> heavyBuffer = game.getGoldCardDeck().getBuffer().stream().map(Card::getIdFront).toList();
        assert firstPlayer.getUserHand().getHand().contains(card);
        assert game.getCurrentPlayer().equals(secondPlayer);
        assert !heavyBuffer.contains(card.getIdFront());
        assert game.getGoldCardDeck().showTopCardOfDeck().getIdFront() != card.getIdFront();
        //lightModel1
        List<Integer> lightBuffer1 = Arrays.stream(firstView.lightGame.getGoldDeck().getCardBuffer()).map(LightCard::idFront).toList();
        assert Arrays.stream(firstView.lightGame.getHand().getCards()).toList().contains(Lightifier.lightifyToCard(card));
        assert firstView.lightGame.getCurrentPlayer().equals(secondView.name);
        assert heavyBuffer.containsAll(lightBuffer1) && lightBuffer1.containsAll(heavyBuffer);
        assert firstView.lightGame.getGoldDeck().getDeckBack().idBack() == game.getGoldCardDeck().showTopCardOfDeck().getIdBack();
        //lightModel2
        List<Integer> lightBuffer2 = Arrays.stream(firstView.lightGame.getGoldDeck().getCardBuffer()).map(LightCard::idFront).toList();
        assert Arrays.stream(secondView.lightGame.getHandOthers().get(firstView.name).getCards()).toList().contains(new LightBack(card.getIdBack()));
        assert firstView.lightGame.getCurrentPlayer().equals(secondView.name);
        assert heavyBuffer.containsAll(lightBuffer2) && lightBuffer2.containsAll(heavyBuffer);
        assert secondView.lightGame.getGoldDeck().getDeckBack().idBack() == game.getGoldCardDeck().showTopCardOfDeck().getIdBack();

    }

    @Test
    void drawFromBuffer() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        String lobbyName1 = "drawFromBuffer";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.login(view2.name);
        controller2.joinLobby(lobbyName1);
        //choose StartCard
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller1.place(startPlacement1);
        startCard = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller2.place(startPlacement2);
        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.RED);
        //choose secret objective
        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller1.chooseSecretObjective(secretObjective1);
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller2.chooseSecretObjective(secretObjective2);

        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        PublicGameController publicController = new PublicGameController(gameController);
        Game game = publicController.getGame();
        //place
        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);

        Player firstToPlay = game.getCurrentPlayer().equals(player1) ? player1 : player2;
        Player secondToPlay = firstToPlay.equals(player1) ? player2 : player1;

        ViewTest firstToPlayView = firstToPlay.getNickname().equals(view1.name) ? view1 : view2;
        ViewTest secondToPlayView = secondToPlay.getNickname().equals(view1.name) ? view1 : view2;

        Controller firstToPlayController = firstToPlay.getNickname().equals(view1.name) ? controller1 : controller2;
        Controller secondToPlayController = secondToPlay.getNickname().equals(view1.name) ? controller1 : controller2;

        CardInHand cardPlaced = firstToPlay.getUserHand().getHand().stream().filter(Objects::nonNull).toList().getFirst();
        Position position = firstToPlay.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement = new LightPlacement(position, Lightifier.lightifyToCard(cardPlaced), CardFace.BACK);
        firstToPlayController.place(placement);
        CardInHand card = game.getGoldCardDeck().showCardFromBuffer(0);
        firstToPlayController.draw(DrawableCard.GOLDCARD, 0);

        //model
        List<Integer> heavyBuffer = game.getGoldCardDeck().getBuffer().stream().map(Card::getIdFront).toList();
        assert firstToPlay.getUserHand().getHand().contains(card);
        assert game.getCurrentPlayer().equals(secondToPlay);
        assert !heavyBuffer.contains(card.getIdFront());
        assert game.getGoldCardDeck().showTopCardOfDeck().getIdFront() != card.getIdFront();
        //lightModel1
        List<Integer> lightBuffer1 = Arrays.stream(firstToPlayView.lightGame.getGoldDeck().getCardBuffer()).map(LightCard::idFront).toList();
        assert Arrays.stream(firstToPlayView.lightGame.getHand().getCards()).toList().contains(Lightifier.lightifyToCard(card));
        assert firstToPlayView.lightGame.getCurrentPlayer().equals(secondToPlayView.name);
        assert heavyBuffer.containsAll(lightBuffer1) && lightBuffer1.containsAll(heavyBuffer);
        assert firstToPlayView.lightGame.getGoldDeck().getDeckBack().idBack() == game.getGoldCardDeck().showTopCardOfDeck().getIdBack();
        //lightModel2
        List<Integer> lightBuffer2 = Arrays.stream(firstToPlayView.lightGame.getGoldDeck().getCardBuffer()).map(LightCard::idFront).toList();
        assert Arrays.stream(secondToPlayView.lightGame.getHandOthers().get(firstToPlayView.name).getCards()).toList().contains(new LightBack(card.getIdBack()));
        assert firstToPlayView.lightGame.getCurrentPlayer().equals(secondToPlayView.name);
        assert heavyBuffer.containsAll(lightBuffer2) && lightBuffer2.containsAll(heavyBuffer);
        assert secondToPlayView.lightGame.getGoldDeck().getDeckBack().idBack() == game.getGoldCardDeck().showTopCardOfDeck().getIdBack();
    }

    @Test
    void completeTurn() {//TODO remove
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "g";
        view2.name = "a";

        String lobbyName1 = "completeTurn";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.joinLobby(lobbyName1);

        LightCard startCard1 = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard1, CardFace.FRONT);
        LightCard startCard2 = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard2, CardFace.FRONT);

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

        LightCard cardPlaced1 = Lightifier.lightifyToCard(firstPlayer.getUserHand().getHand().stream().toList().getFirst());
        Position position1 = firstPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement1 = new LightPlacement(position1, cardPlaced1, CardFace.BACK);

        firstPlayerController.controller.place(placement1);
        firstPlayerController.controller.draw(DrawableCard.GOLDCARD, 0);
    }

    @Test
    void gameStateTest() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";

        String lobbyName1 = "gameStateTest";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.joinLobby(lobbyName1);

        PublicGameController gameController = new PublicGameController(lobbyGameListController.getGameMap().get(lobbyName1));
        Game game = gameController.getGame();
        
        assert !game.getState().equals(GameState.END_GAME) && game.getGameParty().getPlayersList().stream().map(Player::getUserHand).map(Hand::getStartCard).anyMatch(Objects::nonNull);
        assert game.getState().equals(GameState.CHOOSE_START_CARD);
        assert (game.getState().equals(GameState.CHOOSE_START_CARD)) ==  !game.getState().equals(GameState.END_GAME) && game.getGameParty().getPlayersList().stream().map(Player::getUserHand).map(Hand::getStartCard).anyMatch(Objects::nonNull);

        LightCard startCard1 = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard1, CardFace.FRONT);
        LightCard startCard2 = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard2, CardFace.FRONT);

        controller1.place(startPlacement1);
        controller2.place(startPlacement2);

        assert !game.getState().equals(GameState.END_GAME) && !game.getState().equals(GameState.CHOOSE_START_CARD) && game.getGameParty().getPlayersList().stream().map(Player::getPawnColor).anyMatch(Objects::isNull)
                && game.getGameParty().getPawnChoices().stream().anyMatch(Objects::nonNull);
        assert game.getState().equals(GameState.CHOOSE_PAWN);
        assert (game.getState().equals(GameState.CHOOSE_PAWN)) == !game.getState().equals(GameState.END_GAME) && !game.getState().equals(GameState.CHOOSE_START_CARD) && game.getGameParty().getPlayersList().stream().map(Player::getPawnColor).anyMatch(Objects::isNull)
                && game.getGameParty().getPawnChoices().stream().anyMatch(Objects::nonNull);;

        controller1.choosePawn(PawnColors.BLUE);
        controller2.choosePawn(PawnColors.RED);

        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];

        assert game.getGameParty().getPlayersList().stream().map(Player::getUserHand).map(Hand::getSecretObjectiveChoices).anyMatch(Objects::nonNull);
        assert game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE);
        assert game.getState().equals(GameState.CHOOSE_SECRET_OBJECTIVE) == game.getGameParty().getPlayersList().stream().map(Player::getUserHand).map(Hand::getSecretObjectiveChoices).anyMatch(Objects::nonNull);

        controller1.chooseSecretObjective(secretObjective1);
        controller2.chooseSecretObjective(secretObjective2);

        assert game.getState().equals(GameState.ACTUAL_GAME);

        PublicController firstPlayerController = new PublicController(view1.name.equals(game.getCurrentPlayer().getNickname()) ? controller1 : controller2);
        PublicController secondPlayerController = new PublicController(view1.name.equals(game.getCurrentPlayer().getNickname()) ? controller2 : controller1);
        Player firstPlayer = game.getPlayerFromNick(firstPlayerController.getNickname());
        Player secondPlayer = game.getPlayerFromNick(secondPlayerController.getNickname());

        LightCard cardPlaced1 = Lightifier.lightifyToCard(firstPlayer.getUserHand().getHand().stream().toList().getFirst());
        Position position1 = firstPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement1 = new LightPlacement(position1, cardPlaced1, CardFace.BACK);

        firstPlayerController.controller.place(placement1);
        firstPlayerController.controller.draw(DrawableCard.GOLDCARD, 0);
    }

    @Test
    void playerStateTest(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";

        String lobbyName1 = "playerStateTest";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.joinLobby(lobbyName1);

        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        PublicGameController publicController = new PublicGameController(gameController);
        Game game = publicController.getGame();

        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);

        for(Player player : game.getPlayersList()){
            assert player.getState().equals(PlayerState.CHOOSE_START_CARD);
        }

        LightCard startCard1 = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard1, CardFace.FRONT);
        LightCard startCard2 = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard2, CardFace.FRONT);

        controller1.place(startPlacement1);
        assert player1.getState().equals(PlayerState.WAIT);
        controller2.place(startPlacement2);
        for(Player player : game.getPlayersList()){
            assert player.getState().equals(PlayerState.CHOOSE_PAWN);
        }

        controller2.choosePawn(PawnColors.RED);
        assert player2.getState().equals(PlayerState.WAIT);
        controller1.choosePawn(PawnColors.BLUE);
        for(Player player : game.getPlayersList()){
            assert player.getState().equals(PlayerState.CHOOSE_SECRET_OBJECTIVE);
        }

        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];

        controller1.chooseSecretObjective(secretObjective1);
        assert player1.getState().equals(PlayerState.WAIT);
        controller2.chooseSecretObjective(secretObjective2);

        PublicController firstPlayerController = new PublicController(view1.name.equals(game.getCurrentPlayer().getNickname()) ? controller1 : controller2);
        PublicController secondPlayerController = new PublicController(view1.name.equals(game.getCurrentPlayer().getNickname()) ? controller2 : controller1);
        Player firstPlayer = game.getPlayerFromNick(firstPlayerController.getNickname());
        Player secondPlayer = game.getPlayerFromNick(secondPlayerController.getNickname());

        assert firstPlayer.getState().equals(PlayerState.PLACE);
        assert secondPlayer.getState().equals(PlayerState.IDLE);

        LightCard cardPlaced1 = Lightifier.lightifyToCard(firstPlayer.getUserHand().getHand().stream().toList().getFirst());
        Position position1 = firstPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement1 = new LightPlacement(position1, cardPlaced1, CardFace.BACK);

        firstPlayerController.controller.place(placement1);

        assert firstPlayer.getState().equals(PlayerState.DRAW);
        assert secondPlayer.getState().equals(PlayerState.IDLE);

        firstPlayerController.controller.draw(DrawableCard.GOLDCARD, 0);

        assert firstPlayer.getState().equals(PlayerState.IDLE);
        assert secondPlayer.getState().equals(PlayerState.PLACE);
    }

    @Test
    void gameEndingCausePoints() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";

        String lobbyName1 = "gameEndingCausePoints";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.joinLobby(lobbyName1);

        LightCard startCard1 = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard1, CardFace.FRONT);
        LightCard startCard2 = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard2, CardFace.FRONT);

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

        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);

        PublicGame publicGame = new PublicGame(game);

        Player firstToPlay = game.getCurrentPlayer().equals(player1) ? player1 : player2;
        Player secondToPlay = firstToPlay.equals(player1) ? player2 : player1;

        ViewTest firstToPlayView = firstToPlay.getNickname().equals(view1.name) ? view1 : view2;
        ViewTest secondToPlayView = secondToPlay.getNickname().equals(view1.name) ? view1 : view2;

        Controller firstToPlayController = firstToPlay.getNickname().equals(view1.name) ? controller1 : controller2;
        Controller secondToPlayController = secondToPlay.getNickname().equals(view1.name) ? controller1 : controller2;

        view1.state = ViewState.PLACE_CARD;
        view2.state = ViewState.IDLE;

        //change public objective card
        CollectableCardPointMultiplier objMultiplier = new CollectableCardPointMultiplier(Map.of(Resource.PLANT, 3));
        ObjectiveCard plantX3 = new ObjectiveCard(96, 87, 0,objMultiplier);

        publicGame.setPublicObj(plantX3);

        assert game.getCommonObjective().contains(plantX3);

        //place a fake card that adds 100 points
        HashMap<CardCorner, Collectable> cornerMap1 = new HashMap<>();
        for (CardCorner corner : CardCorner.values()) {
            cornerMap1.put(corner, Resource.PLANT);
        }
        ResourceCard exodiaTheForbidden = new ResourceCard(1, 1, Resource.PLANT, 100, cornerMap1);
        Placement exodiaPlacement = new Placement(new Position(1,1), exodiaTheForbidden, CardFace.FRONT);

        //remove the first element from the hand e add the card to get 100 points
        Hand handFirst = firstToPlay.getUserHand();
        CardInHand removedCard = handFirst.getHand().stream().reduce((a,b)->a).get();
        handFirst.removeCard(removedCard);
        handFirst.addCard(exodiaTheForbidden);
        firstToPlay.playCard(exodiaPlacement);
        handFirst.addCard(removedCard);

        assert firstToPlay.getUserCodex().getPoints() == 29;
        assert !(secondToPlay.getUserCodex().getPoints() == 29);

        placeRandom(firstToPlay, firstToPlayController);
        firstToPlayController.draw(DrawableCard.RESOURCECARD, 0);
        assert game.duringEndingTurns();
        assert !game.getState().equals(GameState.END_GAME);

        //play last turns placing the first card in hand and drawing from goldCardDeck
        placeRandom(secondToPlay, secondToPlayController);
        assert !(secondToPlay.getUserCodex().getPoints() == 29);
        secondToPlayController.draw(DrawableCard.RESOURCECARD, 0);

        assert game.duringEndingTurns();
        assert publicGame.getLastTurnCounter() == 1;
        assert !game.getState().equals(GameState.END_GAME);

        placeRandom(firstToPlay, firstToPlayController);
        System.out.println(firstToPlay.getUserCodex().getPoints());
        firstToPlayController.draw(DrawableCard.GOLDCARD, 0);
        placeRandom(secondToPlay, secondToPlayController);
        assert !(secondToPlay.getUserCodex().getPoints() == 29);
        System.out.println(secondToPlay.getUserCodex().getPoints());
        secondToPlayController.draw(DrawableCard.GOLDCARD, 0);

        assert game.getState().equals(GameState.END_GAME);
        assert firstToPlay.getUserCodex().getPoints() == 29;
        System.out.println(secondToPlay.getUserCodex().getPoints());
        assert firstToPlay.getUserCodex().getPoints() > secondToPlay.getUserCodex().getPoints();

        assert firstToPlayView.state.equals(ViewState.GAME_ENDING);
        assert secondToPlayView.state.equals(ViewState.GAME_ENDING);

        assert firstToPlayView.lightGame.getCodexMap().get(firstToPlayView.name).getPoints() == firstToPlay.getUserCodex().getPoints();
        assert firstToPlayView.lightGame.getCodexMap().get(secondToPlayView.name).getPoints() == secondToPlay.getUserCodex().getPoints();

        assert secondToPlayView.lightGame.getCodexMap().get(firstToPlayView.name).getPoints() == firstToPlay.getUserCodex().getPoints();
        assert secondToPlayView.lightGame.getCodexMap().get(secondToPlayView.name).getPoints() == secondToPlay.getUserCodex().getPoints();

        assert firstToPlayView.lightGame.getWinners().contains(firstToPlayView.name);
        assert !firstToPlayView.lightGame.getWinners().contains(secondToPlayView.name);
        assert secondToPlayView.lightGame.getWinners().contains(firstToPlayView.name);
        assert !secondToPlayView.lightGame.getWinners().contains(secondToPlayView.name);
    }

    @Test
    void gameEndingItsATie(){
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";

        String lobbyName1 = "gameEndingItsATie";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        controller1.login(view1.name);
        controller2.login(view2.name);
        controller1.createLobby(lobbyName1, 2);
        assert lobbyGameListController.getLobbyMap().containsKey(lobbyName1);
        controller2.joinLobby(lobbyName1);

        LightCard startCard1 = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard1, CardFace.FRONT);
        LightCard startCard2 = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard2, CardFace.FRONT);

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

        Player player1 = game.getPlayerFromNick(view1.name);
        Player player2 = game.getPlayerFromNick(view2.name);

        PublicGame publicGame = new PublicGame(game);

        Player firstToPlay = game.getCurrentPlayer().equals(player1) ? player1 : player2;
        Player secondToPlay = firstToPlay.equals(player1) ? player2 : player1;

        ViewTest firstToPlayView = firstToPlay.getNickname().equals(view1.name) ? view1 : view2;
        ViewTest secondToPlayView = secondToPlay.getNickname().equals(view1.name) ? view1 : view2;

        Controller firstToPlayController = firstToPlay.getNickname().equals(view1.name) ? controller1 : controller2;
        Controller secondToPlayController = secondToPlay.getNickname().equals(view1.name) ? controller1 : controller2;

        firstToPlayView.state = ViewState.PLACE_CARD;
        secondToPlayView.state = ViewState.IDLE;

        //change secret objective to a useless one, that gives 0 points
        CollectableCardPointMultiplier uselessMultiplier = new CollectableCardPointMultiplier(Map.of(Resource.FUNGI, 100));
        ObjectiveCard uselessObjective = new ObjectiveCard(96, 87, 0, uselessMultiplier);

        player1.setSecretObjective(uselessObjective);
        player2.setSecretObjective(uselessObjective);
        //change public objective card
        CollectableCardPointMultiplier objMultiplier = new CollectableCardPointMultiplier(Map.of(Resource.PLANT, 3));
        ObjectiveCard plantX3 = new ObjectiveCard(96, 87, 100,objMultiplier);

        publicGame.setPublicObj(List.of(uselessObjective, plantX3));

        assert game.getCommonObjective().contains(plantX3);

        //place a fake card that adds 100 points
        HashMap<CardCorner, Collectable> cornerMap1 = new HashMap<>();
        for (CardCorner corner : CardCorner.values()) {
            cornerMap1.put(corner, Resource.PLANT);
        }
        cornerMap1.put(CardCorner.TL, SpecialCollectable.EMPTY);
        ResourceCard exodiaTheForbidden = new ResourceCard(1, 1, Resource.PLANT, 100, cornerMap1);
        Placement exodiaPlacement = new Placement(new Position(1,1), exodiaTheForbidden, CardFace.FRONT);

        //remove the first element from the hand e add the card to get 100 points
        Hand handFirst = firstToPlay.getUserHand();
        CardInHand removedCard = handFirst.getHand().stream().reduce((a,b)->a).get();
        handFirst.removeCard(removedCard);
        handFirst.addCard(exodiaTheForbidden);
        firstToPlay.playCard(exodiaPlacement);
        handFirst.addCard(removedCard);

        assert firstToPlay.getUserCodex().getPoints() == 29;


        placeRandom(firstToPlay, firstToPlayController);
        firstToPlayController.draw(DrawableCard.RESOURCECARD, 0);
        assert game.duringEndingTurns();
        assert !game.getState().equals(GameState.END_GAME);


        Hand handSecond = secondToPlay.getUserHand();
        removedCard = handFirst.getHand().stream().reduce((a,b)->a).get();
        handSecond.removeCard(handSecond.getHand().stream().reduce((a,b)->a).get());
        handSecond.addCard(exodiaTheForbidden);
        secondToPlay.playCard(exodiaPlacement);
        handSecond.addCard(removedCard);

        assert secondToPlay.getUserCodex().getPoints() == 29;

        secondToPlayView.state = ViewState.DRAW_CARD;
        placeRandom(secondToPlay, secondToPlayController);
        secondToPlayController.draw(DrawableCard.GOLDCARD, 0);
        assert game.duringEndingTurns();
        assert !game.getState().equals(GameState.END_GAME);

        assert game.duringEndingTurns();
        assert publicGame.getLastTurnCounter() == 1;
        assert !game.getState().equals(GameState.END_GAME);
        //play last turns placing a card that gives 0 points and doesn't add resources
        HashMap<CardCorner, Collectable> uselessCornerMap = new HashMap<>();
        for (CardCorner corner : CardCorner.values()) {
            uselessCornerMap.put(corner, SpecialCollectable.EMPTY);
        }

        ResourceCard uselessCard = new ResourceCard(1, 1, Resource.ANIMAL, 0, uselessCornerMap);
        Placement uselessPlacement = new Placement(new Position(1,-1), uselessCard, CardFace.FRONT);


        removedCard = handFirst.getHand().stream().reduce((a,b)->a).get();
        handFirst.removeCard(removedCard);
        LightCard lightCard = new LightCard(1, 1);
        handFirst.addCard(Heavifier.heavifyCardInHand(lightCard, lobbyGameListController.getCardTable()));
        Position position = firstToPlay.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement lightPlacement = new LightPlacement(position, lightCard, CardFace.BACK);

        firstToPlayController.place(lightPlacement);
        firstToPlayController.draw(DrawableCard.GOLDCARD, 0);

        removedCard = handSecond.getHand().stream().reduce((a,b)->a).get();
        handSecond.removeCard(removedCard);
        lightCard = new LightCard(1, 1);
        handSecond.addCard(Heavifier.heavifyCardInHand(lightCard, lobbyGameListController.getCardTable()));
        position = secondToPlay.getUserCodex().getFrontier().getFrontier().getFirst();
        lightPlacement = new LightPlacement(position, lightCard, CardFace.BACK);

        secondToPlayController.place(lightPlacement);
        secondToPlayController.draw(DrawableCard.GOLDCARD, 0);

        assert game.getState().equals(GameState.END_GAME);
        System.out.println(firstToPlay.getUserCodex().getPoints());
        System.out.println(secondToPlay.getUserCodex().getPoints());
        assert firstToPlay.getUserCodex().getPoints() == 29;
        assert secondToPlay.getUserCodex().getPoints() == 29;

        assert firstToPlayView.state.equals(ViewState.GAME_ENDING);
        assert secondToPlayView.state.equals(ViewState.GAME_ENDING);

        assert firstToPlayView.lightGame.getCodexMap().get(firstToPlayView.name).getPoints() == firstToPlay.getUserCodex().getPoints();
        assert firstToPlayView.lightGame.getCodexMap().get(secondToPlayView.name).getPoints() == secondToPlay.getUserCodex().getPoints();

        assert secondToPlayView.lightGame.getCodexMap().get(firstToPlayView.name).getPoints() == firstToPlay.getUserCodex().getPoints();
        assert secondToPlayView.lightGame.getCodexMap().get(secondToPlayView.name).getPoints() == secondToPlay.getUserCodex().getPoints();

        assert firstToPlayView.lightGame.getWinners().contains(firstToPlayView.name);
        assert firstToPlayView.lightGame.getWinners().contains(secondToPlayView.name);
        assert secondToPlayView.lightGame.getWinners().contains(firstToPlayView.name);
        assert secondToPlayView.lightGame.getWinners().contains(secondToPlayView.name);
    }

    @Test
    void loginNameTakenOrEmpty(){
        ViewTest view1 = new ViewTest();
        view1.name = "pippo";
        ViewTest duplicateNameView = new ViewTest();
        duplicateNameView.name = "pippo";
        ViewTest emptyNameView = new ViewTest();
        emptyNameView.name = "";
        ViewTest allSpacesNameView = new ViewTest();
        allSpacesNameView.name = "         ";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controllerDuplicateName = new Controller(realLobbyGameListController, duplicateNameView);
        Controller controllerEmptyName = new Controller(realLobbyGameListController, emptyNameView);
        Controller controllerAllSpacesName = new Controller(realLobbyGameListController, allSpacesNameView);

        //view joins before adding lobbies
        controller1.login(view1.name);
        controllerDuplicateName.login(duplicateNameView.name);
        controllerAllSpacesName.login(allSpacesNameView.name);
        controllerEmptyName.login(emptyNameView.name);

        assert view1.state.equals(ViewState.JOIN_LOBBY);
        assert duplicateNameView.state.equals(ViewState.LOGIN_FORM);
        assert emptyNameView.state.equals(ViewState.LOGIN_FORM);
        assert allSpacesNameView.state.equals(ViewState.LOGIN_FORM);
    }

    private void placeRandom(Player player, Controller controller){
        Hand hand = player.getUserHand();
        Position position = player.getUserCodex().getFrontier().getFrontier().getFirst();
        LightCard lightCard = Lightifier.lightifyToCard(hand.getHand().stream().reduce((a,b)->a).get());
        LightPlacement lightPlacement = new LightPlacement(position, lightCard, CardFace.BACK);
        controller.place(lightPlacement);
    }

    @Test
    void createLobbyInvalidParameters(){
        ViewTest gameCreator = new ViewTest();
        gameCreator.name = "pippo";
        ViewTest gameJoiner = new ViewTest();
        gameJoiner.name = "pluto";
        ViewTest lobbyCreator = new ViewTest();
        lobbyCreator.name = "mickey";
        ViewTest invalidLobbyCreator = new ViewTest();
        invalidLobbyCreator.name = "minnie";

        Controller gameCreatorController = new Controller(realLobbyGameListController, gameCreator);
        Controller gameJoinerController = new Controller(realLobbyGameListController, gameJoiner);
        Controller lobbyCreatorController = new Controller(realLobbyGameListController, lobbyCreator);
        Controller invalidLobbyCreatorController = new Controller(realLobbyGameListController, invalidLobbyCreator);

        String gameName = "game1";
        String lobbyName = "lobby1";

        gameCreatorController.login(gameCreator.name);
        gameJoinerController.login(gameJoiner.name);
        lobbyCreatorController.login(lobbyCreator.name);
        invalidLobbyCreatorController.login(invalidLobbyCreator.name);

        //start a game and a lobby
        gameCreatorController.createLobby(gameName, 2);
        gameJoinerController.joinLobby(gameName);
        lobbyCreatorController.createLobby(lobbyName, 2);
        assert gameCreator.state.equals(ViewState.CHOOSE_START_CARD);
        assert gameJoiner.state.equals(ViewState.CHOOSE_START_CARD);
        assert lobbyCreator.state.equals(ViewState.LOBBY);

        //check not possible create lobby with same name as the lobby already present
        invalidLobbyCreatorController.createLobby(lobbyName, 2);
        assert invalidLobbyCreator.state.equals(ViewState.JOIN_LOBBY);

        //check not possible create lobby with same name as the game already present
        invalidLobbyCreatorController.createLobby(gameName, 2);
        assert invalidLobbyCreator.state.equals(ViewState.JOIN_LOBBY);

        //check not possible create lobby with empty name
        invalidLobbyCreatorController.createLobby("", 2);
        assert invalidLobbyCreator.state.equals(ViewState.JOIN_LOBBY);

        //check not possible create lobby with name of only spaces
        invalidLobbyCreatorController.createLobby("         ", 2);
        assert invalidLobbyCreator.state.equals(ViewState.JOIN_LOBBY);

    }

    @Test
    void invalidLobbyJoin(){
        ViewTest lobbyCreator = new ViewTest();
        lobbyCreator.name = "mickey";
        ViewTest invalidLobbyJoiner = new ViewTest();
        invalidLobbyJoiner.name = "minnie";

        Controller lobbyCreatorController = new Controller(realLobbyGameListController, lobbyCreator);
        Controller invalidLobbyCreatorJoiner = new Controller(realLobbyGameListController, invalidLobbyJoiner);

        String lobbyName = "lobby1";
        String nonExistentLobbyName = lobbyName + "_error";

        lobbyCreatorController.login(lobbyCreator.name);
        invalidLobbyCreatorJoiner.login(invalidLobbyJoiner.name);

        //start the game
        lobbyCreatorController.createLobby(lobbyName, 2);
        assert lobbyCreator.state.equals(ViewState.LOBBY);

        invalidLobbyCreatorJoiner.joinLobby(nonExistentLobbyName);
        assert invalidLobbyJoiner.state.equals(ViewState.JOIN_LOBBY);

    }

    @Test
    void deckFinished() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        int numberOfPlayers = 2;
        int totalCardsInDeck = 40;
        int resourceCardsInHandAtStart = 2;
        int goldCardsInHandAtStart = 1;

        String lobbyName1 = "deckFinished";
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
            if(i == ((totalCardsInDeck - resourceCardsInHandAtStart * numberOfPlayers) / numberOfPlayers) -1 )
                secondPlayerController.controller.draw(DrawableCard.RESOURCECARD, 1);
            else
                secondPlayerController.controller.draw(DrawableCard.RESOURCECARD, 0);

        }


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
            if(i == ((totalCardsInDeck - goldCardsInHandAtStart * numberOfPlayers) / numberOfPlayers) -1 )
                secondPlayerController.controller.draw(DrawableCard.GOLDCARD, 1);
            else
                secondPlayerController.controller.draw(DrawableCard.GOLDCARD, 0);

        }

        assert game.areDeckEmpty();
        assert game.getResourceCardDeck().isEmpty();
        assert view1.lightGame.getDecks().get(DrawableCard.RESOURCECARD).getDeckBack() == null;
        assert Arrays.stream(view1.lightGame.getDecks().get(DrawableCard.RESOURCECARD).getCardBuffer()).allMatch(Objects::isNull);
        assert view1.lightGame.getDecks().get(DrawableCard.GOLDCARD).getDeckBack() == null;
        assert Arrays.stream(view1.lightGame.getDecks().get(DrawableCard.GOLDCARD).getCardBuffer()).allMatch(Objects::isNull);
        assert view2.lightGame.getDecks().get(DrawableCard.RESOURCECARD).getDeckBack() == null;
        assert Arrays.stream(view2.lightGame.getDecks().get(DrawableCard.RESOURCECARD).getCardBuffer()).allMatch(Objects::isNull);
        assert view2.lightGame.getDecks().get(DrawableCard.GOLDCARD).getDeckBack() == null;
        assert Arrays.stream(view2.lightGame.getDecks().get(DrawableCard.GOLDCARD).getCardBuffer()).allMatch(Objects::isNull);


        assert game.getGoldCardDeck().isEmpty();

        assert game.duringEndingTurns();
        assert game.getState().equals(GameState.LAST_TURNS);

        assert game.getCurrentPlayer().equals(firstPlayer);

        LightCard cardPlaced1 = Lightifier.lightifyToCard(firstPlayer.getUserHand().getHand().stream().toList().getFirst());
        Position position1 = firstPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement1 = new LightPlacement(position1, cardPlaced1, CardFace.BACK);

        firstPlayerController.controller.place(placement1);

        assert game.getCurrentPlayer().equals(secondPlayer);

        LightCard cardPlaced2 = Lightifier.lightifyToCard(secondPlayer.getUserHand().getHand().stream().toList().getFirst());
        Position position2 = secondPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement2 = new LightPlacement(position2, cardPlaced2, CardFace.BACK);

        secondPlayerController.controller.place(placement2);

        assert game.getState().equals(GameState.END_GAME);
    }

    @Test
    void chatPubblicMessageTest(){
        ViewTest[] views = new ViewTest[3];
        views[0] = new ViewTest();
        views[1] = new ViewTest();
        views[2] = new ViewTest();
        views[0].name = "pippo";
        views[1].name = "pluto";
        views[2].name = "paperino";

        String lobbyName1 = "chatMessageTest";
        Controller pippoController = new Controller(realLobbyGameListController, views[0]);
        Controller plutoController = new Controller(realLobbyGameListController, views[1]);
        Controller paperinoController = new Controller(realLobbyGameListController, views[2]);

        pippoController.login(views[0].name);
        plutoController.login(views[1].name);
        paperinoController.login(views[2].name);

        pippoController.createLobby(lobbyName1, 3);
        plutoController.joinLobby(lobbyName1);
        paperinoController.joinLobby(lobbyName1);

        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        PublicGameController publicController = new PublicGameController(gameController);
        Game game = publicController.getGame();

        Random random = new Random();
        int senderIdx = random.nextInt(3);

        //Send a public message from a random player
        pippoController.sendChatMessage(new ChatMessage(views[senderIdx].name, "PublicMSg"));

        //Check if the messages is saved in the model (chatManager)
        for(int i=0; i < views.length; i++){
            assert game.getGameParty().getChatManager().retrieveChat(views[i].name).stream().map(ChatMessage::getSender).anyMatch(views[senderIdx].name::equals);
            assert game.getGameParty().getChatManager().retrieveChat(views[i].name).stream().map(ChatMessage::getMessage).anyMatch("PublicMSg"::equals);
            assert game.getGameParty().getChatManager().retrieveChat(views[i].name).size() == 1;
        }

        //Check if every lightModel has the public message and only that
        for(int i = 0; i < views.length; i++){
            assert views[i].lightGame.getLightGameParty().getLightChat().getChatHistory().stream().map(ChatMessage::getSender).anyMatch(views[senderIdx].name::equals);
            assert views[i].lightGame.getLightGameParty().getLightChat().getChatHistory().stream().map(ChatMessage::getMessage).anyMatch("PublicMSg"::equals);
            assert views[i].lightGame.getLightGameParty().getLightChat().getChatHistory().size() == 1;
        }

        int newSenderIdx = (senderIdx + 1) % 3;
        //Send a another public message, from another player, and check if the chat is updated correctly
        plutoController.sendChatMessage(new ChatMessage(views[newSenderIdx].name, "PublicMSg2"));

        for(int i=0; i < views.length; i++){
            //new message is saved in the model
            assert game.getGameParty().getChatManager().retrieveChat(views[i].name).stream().map(ChatMessage::getSender).anyMatch(views[newSenderIdx].name::equals);
            assert game.getGameParty().getChatManager().retrieveChat(views[i].name).stream().map(ChatMessage::getMessage).anyMatch("PublicMSg2"::equals);
            //old message is still there
            assert game.getGameParty().getChatManager().retrieveChat(views[i].name).stream().map(ChatMessage::getSender).anyMatch(views[senderIdx].name::equals);
            assert game.getGameParty().getChatManager().retrieveChat(views[i].name).stream().map(ChatMessage::getMessage).anyMatch("PublicMSg"::equals);
            //no other messages are present
            assert game.getGameParty().getChatManager().retrieveChat(views[i].name).size() == 2;
        }

        for(int i = 0; i < views.length; i++){
            //new message is saved in the lightModel
            assert views[i].lightGame.getLightGameParty().getLightChat().getChatHistory().stream().map(ChatMessage::getSender).anyMatch(views[newSenderIdx].name::equals);
            assert views[i].lightGame.getLightGameParty().getLightChat().getChatHistory().stream().map(ChatMessage::getMessage).anyMatch("PublicMSg2"::equals);
            //old message is still there
            assert views[i].lightGame.getLightGameParty().getLightChat().getChatHistory().stream().map(ChatMessage::getSender).anyMatch(views[senderIdx].name::equals);
            assert views[i].lightGame.getLightGameParty().getLightChat().getChatHistory().stream().map(ChatMessage::getMessage).anyMatch("PublicMSg"::equals);
            //no other messages are present
            assert views[i].lightGame.getLightGameParty().getLightChat().getChatHistory().size() == 2;
        }
    }

    @Test
    void chatPrivateMsgTest(){
        ViewTest[] views = new ViewTest[3];
        views[0] = new ViewTest();
        views[1] = new ViewTest();
        views[2] = new ViewTest();
        views[0].name = "pippo";
        views[1].name = "pluto";
        views[2].name = "paperino";

        String lobbyName1 = "chatMessageTest";
        Controller pippoController = new Controller(realLobbyGameListController, views[0]);
        Controller plutoController = new Controller(realLobbyGameListController, views[1]);
        Controller paperinoController = new Controller(realLobbyGameListController, views[2]);

        pippoController.login(views[0].name);
        plutoController.login(views[1].name);
        paperinoController.login(views[2].name);

        pippoController.createLobby(lobbyName1, 3);
        plutoController.joinLobby(lobbyName1);
        paperinoController.joinLobby(lobbyName1);

        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        PublicGameController publicController = new PublicGameController(gameController);
        Game game = publicController.getGame();

        //Check that a message cannot be sent if the sender is also the receiver
        pippoController.sendChatMessage(new ChatMessage("pippo", "PrivateMSg", "pippo"));
        assert game.getGameParty().getChatManager().retrieveChat("pippo").isEmpty();
        assert views[0].lightGame.getLightGameParty().getLightChat().getChatHistory().isEmpty();

        //Send a private message from a random player to another random player
        pippoController.sendChatMessage(new ChatMessage("pippo", "PrivateMSg", "pluto"));

        //Check that only the sender and the receiver have the message in their lightModel
        for(int i = 0; i < views.length; i++){
            if(views[i].name.equals("pippo") || views[i].name.equals("pluto")){
                assert views[i].lightGame.getLightGameParty().getLightChat().getChatHistory().stream().map(ChatMessage::getSender).anyMatch("pippo"::equals);
                assert views[i].lightGame.getLightGameParty().getLightChat().getChatHistory().stream().map(ChatMessage::getMessage).anyMatch("PrivateMSg"::equals);
                assert views[i].lightGame.getLightGameParty().getLightChat().getChatHistory().stream().map(ChatMessage::getReceiver).anyMatch("pluto"::equals);
                assert views[i].lightGame.getLightGameParty().getLightChat().getChatHistory().size() == 1;
            } else {
                assert views[i].lightGame.getLightGameParty().getLightChat().getChatHistory().isEmpty();
            }
        }

        //Check if the messages is saved in the model (chatManager) only for the sender and the receiver
        for(int i=0; i < views.length; i++){
            if(views[i].name.equals("pippo") || views[i].name.equals("pluto")){
                assert game.getGameParty().getChatManager().retrieveChat(views[i].name).stream().map(ChatMessage::getSender).anyMatch("pippo"::equals);
                assert game.getGameParty().getChatManager().retrieveChat(views[i].name).stream().map(ChatMessage::getMessage).anyMatch("PrivateMSg"::equals);
                assert game.getGameParty().getChatManager().retrieveChat(views[i].name).stream().map(ChatMessage::getReceiver).anyMatch("pluto"::equals);
                assert game.getGameParty().getChatManager().retrieveChat(views[i].name).size() == 1;
            } else {
                assert game.getGameParty().getChatManager().retrieveChat(views[i].name).isEmpty();
            }
        }
    }
}