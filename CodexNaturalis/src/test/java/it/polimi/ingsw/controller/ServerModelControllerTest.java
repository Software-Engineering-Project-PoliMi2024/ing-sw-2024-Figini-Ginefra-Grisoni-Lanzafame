package it.polimi.ingsw.controller;

import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffPublishers.ViewTest;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightDeck;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.cards.Card;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.playerReleted.Position;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

class ServerModelControllerTest {

    @Test
    void login() {
        MultiGame multiGame = new MultiGame();
        ViewTest view1 = new ViewTest();
        view1.name = "giorgia";

        ViewTest view2 = new ViewTest();
        view2.name = "meloni";
        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);

        Lobby lobby1 = new Lobby(2, view1.name, "lobby1");
        Lobby lobby2 = new Lobby(2, view1.name, "lobby2");

        multiGame.addLobby(lobby1);
        serverModelController1.login(view1.name);

        multiGame.addLobby(lobby2);
        ArrayList<LightLobby> lobbies = new ArrayList<>();
        lobbies.add(Lightifier.lightify(lobby2));
        serverModelController2.login(view2.name);


        assert view1.lightLobbyList.getLobbies().contains(Lightifier.lightify(lobby1));
        assert view1.lightLobbyList.getLobbies().contains(Lightifier.lightify(lobby2));
        assert view2.lightLobbyList.getLobbies().contains(Lightifier.lightify(lobby1));
        assert view2.lightLobbyList.getLobbies().contains(Lightifier.lightify(lobby2));
        assert multiGame.getUsernames().contains(view1.name);
        assert multiGame.getUsernames().contains(view2.name);
        assert serverModelController1.getNickname().equals(view1.name);
        assert serverModelController2.getNickname().equals(view2.name);
    }
    @Test
    void createLobby() {
        MultiGame multiGame = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();

        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);

        view1.name = "gino";
        view2.name = "gianni";
        String lobbyName1 = "test1";
        String lobbyName2 = "test2";

        serverModelController1.login(view1.name);
        serverModelController1.createLobby(lobbyName1, 2);
        serverModelController2.login(view2.name);
        serverModelController1.createLobby(lobbyName2, 2);


        System.out.println(serverModelController1.getNickname());
        System.out.println(serverModelController2.getNickname());
        for (LightLobby l : view2.lightLobbyList.getLobbies()) {
            System.out.println("#" + l.name());
        }

        assert multiGame.getLobbyByName(lobbyName1) != null;
        assert multiGame.getLobbyByName(lobbyName2) != null;
        assert view1.lightLobbyList.getLobbies().isEmpty();
        assert view2.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().contains(lobbyName1);
        assert view2.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().contains(lobbyName2);

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

        assert multiGame.getUserGame(view1.name) == null;
        assert multiGame.getLobbyByName(lobbyName1) != null;
        assert multiGame.getLobbyByName(lobbyName1).getLobbyPlayerList().contains(view1.name);
        assert multiGame.getLobbyByName(lobbyName1).getLobbyPlayerList().contains(view2.name);
        assert multiGame.getLobbyByName(lobbyName1).getLobbyPlayerList().size() == 2;

        assert view1.lightLobbyList.getLobbies().isEmpty();
        assert view1.lightLobby.name().equals(lobbyName1);
        assert view1.lightLobby.nicknames().contains(view1.name);
        assert view1.lightLobby.nicknames().contains(view2.name);
        assert view1.lightLobby.nicknames().size() == 2;
        assert view1.lightLobby.numberMaxPlayer() == 3;

        assert view2.lightLobbyList.getLobbies().isEmpty();
        assert view2.lightLobby.name().equals(lobbyName1);
        assert view2.lightLobby.nicknames().contains(view1.name);
        assert view2.lightLobby.nicknames().contains(view2.name);
        assert view2.lightLobby.nicknames().size() == 2;
        assert view2.lightLobby.numberMaxPlayer() == 3;

    }

    @Test
    void joinLobbyAndStartGame(){
        MultiGame multiGame = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();


        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);

        view1.name = "pippo";
        view2.name = "pluto";

        String lobbyName1 = "test1";

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
        assert view1.lightLobby.name() == lobbyName1;

        assert view3.lightLobby.nicknames().contains(view1.name);
        assert !view3.lightLobby.nicknames().contains(view2.name);
        assert view3.lightLobby.nicknames().contains(view3.name);
        assert view3.lightLobby.numberMaxPlayer() == 3;
        assert view3.lightLobby.name() == lobbyName1;

        assert view2.lightLobby.nicknames().isEmpty();
        assert view2.lightLobby.numberMaxPlayer() == 0;
        assert view2.lightLobby.name() == "";

        assert view4.lightLobby.nicknames().isEmpty();
        assert view4.lightLobby.numberMaxPlayer() == 0;
        assert view4.lightLobby.name() == "";

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
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();

        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);

        view1.name = "giorgia";
        view2.name = "meloni";
        view3.name = "salvini";
        view4.name = "renzi";
        view5.name = "draghi";
        String lobbyName1 = "test1";

        serverModelController1.login(view1.name);
        serverModelController1.createLobby(lobbyName1, 2);
        serverModelController2.login(view2.name);
        serverModelController2.joinLobby(lobbyName1);

        System.out.println(view1.lightGame.getCodexMap().keySet());

        //check common objective
        //2 test: 1) simple start card chosen check, 2) start card chosen by the last player
        //remove currentPlayer index and test new implementation from GameParty
    }

    @Test
    void choseSecretObjective() {

    }

    @Test
    void place() {
    }

    @Test
    void draw() {
    }
}