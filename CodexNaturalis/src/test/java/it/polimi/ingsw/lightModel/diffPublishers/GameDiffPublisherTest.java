package it.polimi.ingsw.lightModel.diffPublishers;

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

        game.subcribe(view1, view1.name);
        game.subcribe(view2, view2.name);

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

        ArrayList<String> namesActive = new ArrayList<>();
        namesActive.add(view1.name);
        namesActive.add(view3.name);

        game.subcribe(view1, view1.name);
        game.subcribe(view2, view2.name);
        game.unsubscrive(view2);
        game.subcribe(view3, view3.name);

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