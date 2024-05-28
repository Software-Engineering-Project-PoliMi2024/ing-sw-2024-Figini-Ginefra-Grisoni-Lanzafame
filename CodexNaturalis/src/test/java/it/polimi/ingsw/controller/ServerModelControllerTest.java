package it.polimi.ingsw.controller;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.lightModel.LightModelConfig;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffPublishers.ViewTest;
import it.polimi.ingsw.lightModel.lightPlayerRelated.*;
import it.polimi.ingsw.lightModel.lightTableRelated.LightDeck;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGameParty;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.cards.Card;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.DrawableCard;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewState;
import javafx.geometry.Pos;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class ServerModelControllerTest {
    @Test
    void badLogins(){
        MultiGame multiGame = new MultiGame();
        ViewTest view1 = new ViewTest();
        view1.name = "pippo";
        ViewTest view2 = new ViewTest();
        view2.name = "pippo";
        ViewTest view3 = new ViewTest();
        view3.name = "";
        ViewTest view4 = new ViewTest();
        view4.name = "                ";
        String LobbyCreatorName = "lobbyCreator";
        List<ViewTest> views = Arrays.stream(new ViewTest[]{view2, view3, view4}).toList();
        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);
        ServerModelController serverModelController3 = new ServerModelController(multiGame, view3);
        ServerModelController serverModelController4 = new ServerModelController(multiGame, view4);

        serverModelController1.login(view1.name);
        serverModelController2.login(view2.name);
        serverModelController3.login(view3.name);
        serverModelController4.login(view4.name);

        for(ViewTest view : views){
            System.out.println(view.name);
            assert view.state.equals(ViewState.LOGIN_FORM);
        }
    }

    @Test
    void loginJoiningLobbyList() {
        MultiGame multiGame = new MultiGame();
        ViewTest view1 = new ViewTest();
        view1.name = "pippo";
        ViewTest view2 = new ViewTest();
        view2.name = "pluto";
        String LobbyCreatorName = "lobbyCreator";
        List<ViewTest> views = Arrays.stream(new ViewTest[]{view1, view2}).toList();
        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);

        //view joins before adding lobbies
        serverModelController1.login(view1.name);
        //add lobby
        Lobby lobby1 = new Lobby(2, LobbyCreatorName, "lobby1");
        multiGame.addLobby(lobby1);
        multiGame.notifyNewLobby(LobbyCreatorName, lobby1);
        //view joins after adding lobbies
        serverModelController2.login(view2.name);

        for(ViewTest view : views) {
            System.out.println(view.name);
            assert view.lightLobbyList.getLobbies().contains(Lightifier.lightify(lobby1));
            assert multiGame.getUsernames().contains(view.name);
            assert view.state.equals(ViewState.JOIN_LOBBY);
        }
    }

    @Test
    void createLobby() {
        MultiGame multiGame = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();

        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);
        ServerModelController serverModelController3 = new ServerModelController(multiGame, view3);

        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "topolino";
        String lobbyName1 = "test1";
        String lobbyName2 = "test2";

        serverModelController1.login(view1.name);
        serverModelController1.createLobby(lobbyName1, 2);
        serverModelController3.login(view3.name);
        serverModelController2.login(view2.name);
        serverModelController2.createLobby(lobbyName2, 2);

        assert multiGame.getLobbyByName(lobbyName1) != null;
        assert multiGame.getLobbyByName(lobbyName2) != null;
        assert view1.lightLobbyList.getLobbies().isEmpty();
        assert view2.lightLobbyList.getLobbies().isEmpty();
        assert view3.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().contains(lobbyName1);
        assert view3.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().contains(lobbyName2);
    }

    @Test
    void malevolentCreateLobby(){
        MultiGame multiGame = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();

        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);
        ServerModelController serverModelController3 = new ServerModelController(multiGame, view3);

        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "topolino";
        String lobbyName1 = "test1";
        String lobbyName2 = "test2";

        serverModelController3.login(view3.name);
        //create a lobby without logginIn
        System.out.println(serverModelController1.getNickname());
        serverModelController1.createLobby(lobbyName1, 2);
        //the same user creates two lobbies
        serverModelController2.login(view2.name);
        serverModelController2.createLobby(lobbyName2, 2);
        serverModelController2.createLobby(lobbyName1, 2);

        assert multiGame.getLobbyByName(lobbyName1) == null;
        assert multiGame.getLobbyByName(lobbyName2) == null;
        assert view3.lightLobbyList.getLobbies().isEmpty();
    }

    @Test
    void malevolentJoinLobby(){
        MultiGame multiGame = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();

        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);
        ServerModelController serverModelController3 = new ServerModelController(multiGame, view3);

        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "topolino";

        String lobbyName1 = "test1";
        String lobbyName2 = "test2";

        serverModelController1.login(view1.name);
        serverModelController1.createLobby(lobbyName1, 3);
        serverModelController2.login(view2.name);
        serverModelController2.createLobby(lobbyName2, 3);
        serverModelController2.joinLobby(lobbyName1);
        serverModelController3.joinLobby(lobbyName1);

        assert view1.lightLobby.nicknames().contains(view1.name);
        assert !view1.lightLobby.nicknames().contains(view2.name);
        assert !view1.lightLobby.nicknames().contains(view3.name);
    }

    @Test
    void joinLobby() {
        MultiGame multiGame = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();


        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);

        view1.name = "pippo";
        view2.name = "pluto";

        String lobbyName1 = "test1";

        serverModelController1.login(view1.name);
        serverModelController1.createLobby(lobbyName1, 3);
        serverModelController2.login(view2.name);
        serverModelController2.joinLobby(lobbyName1);

        assert multiGame.getGameFromUserNick(view1.name) == null;
        assert multiGame.getLobbyByName(lobbyName1) != null;
        assert multiGame.getLobbyByName(lobbyName1).getLobbyPlayerList().contains(view1.name);
        assert multiGame.getLobbyByName(lobbyName1).getLobbyPlayerList().contains(view2.name);
        assert multiGame.getLobbyByName(lobbyName1).getLobbyPlayerList().size() == 2;

        for(ViewTest view : Arrays.stream(new ViewTest[]{view1, view2}).toList()){
            System.out.println(view.name);
            assert view.lightLobbyList.getLobbies().isEmpty();
            assert view.lightLobby.nicknames().contains(view1.name);
            assert view.lightLobby.nicknames().contains(view2.name);
            assert view.lightLobby.nicknames().size() == 2;
            assert view.lightLobby.numberMaxPlayer() == 3;
            assert view.lightLobby.name().equals(lobbyName1);
            assert view.state.equals(ViewState.LOBBY);
        }
    }

    @Test
    void joinLobbyAndStartGame(){
        MultiGame multiGame = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();

        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);
        ServerModelController serverModelController3 = new ServerModelController(multiGame, view3);

        view1.name = "pippo";
        view2.name = "pluto";
        view3.name = "topolino";

        String lobbyName1 = "test1";

        serverModelController3.login(view3.name);
        serverModelController1.login(view1.name);
        serverModelController1.createLobby(lobbyName1, 2);
        serverModelController2.login(view2.name);
        serverModelController2.joinLobby(lobbyName1);

        //game created and created correctly
        assert multiGame.getGameByName(lobbyName1) != null;
        Game game = multiGame.getGameByName(lobbyName1);
        assert multiGame.getLobbyByName(lobbyName1) == null;
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
        assert cardInHandThatIsNotNull.idFront() == multiGame.getUserFromNick(view1.name).getUserHand().getStartCard().getIdFront();
        assert cardInHandThatIsNotNull.idBack() == multiGame.getUserFromNick(view1.name).getUserHand().getStartCard().getIdBack();
        assert view1.lightGame.getHand().getCardPlayability().values().size() == 1;
        assert view1.lightGame.getHand().getCardPlayability().get(cardInHandThatIsNotNull) == true;
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
        assert cardInHandThatIsNotNull.idFront() == multiGame.getUserFromNick(view2.name).getUserHand().getStartCard().getIdFront();
        assert cardInHandThatIsNotNull.idBack() == multiGame.getUserFromNick(view2.name).getUserHand().getStartCard().getIdBack();
        assert view2.lightGame.getHand().getCardPlayability().values().size() == 1;
        assert view2.lightGame.getHand().getCardPlayability().get(cardInHandThatIsNotNull) == true;
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
        MultiGame multiGame = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();

        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);
        ServerModelController serverModelController3 = new ServerModelController(multiGame, view3);
        ServerModelController serverModelController4 = new ServerModelController(multiGame, view4);

        view1.name = "giorgia";
        view2.name = "meloni";
        view3.name = "salvini";
        view4.name = "renzi";
        view5.name = "draghi";
        String lobbyName1 = "test1";
        String lobbyName2 = "test2";

        serverModelController1.login(view1.name);
        serverModelController1.createLobby(lobbyName1, 3);
        serverModelController2.login(view2.name);
        serverModelController2.joinLobby(lobbyName1);
        serverModelController2.leaveLobby();
        serverModelController3.login(view3.name);
        serverModelController3.joinLobby(lobbyName1);
        serverModelController4.login(view4.name);
        serverModelController4.createLobby(lobbyName2, 2);
        serverModelController4.leaveLobby();

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

        assert multiGame.getLobbyByName(lobbyName1).getLobbyPlayerList().contains(view1.name);
        assert multiGame.getLobbyByName(lobbyName1).getLobbyPlayerList().contains(view3.name);
        assert !multiGame.getLobbyByName(lobbyName1).getLobbyPlayerList().contains(view2.name);
        assert multiGame.getGameByName(lobbyName2) == null;
    }

    @Test
    void selectStartCardFace() {
        MultiGame multiGame = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();

        view1.name = "pippo";
        view2.name = "pluto";

        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);

        String lobbyName1 = "test1";

        serverModelController1.login(view1.name);
        serverModelController1.createLobby(lobbyName1, 2);
        serverModelController2.login(view2.name);
        serverModelController2.joinLobby(lobbyName1);
        //game started
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        serverModelController1.place(startPlacement);

        //check model
        Game game = multiGame.getGameFromUserNick(view1.name);
        User user1 = game.getUserFromNick(view1.name);
        User user2 = game.getUserFromNick(view2.name);
        //user1
        assert user1.hasPlacedStartCard();
        assert !game.othersHadAllPlacedStartCard(view1.name);
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
        assert game.othersHadAllPlacedStartCard(view2.name);
    }

    @Test
    void lastPlayerSelectStartCard(){
        MultiGame multiGame = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();

        view1.name = "pippo";
        view2.name = "pluto";

        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);

        String lobbyName1 = "test1";

        serverModelController1.login(view1.name);
        serverModelController1.createLobby(lobbyName1, 2);
        serverModelController2.login(view2.name);
        serverModelController2.joinLobby(lobbyName1);
        //game started
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        serverModelController1.place(startPlacement1);
        startCard = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        serverModelController2.place(startPlacement2);

        //check model
        Game game = multiGame.getGameFromUserNick(view1.name);
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

        assert cardsOnServer1.size() == 3;
        assert cardsOnServer1.stream().allMatch(Objects::nonNull);
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
        LightHand lightHand2 = view1.lightGame.getHand();
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
        MultiGame multiGame = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();

        view1.name = "pippo";
        view2.name = "pluto";

        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);

        String lobbyName1 = "test1";

        serverModelController1.login(view1.name);
        serverModelController1.createLobby(lobbyName1, 2);
        serverModelController2.login(view2.name);
        serverModelController2.joinLobby(lobbyName1);
        //choose StartCard
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        serverModelController1.place(startPlacement1);
        startCard = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        serverModelController2.place(startPlacement2);
        //choose secret objective
        LightCard secretObjective = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        serverModelController1.choseSecretObjective(secretObjective);

        //model
        Game game = multiGame.getGameFromUserNick(view1.name);
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
        MultiGame multiGame = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();

        view1.name = "pippo";
        view2.name = "pluto";

        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);

        String lobbyName1 = "test1";

        serverModelController1.login(view1.name);
        serverModelController1.createLobby(lobbyName1, 2);
        serverModelController2.login(view2.name);
        serverModelController2.joinLobby(lobbyName1);
        //choose StartCard
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        serverModelController1.place(startPlacement1);
        startCard = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        serverModelController2.place(startPlacement2);
        //choose secret objective
        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        serverModelController1.choseSecretObjective(secretObjective1);
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];
        serverModelController2.choseSecretObjective(secretObjective2);
        //model
        Game game = multiGame.getGameFromUserNick(view1.name);
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
        MultiGame multiGame = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();

        view1.name = "pippo";
        view2.name = "pluto";

        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);

        String lobbyName1 = "test1";

        serverModelController1.login(view1.name);
        serverModelController1.createLobby(lobbyName1, 2);
        serverModelController2.login(view2.name);
        serverModelController2.joinLobby(lobbyName1);
        //choose StartCard
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        serverModelController1.place(startPlacement1);
        startCard = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        serverModelController2.place(startPlacement2);
        //choose secret objective
        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        serverModelController1.choseSecretObjective(secretObjective1);
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];
        serverModelController2.choseSecretObjective(secretObjective2);
        //place
        Game game = multiGame.getGameFromUserNick(view1.name);
        User user1 = game.getUserFromNick(view1.name);
        User user2 = game.getUserFromNick(view2.name);
        game.setPlayerIndex(game.getUsersList().lastIndexOf(user1));
        CardInHand cardPlaced = user1.getUserHand().getHand().stream().filter(Objects::nonNull).toList().getFirst();
        Position position = user1.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement = new LightPlacement(position, Lightifier.lightifyToCard(cardPlaced), CardFace.FRONT);
        serverModelController1.place(placement);

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
        assert !Arrays.stream(view2.lightGame.getHandOthers().get(view1.name).getCards()).toList().contains(Lightifier.lightifyToBack(cardPlaced));
        assert lightCodex1on2.getPlacementHistory().contains(placement);
        assert !lightCodex1on2.getFrontier().frontier().contains(position);
    }

    @Test
    void drawFromDeck() {
        MultiGame multiGame = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();

        view1.name = "pippo";
        view2.name = "pluto";

        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);

        String lobbyName1 = "test1";

        serverModelController1.login(view1.name);
        serverModelController1.createLobby(lobbyName1, 2);
        serverModelController2.login(view2.name);
        serverModelController2.joinLobby(lobbyName1);
        //choose StartCard
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        serverModelController1.place(startPlacement1);
        startCard = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        serverModelController2.place(startPlacement2);
        //choose secret objective
        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        serverModelController1.choseSecretObjective(secretObjective1);
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];
        serverModelController2.choseSecretObjective(secretObjective2);
        //place
        Game game = multiGame.getGameFromUserNick(view1.name);
        User user1 = game.getUserFromNick(view1.name);
        User user2 = game.getUserFromNick(view2.name);
        game.setPlayerIndex(game.getUsersList().lastIndexOf(user1));
        CardInHand cardPlaced = user1.getUserHand().getHand().stream().filter(Objects::nonNull).toList().getFirst();
        Position position = user1.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement = new LightPlacement(position, Lightifier.lightifyToCard(cardPlaced), CardFace.FRONT);
        serverModelController1.place(placement);
        CardInHand card = game.getGoldCardDeck().showTopCardOfDeck();
        serverModelController1.draw(DrawableCard.GOLDCARD, Configs.actualDeckPos);

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
        MultiGame multiGame = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();

        view1.name = "pippo";
        view2.name = "pluto";

        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);

        String lobbyName1 = "test1";

        serverModelController1.login(view1.name);
        serverModelController1.createLobby(lobbyName1, 2);
        serverModelController2.login(view2.name);
        serverModelController2.joinLobby(lobbyName1);
        //choose StartCard
        LightCard startCard = view1.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement1 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        serverModelController1.place(startPlacement1);
        startCard = view2.lightGame.getHand().getCards()[0];
        LightPlacement startPlacement2 = new LightPlacement(new Position(0,0), startCard, CardFace.FRONT);
        serverModelController2.place(startPlacement2);
        //choose secret objective
        LightCard secretObjective1 = view1.lightGame.getHand().getSecretObjectiveOptions()[0];
        serverModelController1.choseSecretObjective(secretObjective1);
        LightCard secretObjective2 = view2.lightGame.getHand().getSecretObjectiveOptions()[0];
        serverModelController2.choseSecretObjective(secretObjective2);
        //place
        Game game = multiGame.getGameFromUserNick(view1.name);
        User user1 = game.getUserFromNick(view1.name);
        User user2 = game.getUserFromNick(view2.name);
        game.setPlayerIndex(game.getUsersList().lastIndexOf(user1));
        CardInHand cardPlaced = user1.getUserHand().getHand().stream().filter(Objects::nonNull).toList().getFirst();
        Position position = user1.getUserCodex().getFrontier().getFrontier().getFirst();
        LightPlacement placement = new LightPlacement(position, Lightifier.lightifyToCard(cardPlaced), CardFace.FRONT);
        serverModelController1.place(placement);
        CardInHand card = game.getGoldCardDeck().showTopCardOfDeck();
        serverModelController1.draw(DrawableCard.GOLDCARD, 0);

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
}