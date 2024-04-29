package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.List;

public class LobbyRenderable extends Renderable {
    private LightLobby lightLobby;
    private String userInput = "";

    public LobbyRenderable(String name, LightLobby lightLobby, CommandPrompt[] relatedCommands, ControllerInterface controller) {
        super(name, relatedCommands, controller);
        this.lightLobby = lightLobby;
    }

    @Override
    public void render() {
        PromptStyle.printInABox("Lobby", 70);
        List<String> nicknames = lightLobby.nicknames();
        for (String nickname : nicknames) {
            PromptStyle.printBetweenSeparators(nickname, 70);
        }
        PromptStyle.printSeparator(72);
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
