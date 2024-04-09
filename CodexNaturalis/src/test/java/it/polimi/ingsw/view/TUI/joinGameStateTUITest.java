package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.tableReleted.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class joinGameStateTUITest {

    @Test
    void run() {
        Game game = new Game("game1", 4);
        ArrayList<Game> games = new ArrayList<>();
        games.add(game);
        joinGameStateTUI.run(games);
    }
}