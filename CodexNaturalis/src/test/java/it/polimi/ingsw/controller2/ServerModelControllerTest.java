package it.polimi.ingsw.controller2;

import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffPublishers.ViewTest;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiffEdit;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Lobby;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

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
        try{
        serverModelController1.login(view1.name);
        }catch (RemoteException e){
            e.printStackTrace();
        }
        multiGame.addLobby(lobby2);
        ArrayList<LightLobby> lobbies = new ArrayList<>();
        lobbies.add(Lightifier.lightify(lobby2));
        try {
            serverModelController2.login(view2.name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

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

        view1.name = "giorgia";
        view2.name = "meloni";
        String lobbyName1 = "test1";
        String lobbyName2 = "test2";

        try {
            serverModelController1.login(view1.name);
            serverModelController1.createLobby(lobbyName1, 2);
            serverModelController2.login(view2.name);
            serverModelController1.createLobby(lobbyName2, 2);
        }catch (RemoteException r){
            r.printStackTrace();
        }

        System.out.println(serverModelController1.getNickname());
        System.out.println(serverModelController2.getNickname());
        for(LightLobby l : view2.lightLobbyList.getLobbies()){
            System.out.println("#"+l.name());
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
        ViewTest view3 = new ViewTest();
        ViewTest view4 = new ViewTest();
        ViewTest view5 = new ViewTest();

        ServerModelController serverModelController1 = new ServerModelController(multiGame, view1);
        ServerModelController serverModelController2 = new ServerModelController(multiGame, view2);
        ServerModelController serverModelController3 = new ServerModelController(multiGame, view3);
        ServerModelController serverModelController4 = new ServerModelController(multiGame, view4);
        ServerModelController serverModelController5 = new ServerModelController(multiGame, view5);

        view1.name = "giorgia";
        view2.name = "meloni";
        view3.name = "salvini";
        view4.name = "renzi";
        view5.name = "draghi";
        String lobbyName1 = "test1";
        String lobbyName2 = "test2";

        try {
            serverModelController1.login(view1.name);
            serverModelController1.createLobby(lobbyName1, 3);
            serverModelController2.login(view2.name);
            serverModelController2.joinLobby(lobbyName1);

            serverModelController3.login(view3.name);
            serverModelController3.createLobby(lobbyName2, 2);
            serverModelController4.login(view4.name);
            serverModelController5.login(view5.name);
            serverModelController5.joinLobby(lobbyName2);
        } catch (RemoteException r) {
            r.printStackTrace();
        }


        assert view1.lightLobbyList.getLobbies().isEmpty();
        assert view2.lightLobbyList.getLobbies().isEmpty();
        assert view3.lightLobbyList.getLobbies().isEmpty();
        assert view4.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().contains(lobbyName1);
        assert !view4.lightLobbyList.getLobbies().stream().map(LightLobby::name).toList().contains(lobbyName2);

        assert view1.lightLobby.nicknames().contains(view1.name);
        assert view1.lightLobby.nicknames().contains(view2.name);
        assert !view1.lightLobby.nicknames().contains(view3.name);
        assert view1.lightLobby.numberMaxPlayer() == 3;
        assert view1.lightLobby.name() == lobbyName1;

        assert view2.lightLobby.nicknames().contains(view1.name);
        assert view2.lightLobby.nicknames().contains(view2.name);
        assert !view2.lightLobby.nicknames().contains(view3.name);
        assert view2.lightLobby.numberMaxPlayer() == 3;
        assert view2.lightLobby.name() == lobbyName1;

        assert view3.lightLobby.numberMaxPlayer() == 0;
        assert view3.lightLobby.nicknames().isEmpty();
        assert view3.lightLobby.name() == null;

        assert view5.lightLobby.numberMaxPlayer() == 0;
        assert view5.lightLobby.nicknames().isEmpty();
        assert view5.lightLobby.name() == null;

        assert multiGame.getLobbyByName(lobbyName1) != null;
        assert multiGame.getLobbyByName(lobbyName2) == null;
        assert multiGame.getLobbyByName(lobbyName1).getLobbyPlayerList().contains(view1.name);
        assert multiGame.getLobbyByName(lobbyName1).getLobbyPlayerList().contains(view2.name);
        assert multiGame.getGameByName(lobbyName2) != null;
        assert multiGame.getGameByName(lobbyName2).getGameParty().getUsersList().stream().map(User::getNickname).toList().contains(view3.name);
        assert multiGame.getGameByName(lobbyName2).getGameParty().getUsersList().stream().map(User::getNickname).toList().contains(view5.name);
        assert multiGame.getGameByName(lobbyName2).getGameParty().getNumberOfMaxPlayer() == 2;

        assert multiGame.getGameByName(lobbyName2).getGameParty().getUsersList().stream().map(User::getUserHand).map(Hand::getStartCard).toList().size() == 2;
        assert view3.lightGame.getHand().getCards()[0].id() != 0;
        assert view3.lightGame.getHand().getCards()[1] == null;
        assert view3.lightGame.getHand().getCards()[2] == null;
        assert view5.lightGame.getHand().getCards()[0].id() != 0;
        assert view5.lightGame.getHand().getCards()[1] == null;
        assert view5.lightGame.getHand().getCards()[2] == null;

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

        try {
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
        } catch (RemoteException r) {
            r.printStackTrace();
        }

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
        assert view2.lightLobby.name() == null;

        assert view4.lightLobby.nicknames().isEmpty();
        assert view4.lightLobby.numberMaxPlayer() == 0;
        assert view4.lightLobby.name() == null;

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

        try{
            serverModelController1.login(view1.name);
            serverModelController1.createLobby(lobbyName1, 2);
            serverModelController2.login(view2.name);
            serverModelController2.joinLobby(lobbyName1);
            serverModelController1.selectStartCardFace(CardFace.FRONT);
        }catch (RemoteException r){
            r.printStackTrace();
        }

        System.out.println(view1.lightGame.getCodexMap().keySet());
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