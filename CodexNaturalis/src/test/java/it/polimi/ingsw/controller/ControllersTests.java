package it.polimi.ingsw.controller;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.OSRelated;
import it.polimi.ingsw.controller.PublicController.ControllerPublic;
import it.polimi.ingsw.controller.PublicController.GameControllerPublic;
import it.polimi.ingsw.controller.PublicController.LobbyControllerPublic;
import it.polimi.ingsw.controller.PublicController.LobbyListControllerPublic;
import it.polimi.ingsw.lightModel.LightModelConfig;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.ViewTest;
import it.polimi.ingsw.lightModel.lightPlayerRelated.*;
import it.polimi.ingsw.lightModel.lightTableRelated.LightDeck;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.cardReleted.cards.Card;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.utilityEnums.*;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class ControllersTests {
    private LobbyGameListController realLobbyGameListController;
    private LobbyListControllerPublic lobbyGameListController;

    @BeforeAll
    public static void setUpAll(){
        OSRelated.checkOrCreateDataFolderServer(); //Create the dataFolder if necessary. Normally this is done in the Server class
    }

    @BeforeEach
    public void setUp(){
        realLobbyGameListController = new LobbyGameListController();
        lobbyGameListController = new LobbyListControllerPublic(realLobbyGameListController);

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
        //add lobby
        Lobby lobby = new Lobby(2, "test1");
        controller1.createLobby(lobby.getLobbyName(), lobby.getNumberOfMaxPlayer());
        //view joins after adding lobbies
        controller2.login(view2.name);

        System.out.println(view1.name);
        assert !view1.lightLobbyList.getLobbies().contains(Lightifier.lightify(lobby));
        assert !lobbyGameListController.getViewMap().containsKey(view1.name);
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
        String LobbyCreatorName = "lobbyCreator";
        List<ViewTest> views = Arrays.stream(new ViewTest[]{view1, view2}).toList();
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);

        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "topolino";
        String lobbyName1 = "test1";
        String lobbyName2 = "test2";

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
        String LobbyCreatorName = "lobbyCreator";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);

        view1.name = "pippo";
        view2.name = "pluto";
        String lobbyName1 = "test1";
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
        view3.name = "topolino";
        String lobbyName1 = "test1";
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
        GameControllerPublic publicController = new GameControllerPublic(gameController);
        Game game = publicController.getGame();
        assert game.getGameParty().getNumberOfMaxPlayer() == 2;
        assert game.getGameParty().getUsersList().stream().map(User::getNickname).toList().contains(view1.name);
        assert game.getGameParty().getUsersList().stream().map(User::getNickname).toList().contains(view2.name);
        assert game.getGameParty().getUsersList().size() == 2;
        assert game.getGameParty().getCurrentPlayer() != null;
        assert game.getName().equals(lobbyName1);
        assert game.getGoldCardDeck() != null;
        assert game.getResourceCardDeck() != null;
        assert game.getStartingCardDeck() != null;
        assert game.getObjectiveCardDeck() != null;
        //user have a start card in hand
        assert game.getGameParty().getUsersList().stream().map(User::getUserHand).map(Hand::getStartCard).allMatch(Objects::nonNull);

        //lobby removed from lobbyList
        assert !view3.lightLobbyList.getLobbies().contains(lobbyName1);

        //lightLobbyList on view is empty
        assert view1.lightLobbyList.getLobbies().isEmpty();
        //lobbyList on view has been reset
        assert view1.lightLobby.nicknames().isEmpty();
        assert view1.lightLobby.numberMaxPlayer() == 0;
        assert view1.lightLobby.name().isEmpty();
        //gameParty correctly set
        assert view1.lightGame.getLightGameParty().getGameName().equals(lobbyName1);
        assert view1.lightGame.getLightGameParty().getPlayerActiveList().get(view1.name) != null;
        assert view1.lightGame.getLightGameParty().getPlayerActiveList().get(view2.name) != null;
        assert view1.lightGame.getLightGameParty().getPlayerActiveList().size() == 2;
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
        assert cardInHandThatIsNotNull.idFront() == game.getUserFromNick(view1.name).getUserHand().getStartCard().getIdFront();
        assert cardInHandThatIsNotNull.idBack() == game.getUserFromNick(view1.name).getUserHand().getStartCard().getIdBack();
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
        assert view1.lightGame.getRanking().isEmpty();

        //lightLobbyList on view is empty
        assert view2.lightLobbyList.getLobbies().isEmpty();
        //lobbyList on view has been reset
        assert view2.lightLobby.nicknames().isEmpty();
        assert view2.lightLobby.numberMaxPlayer() == 0;
        assert view2.lightLobby.name().isEmpty();
        //gameParty correctly set
        assert view2.lightGame.getLightGameParty().getGameName().equals(lobbyName1);
        assert view2.lightGame.getLightGameParty().getPlayerActiveList().get(view1.name) != null;
        assert view2.lightGame.getLightGameParty().getPlayerActiveList().get(view2.name) != null;
        assert view2.lightGame.getLightGameParty().getPlayerActiveList().size() == 2;
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
        assert cardInHandThatIsNotNull.idFront() == game.getUserFromNick(view2.name).getUserHand().getStartCard().getIdFront();
        assert cardInHandThatIsNotNull.idBack() == game.getUserFromNick(view2.name).getUserHand().getStartCard().getIdBack();
        assert view2.lightGame.getHand().getCardPlayability().values().size() == 1;
        Assertions.assertTrue(view2.lightGame.getHand().getCardPlayability().get(cardInHandThatIsNotNull) == true);
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
        assert view2.lightGame.getRanking().isEmpty();
    }


    @Test
    void leaveLobby() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "topolino";
        view4.name = "gianni";
        String lobbyName1 = "test1";
        String lobbyName2 = "test2";
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
        LobbyControllerPublic publicController = new LobbyControllerPublic(lobbyController);
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
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "topolino";
        view4.name = "gianni";
        String lobbyName1 = "test1";
        String lobbyName2 = "test2";
        Controller controller1 = new Controller(realLobbyGameListController, view1);
        Controller controller2 = new Controller(realLobbyGameListController, view2);
        Controller controller3 = new Controller(realLobbyGameListController, view3);
        Controller controller4 = new Controller(realLobbyGameListController, view4);

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
        GameControllerPublic publicController = new GameControllerPublic(gameController);
        Game game = publicController.getGame();

        assert game != null;
        User user1 = game.getUserFromNick(view1.name);
        User user2 = game.getUserFromNick(view2.name);
        assert game.isInStartCardState();

        //user1
        assert user1.hasPlacedStartCard();
        assert user1.getUserCodex().getPlacementAt(new Position(0,0)) != null;
        assert user1.getUserHand().getHand().stream().allMatch(Objects::nonNull);
        assert user1.hasPlacedStartCard();
        assert user1.getUserHand().getStartCard() == null;
        //user2
        assert !user2.hasPlacedStartCard();
        assert user2.getUserHand().getHand().stream().allMatch(Objects::isNull);
        assert user2.getUserHand().getStartCard() != null;

        //view 1 lightModel updated correctly
        assert view1.lightGame.getCodexMap().get(view1.name).getPlacementHistory().contains(startPlacement);
        assert Arrays.stream(view1.lightGame.getHand().getCards()).allMatch(Objects::nonNull);
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
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "topolino";
        view4.name = "gianni";
        String lobbyName1 = "test1";
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
        GameControllerPublic publicController = new GameControllerPublic(gameController);
        Game game = publicController.getGame();

        User user1 = game.getUserFromNick(view1.name);
        User user2 = game.getUserFromNick(view2.name);
        Hand hand1 = user1.getUserHand();
        Hand hand2 = user2.getUserHand();

        //user1
        assert user1.hasPlacedStartCard();
        assert user1.getUserHand().getSecretObjectiveChoices().stream().allMatch(Objects::nonNull);
        assert !user1.hasChosenObjective();
        //user2
        assert user2.hasPlacedStartCard();
        assert user2.getUserHand().getSecretObjectiveChoices().stream().allMatch(Objects::nonNull);
        assert !user2.hasChosenObjective();

        //view 1 lightModel updated correctly
        LightHand lightHand1 = view1.lightGame.getHand();
        List<LightCard> cardsOnClient1 = Arrays.stream(lightHand1.getCards()).toList();
        List<LightCard> cardsOnServer1 = hand1.getHand().stream().map(Lightifier::lightifyToCard).toList();
        List<LightCard> secretObjOnLight = Arrays.stream(lightHand1.getSecretObjectiveOptions()).toList();
        List<LightCard> secretObjOnServer = hand1.getSecretObjectiveChoices().stream().map(Lightifier::lightifyToCard).toList();
        System.out.println(secretObjOnServer);
        System.out.println(secretObjOnLight);

        assert cardsOnServer1.size() == 3;
        assert cardsOnServer1.stream().allMatch(Objects::nonNull);
        assert secretObjOnLight.equals(secretObjOnServer);
        assert Arrays.stream(lightHand1.getSecretObjectiveOptions()).allMatch(Objects::nonNull);
        assert cardsOnClient1.containsAll(cardsOnServer1);
        assert cardsOnServer1.containsAll(cardsOnClient1);
        assert Arrays.stream(view1.lightGame.getHandOthers().get(view2.name).getCards()).allMatch(Objects::nonNull);
        assert view1.lightGame.getCodexMap().get(view2.name).getPlacementHistory().contains(startPlacement2);

        int resourceActualDeckOnViewBackID1 = view1.lightGame.getResourceDeck().getDeckBack().idBack();
        int goldActualDeckBackOnViewID1 = view1.lightGame.getGoldDeck().getDeckBack().idBack();
        List<Integer> resourceDeckBufferOnViewID1 = Arrays.stream(view1.lightGame.getResourceDeck().getCardBuffer()).map(LightCard::idFront).toList();
        List<Integer> goldDeckBufferOnViewID1 = Arrays.stream(view1.lightGame.getGoldDeck().getCardBuffer()).map(LightCard::idFront).toList();

        assert resourceActualDeckOnViewBackID1 == game.getResourceCardDeck().showTopCardOfDeck().getIdBack();
        assert goldActualDeckBackOnViewID1 == game.getGoldCardDeck().showTopCardOfDeck().getIdBack();
        assert resourceDeckBufferOnViewID1.containsAll(game.getResourceCardDeck().getBuffer().stream().map(Card::getIdFront).toList());
        assert goldDeckBufferOnViewID1.containsAll(game.getGoldCardDeck().getBuffer().stream().map(Card::getIdFront).toList());

        //view 2 lightModel updated correctly
        LightHand lightHand2 = view2.lightGame.getHand();
        List<LightCard> secretObjOnLight2 = Arrays.stream(lightHand2.getSecretObjectiveOptions()).toList();
        List<LightCard> secretObjOnServer2 = hand2.getSecretObjectiveChoices().stream().map(Lightifier::lightifyToCard).toList();

        assert secretObjOnServer2.equals(secretObjOnLight2);
        List<LightCard> cardsOnClient2 = Arrays.stream(lightHand1.getCards()).toList();
        List<LightCard> cardsOnServer2 = hand1.getHand().stream().map(Lightifier::lightifyToCard).toList();
        assert cardsOnClient1.containsAll(cardsOnServer2);
        assert cardsOnServer1.containsAll(cardsOnClient2);
        assert Arrays.stream(lightHand2.getSecretObjectiveOptions()).allMatch(Objects::nonNull);
        assert Arrays.stream(view2.lightGame.getHandOthers().get(view1.name).getCards()).allMatch(Objects::nonNull);
        assert view2.lightGame.getCodexMap().get(view1.name).getPlacementHistory().contains(startPlacement1);
    }

    @Test
    void choseSecretObjective() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "topolino";
        view4.name = "gianni";
        String lobbyName1 = "test1";
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
        //choose secret objective
        LightCard secretObjective = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller1.chooseSecretObjective(secretObjective);

        //model
        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        GameControllerPublic publicController = new GameControllerPublic(gameController);
        Game game = publicController.getGame();

        User user1 = game.getUserFromNick(view1.name);
        Hand hand1 = user1.getUserHand();
        User user2 = game.getUserFromNick(view2.name);

        //user1
        assert user1.hasChosenObjective();
        assert hand1.getSecretObjective() != null;
        assert hand1.getSecretObjective().getIdFront() == secretObjective.idFront();
        assert hand1.getSecretObjective().getIdBack() == secretObjective.idBack();
        assert hand1.getSecretObjectiveChoices() == null;

        //user2
        assert !user2.hasChosenObjective();
        assert game.othersHadAllChooseSecretObjective(view2.name);

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
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "topolino";
        view4.name = "gianni";
        String lobbyName1 = "test1";
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
        //choose secret objective
        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller1.chooseSecretObjective(secretObjective1);
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller2.chooseSecretObjective(secretObjective2);
        //model
        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        GameControllerPublic publicController = new GameControllerPublic(gameController);
        Game game = publicController.getGame();

        User user1 = game.getUserFromNick(view1.name);
        Hand hand1 = user1.getUserHand();
        User user2 = game.getUserFromNick(view2.name);
        Hand hand2 = user2.getUserHand();
        //user1
        assert user1.hasChosenObjective();
        assert hand1.getSecretObjective() != null;
        assert hand1.getSecretObjective().getIdFront() == secretObjective1.idFront();
        assert hand1.getSecretObjective().getIdBack() == secretObjective1.idBack();
        assert hand1.getSecretObjectiveChoices() == null;
        //user2
        assert user1.hasChosenObjective();
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
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "topolino";
        view4.name = "gianni";
        String lobbyName1 = "test1";
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
        //choose secret objective
        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller1.chooseSecretObjective(secretObjective1);
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller2.chooseSecretObjective(secretObjective2);
        //place
        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        GameControllerPublic publicController = new GameControllerPublic(gameController);
        Game game = publicController.getGame();

        User user1 = game.getUserFromNick(view1.name);
        User user2 = game.getUserFromNick(view2.name);
        game.setCurrentPlayerIndex(game.getUsersList().lastIndexOf(user1));
        CardInHand cardPlaced = user1.getUserHand().getHand().stream().filter(Objects::nonNull).toList().getFirst();
        Position position = user1.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement = new LightPlacement(position, Lightifier.lightifyToCard(cardPlaced), CardFace.FRONT);
        controller1.place(placement);

        //model
        assert !user1.getUserHand().getHand().contains(cardPlaced);
        assert user1.getUserCodex().getPlacementAt(position) != null;
        assert user1.getUserCodex().getPlacementAt(position).card().getIdFront() == cardPlaced.getIdFront();
        assert user1.getUserCodex().getPlacementAt(position).card().getIdBack() == cardPlaced.getIdBack();

        //lightModel1
        LightCodex lightCodex1 = view1.lightGame.getCodexMap().get(view1.name);
        LightHand lightHand1 = view1.lightGame.getHand();
        assert !Arrays.stream(lightHand1.getCards()).toList().contains(Lightifier.lightifyToCard(cardPlaced));
        assert lightCodex1.getPlacementHistory().contains(placement);
        assert !lightCodex1.getFrontier().frontier().contains(position);
        assert lightCodex1.getEarnedCollectables().equals(user1.getUserCodex().getEarnedCollectables());
        assert lightCodex1.getFrontier().frontier().equals(user1.getUserCodex().getFrontier().getFrontier());
        for(CardInHand card : user1.getUserHand().getHand()){
            assert card == null || lightHand1.getCardPlayability().get(Lightifier.lightifyToCard(card)) == card.canBePlaced(user1.getUserCodex());
        }
        //lightModel2
        LightCodex lightCodex1on2 = view2.lightGame.getCodexMap().get(view1.name);
        List<LightBack> lightHand1on2 = new ArrayList<>(Arrays.stream(view2.lightGame.getHandOthers().get(view1.name).getCards()).toList());
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
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "topolino";
        view4.name = "gianni";
        String lobbyName1 = "test1";
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
        //choose secret objective
        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller1.chooseSecretObjective(secretObjective1);
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller2.chooseSecretObjective(secretObjective2);
        //place
        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        GameControllerPublic publicController = new GameControllerPublic(gameController);
        Game game = publicController.getGame();

        User user1 = game.getUserFromNick(view1.name);
        User user2 = game.getUserFromNick(view2.name);
        game.setCurrentPlayerIndex(game.getUsersList().lastIndexOf(user1));
        CardInHand cardPlaced = user1.getUserHand().getHand().stream().filter(Objects::nonNull).toList().getFirst();
        Position position = user1.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement = new LightPlacement(position, Lightifier.lightifyToCard(cardPlaced), CardFace.FRONT);
        controller1.place(placement);
        CardInHand card = game.getGoldCardDeck().showTopCardOfDeck();
        controller1.draw(DrawableCard.GOLDCARD, Configs.actualDeckPos);

        //model
        List<Integer> heavyBuffer = game.getGoldCardDeck().getBuffer().stream().map(Card::getIdFront).toList();
        assert user1.getUserHand().getHand().contains(card);
        assert game.getCurrentPlayer().equals(user2);
        assert !heavyBuffer.contains(card.getIdFront());
        assert game.getGoldCardDeck().showTopCardOfDeck().getIdFront() != card.getIdFront();
        //lightModel1
        List<Integer> lightBuffer1 = Arrays.stream(view1.lightGame.getGoldDeck().getCardBuffer()).map(LightCard::idFront).toList();
        assert Arrays.stream(view1.lightGame.getHand().getCards()).toList().contains(Lightifier.lightifyToCard(card));
        assert view1.lightGame.getCurrentPlayer().equals(view2.name);
        assert heavyBuffer.containsAll(lightBuffer1) && lightBuffer1.containsAll(heavyBuffer);
        assert view1.lightGame.getGoldDeck().getDeckBack().idBack() == game.getGoldCardDeck().showTopCardOfDeck().getIdBack();
        //lightModel2
        List<Integer> lightBuffer2 = Arrays.stream(view1.lightGame.getGoldDeck().getCardBuffer()).map(LightCard::idFront).toList();
        assert Arrays.stream(view2.lightGame.getHandOthers().get(view1.name).getCards()).toList().contains(new LightBack(card.getIdBack()));
        assert view1.lightGame.getCurrentPlayer().equals(view2.name);
        assert heavyBuffer.containsAll(lightBuffer2) && lightBuffer2.containsAll(heavyBuffer);
        assert view2.lightGame.getGoldDeck().getDeckBack().idBack() == game.getGoldCardDeck().showTopCardOfDeck().getIdBack();

    }

    @Test
    void drawFromBuffer() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "topolino";
        view4.name = "gianni";
        String lobbyName1 = "test1";
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
        //choose secret objective
        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller1.chooseSecretObjective(secretObjective1);
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller2.chooseSecretObjective(secretObjective2);
        //place
        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        GameControllerPublic publicController = new GameControllerPublic(gameController);
        Game game = publicController.getGame();

        User user1 = game.getUserFromNick(view1.name);
        User user2 = game.getUserFromNick(view2.name);
        game.setCurrentPlayerIndex(game.getUsersList().lastIndexOf(user1));
        CardInHand cardPlaced = user1.getUserHand().getHand().stream().filter(Objects::nonNull).toList().getFirst();
        Position position = user1.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement = new LightPlacement(position, Lightifier.lightifyToCard(cardPlaced), CardFace.FRONT);
        controller1.place(placement);
        CardInHand card = game.getGoldCardDeck().showCardFromBuffer(0);
        controller1.draw(DrawableCard.GOLDCARD, 0);

        //model
        List<Integer> heavyBuffer = game.getGoldCardDeck().getBuffer().stream().map(Card::getIdFront).toList();
        assert user1.getUserHand().getHand().contains(card);
        assert game.getCurrentPlayer().equals(user2);
        assert !heavyBuffer.contains(card.getIdFront());
        assert game.getGoldCardDeck().showTopCardOfDeck().getIdFront() != card.getIdFront();
        //lightModel1
        List<Integer> lightBuffer1 = Arrays.stream(view1.lightGame.getGoldDeck().getCardBuffer()).map(LightCard::idFront).toList();
        assert Arrays.stream(view1.lightGame.getHand().getCards()).toList().contains(Lightifier.lightifyToCard(card));
        assert view1.lightGame.getCurrentPlayer().equals(view2.name);
        assert heavyBuffer.containsAll(lightBuffer1) && lightBuffer1.containsAll(heavyBuffer);
        assert view1.lightGame.getGoldDeck().getDeckBack().idBack() == game.getGoldCardDeck().showCardFromBuffer(0).getIdBack();
        //lightModel2
        List<Integer> lightBuffer2 = Arrays.stream(view1.lightGame.getGoldDeck().getCardBuffer()).map(LightCard::idFront).toList();
        assert Arrays.stream(view2.lightGame.getHandOthers().get(view1.name).getCards()).toList().contains(new LightBack(card.getIdBack()));
        assert view1.lightGame.getCurrentPlayer().equals(view2.name);
        assert heavyBuffer.containsAll(lightBuffer2) && lightBuffer2.containsAll(heavyBuffer);
        assert view2.lightGame.getGoldDeck().getDeckBack().idBack() == game.getGoldCardDeck().showCardFromBuffer(0).getIdBack();

    }

    @Test
    void completeTurn() {
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

        LightCard startCard1 = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard1, CardFace.FRONT);
        LightCard startCard2 = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard2, CardFace.FRONT);

        controller1.place(startPlacement1);
        controller2.place(startPlacement2);

        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];

        controller1.chooseSecretObjective(secretObjective1);
        controller2.chooseSecretObjective(secretObjective2);

        GameController gameController = lobbyGameListController.getGameMap().get(lobbyName1);
        GameControllerPublic publicController = new GameControllerPublic(gameController);
        Game game = publicController.getGame();

        ControllerPublic firstPlayerController = new ControllerPublic(view1.name.equals(game.getCurrentPlayer().getNickname()) ? controller1 : controller2);
        ControllerPublic secondPlayerController = new ControllerPublic(view1.name.equals(game.getCurrentPlayer().getNickname()) ? controller2 : controller1);
        User firstPlayer = game.getUserFromNick(firstPlayerController.getNickname());
        User secondPlayer = game.getUserFromNick(secondPlayerController.getNickname());

        LightCard cardPlaced1 = Lightifier.lightifyToCard(firstPlayer.getUserHand().getHand().stream().toList().getFirst());
        Position position1 = firstPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement1 = new LightPlacement(position1, cardPlaced1, CardFace.FRONT);

        firstPlayerController.controller.place(placement1);
        firstPlayerController.controller.draw(DrawableCard.GOLDCARD, 0);
    }

/*
    @Test
    void gameEndingCausePoints(){
        LobbyListControllerPublic lobbyGameListController = new LobbyListControllerPublic();
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "topolino";
        view4.name = "gianni";
        String lobbyName1 = "test1";
        ControllerPublic controller1 = new ControllerPublic(lobbyGameListController, view1);
        ControllerPublic controller2 = new ControllerPublic(lobbyGameListController, view2);

        Map<String, ControllerPublic> serverModelControllerMap = new HashMap<>();
        serverModelControllerMap.put(view1.name, controller1);
        serverModelControllerMap.put(view2.name, controller2);

        controller1.login(view1.name);
        controller1.createLobby(lobbyName1, 2);
        controller2.login(view2.name);
        controller2.joinLobby(lobbyName1);

        System.out.println(lobbyGameListController.gameMap.get(lobbyName1).playerViewMap.keySet());
        //choose StartCard
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller1.place(startPlacement1);
        startCard = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        controller2.place(startPlacement2);
        //choose secret objective
        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller1.chooseSecretObjective(secretObjective1);
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];
        controller2.chooseSecretObjective(secretObjective2);
        //place
        Game game = lobbyGameListController.gameMap.get(lobbyName1).game;
        User user1 = game.getUserFromNick(view1.name);
        User user2 = game.getUserFromNick(view2.name);


        User firstUserInTurn = game.getUsersList().getFirst();
        CardInHand cardPlaced1 = firstUserInTurn.getUserHand().getHand().stream().filter(Objects::nonNull).toList().getFirst();
        Position position1 = firstUserInTurn.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement1 = new LightPlacement(position1, Lightifier.lightifyToCard(cardPlaced1), CardFace.FRONT);
        serverModelControllerMap.get(firstUserInTurn.getNickname()).place(placement1);
        //place a fake card that adds 20 points
        HashMap<CardCorner, Collectable> cornerMap1 = new HashMap<>();
        for(CardCorner corner : CardCorner.values()){
            cornerMap1.put(corner, SpecialCollectable.EMPTY);
        }
        ResourceCard exodiaTheForbidden = new ResourceCard(1,1, Resource.PLANT, 20, cornerMap1);
        firstUserInTurn.getUserCodex().playCard(new Placement(firstUserInTurn.getUserCodex().getFrontier().getFrontier().getFirst(), exodiaTheForbidden, CardFace.FRONT));
        serverModelControllerMap.get(firstUserInTurn.getNickname()).draw(DrawableCard.GOLDCARD, 0);

        assert game.duringLastTurns();
        assert lobbyGameListController.gameMap.get(lobbyName1).playerViewMap.containsKey(view2.name);

        User nextPlayer = game.getCurrentPlayer();
        CardInHand cardPlaced2 = nextPlayer.getUserHand().getHand().stream().filter(Objects::nonNull).toList().getFirst();
        Position position2 = nextPlayer.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement2 = new LightPlacement(position2, Lightifier.lightifyToCard(cardPlaced2), CardFace.FRONT);
        serverModelControllerMap.get(nextPlayer.getNickname()).place(placement2);
        serverModelControllerMap.get(nextPlayer.getNickname()).draw(DrawableCard.GOLDCARD, 0);

        assert firstUserInTurn.getUserCodex().getPoints() >= 20;
        assert game.getCurrentPlayer().equals(nextPlayer);
        System.out.println(game.getWinners());
        assert !view1.lightGame.getRanking().isEmpty();
        assert !view2.lightGame.getRanking().isEmpty();
        assert view1.lightGame.getRanking().contains(firstUserInTurn.getNickname());
        assert view2.lightGame.getRanking().contains(firstUserInTurn.getNickname());
        assert view1.lightGame.getCodexMap().get(firstUserInTurn.getNickname()).getPoints()>=20;
        assert view2.lightGame.getCodexMap().get(firstUserInTurn.getNickname()).getPoints()>=20;

    }*/
}