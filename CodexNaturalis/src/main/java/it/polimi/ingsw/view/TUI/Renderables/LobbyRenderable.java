package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterfaceClient;
import it.polimi.ingsw.lightModel.LightLobby;
import it.polimi.ingsw.model.tableReleted.GameParty;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.List;

public class LobbyRenderable extends Renderable {
    private LightLobby lightLobby;
    private String userInput = "";

    public LobbyRenderable(String name, LightLobby lightLobby, CommandPrompt[] relatedCommands, ControllerInterfaceClient controller) {
        super(name, relatedCommands, controller);
        this.lightLobby = lightLobby;
    }

    @Override
    public void render() {
        System.out.println("=====================================================================");
        System.out.println("Lobby of the game: " + lightLobby.name());
        System.out.println("=====================================================================");
        System.out.println("Players:");
        List<String> nicknames = lightLobby.nicknames();
        for (int i = 0; i < nicknames.size(); i++) {
            System.out.println("\t[" + (i) + "] - " + nicknames.get(i));
        }
        System.out.println("=====================================================================");
    }

    @Override
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()) {
            case CommandPrompt.DISPLAY_LOBBY:
                this.render();
                break;
            default:
                break;
        }
    }
}
