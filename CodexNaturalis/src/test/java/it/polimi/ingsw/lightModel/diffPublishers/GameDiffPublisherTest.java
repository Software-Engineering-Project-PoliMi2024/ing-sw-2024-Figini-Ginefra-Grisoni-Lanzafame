package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.controller.ServerModelController;
import it.polimi.ingsw.model.MultiGame;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class GameDiffPublisherTest {

    @Test
    void subscribe() {
        MultiGame games = new MultiGame();

        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        view1.name = "rob";
        view2.name = "bob";
        Lobby lobby = new Lobby(3, view1.name, "rob's");
        Game game = games.createGame(lobby);
        ServerModelController controller1 = new ServerModelController(games, view1);
        ServerModelController controller2 = new ServerModelController(games, view2);

        game.subscribe(controller1, view1.name);
        game.subscribe(controller2, view2.name);

        ArrayList<String> names = new ArrayList<>();
        names.add(view1.name);
        names.add(view2.name);
        assert  view1.lightGame.getLightGameParty().getGameName().equals(game.getName()) &&
                view1.lightGame.getLightGameParty().getPlayerActiveList().keySet().containsAll(names) &&
                view2.lightGame.getLightGameParty().getGameName().equals(game.getName()) &&
                view2.lightGame.getLightGameParty().getPlayerActiveList().keySet().containsAll(names);
    }

    @Test
    void unsubscribe() {
        MultiGame games = new MultiGame();
        ViewTest view1 = new ViewTest();
        ViewTest view2 = new ViewTest();
        ViewTest view3 = new ViewTest();
        view1.name = "rob";
        view2.name = "bob";
        view3.name = "cob";
        Lobby lobby = new Lobby(3, view1.name, "rob's");
        Game game = games.createGame(lobby);
        ServerModelController controller1 = new ServerModelController(games, view1);
        ServerModelController controller2 = new ServerModelController(games, view2);
        ServerModelController controller3 = new ServerModelController(games, view3);

        ArrayList<String> namesActive = new ArrayList<>();
        namesActive.add(view1.name);
        namesActive.add(view3.name);

        game.subscribe(controller1, view1.name);
        game.subscribe(controller2, view2.name);
        game.unsubscribe(controller2);
        game.subscribe(controller3, view3.name);

        assert  view1.lightGame.getLightGameParty().getGameName().equals(game.getName()) &&
                view1.lightGame.getLightGameParty().getPlayerActiveList().keySet().containsAll(namesActive) &&
                view3.lightGame.getLightGameParty().getGameName().equals(game.getName()) &&
                view3.lightGame.getLightGameParty().getPlayerActiveList().keySet().containsAll(namesActive) &&
                view2.lightGame.getLightGameParty().getGameName() == null
                &&
                !view2.lightGame.getLightGameParty().getPlayerActiveList().containsKey(view1.name) &&
                !view2.lightGame.getLightGameParty().getPlayerActiveList().containsKey(view2.name) &&
                !view2.lightGame.getLightGameParty().getPlayerActiveList().containsKey(view3.name);
    }
}