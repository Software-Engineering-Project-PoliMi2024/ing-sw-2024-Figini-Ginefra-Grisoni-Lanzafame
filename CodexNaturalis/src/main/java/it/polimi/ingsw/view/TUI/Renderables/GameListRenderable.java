package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.model.tableReleted.Game;
import java.util.List;

public class GameListRenderable extends Renderable {
    private final List<Game> games;
    private String selectedGameIndex = "";

    public GameListRenderable(List<Game> games) {
        this.games = games;
    }

    @Override
    public void render() {
        if (games.isEmpty()) {
            System.out.println("No games available to join.");
        } else {
            System.out.println("Available games:");
            for (int i = 0; i < games.size(); i++) {
                System.out.println((i + 1) + ". " + games.get(i).getName() + " - Players: " + games.get(i).getGameParty().getUsersList().size() + "/" + games.get(i).getGameParty().getNumberOfMaxPlayer());
            }
        }
    }

    @Override
    public void update() {
        // re-render or clear the field
        render();
    }

    @Override
    public void updateInput(String input) {
        this.selectedGameIndex = input.trim();
    }

    // Getters?
    public String getSelectedGameIndex() {
        return selectedGameIndex;
    }
}
