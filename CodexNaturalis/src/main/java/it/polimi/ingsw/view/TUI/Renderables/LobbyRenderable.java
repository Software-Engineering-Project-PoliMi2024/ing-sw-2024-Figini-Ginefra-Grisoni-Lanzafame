package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.model.tableReleted.GameParty;
import it.polimi.ingsw.model.playerReleted.User;

import java.util.List;

public class LobbyRenderable extends Renderable {
    private GameParty gameParty;
    private String userInput = "";

    public LobbyRenderable(GameParty gameParty) {
        this.gameParty = gameParty;
    }

    @Override
    public void render() {
        if (gameParty == null || gameParty.getUsersList().isEmpty()) {
            System.out.println("Waiting for players...");
        } else {
            System.out.println("Current lobby for game: " + gameParty.getGameName()); //where to retrieve name, missing method in game party ??
            System.out.println("Players in lobby:");
            List<User> users = gameParty.getUsersList();
            for (User user : users) {
                System.out.println("- " + user.getNickname());
            }
            System.out.println("Maximum players: " + gameParty.getNumberOfMaxPlayer());
        }
    }

    @Override
    public void update() {
        // re-render or clear the field
        render();
    }

    @Override
    public void updateInput(String input) {
        this.userInput = input.trim();
    }

    public String getUserInput() {
        return userInput;
    }
}
