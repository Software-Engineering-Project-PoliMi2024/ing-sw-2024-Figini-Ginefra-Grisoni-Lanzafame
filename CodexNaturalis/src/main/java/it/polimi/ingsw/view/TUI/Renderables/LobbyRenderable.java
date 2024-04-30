package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.rmi.RemoteException;
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
        List<String> nicknames = lightLobby.nicknames();
        PromptStyle.printListInABox("Lobby", nicknames, 70, 1);
    }

    @Override
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()) {
            case CommandPrompt.DISPLAY_LOBBY:
                this.render();
                break;
            case CommandPrompt.LEAVE_LOBBY:
                try {
                    controller.leaveLobby();
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
