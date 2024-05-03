package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.model.tableReleted.Lobby;
import org.junit.jupiter.api.Test;
class LobbyDiffPublisherTest {
    /*
    - 2 actors :
        - the first subscribes
        - the second subscribes
    - check: the first has the right lightLobby that is the same of the second
             and contains both names of the actors, the right LobbyName and the right
             number of player
    */
    @Test
    void subscribePlayer() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "roberto";
        view2.name = "baggio";
        Lobby lobby = new Lobby(3,view1.name, "test");

        lobby.subscribe(view1, view1.name, lobby.getLobbyName(), lobby.getNumberOfMaxPlayer());
        lobby.subscribe(view2, view2.name, lobby.getLobbyName(), lobby.getNumberOfMaxPlayer());

        assert  view1.lightLobby.name().equals(lobby.getLobbyName()) &&
                view1.lightLobby.numberMaxPlayer() == lobby.getNumberOfMaxPlayer() &&
                view1.lightLobby.nicknames().contains(view1.name) &&
                view1.lightLobby.nicknames().contains(view2.name) &&

                view2.lightLobby.name().equals(lobby.getLobbyName()) &&
                view2.lightLobby.numberMaxPlayer() == lobby.getNumberOfMaxPlayer() &&
                view2.lightLobby.nicknames().contains(view1.name) &&
                view2.lightLobby.nicknames().contains(view2.name);
    }
    /*
    - 3 actors :
        - the first subscribes
        - the second subscribes
        - the second unsubscribes
        - the third subscribes
    - check: the first has the right user list that is the same user list
             of the third that contains the name of the first and the third actor,
             but not of the second who had unsubscribed
    - check: the second actor who had unsubscribe has the lobby in the reset state
    */
    @Test
    void unsubscribe() {
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        view1.name = "roberto";
        view2.name = "baggio";
        view3.name = "lime";
        Lobby lobby = new Lobby(3,view1.name, "test");

        lobby.subscribe(view1, view1.name, lobby.getLobbyName(), lobby.getNumberOfMaxPlayer());
        lobby.subscribe(view2, view3.name, lobby.getLobbyName(), lobby.getNumberOfMaxPlayer());
        lobby.unsubscribe(view2);
        lobby.subscribe(view3, view3.name, lobby.getLobbyName(), lobby.getNumberOfMaxPlayer());

        assert  view1.lightLobby.nicknames().contains(view1.name) &&
                view1.lightLobby.nicknames().contains(view3.name) &&
                !view1.lightLobby.nicknames().contains(view2.name) &&

                view3.lightLobby.nicknames().contains(view1.name) &&
                view3.lightLobby.nicknames().contains(view3.name) &&
                !view3.lightLobby.nicknames().contains(view2.name) &&

                view2.lightLobby.name() == null &&
                view2.lightLobby.numberMaxPlayer() == 0 &&
                view2.lightLobby.nicknames().isEmpty();
    }
}