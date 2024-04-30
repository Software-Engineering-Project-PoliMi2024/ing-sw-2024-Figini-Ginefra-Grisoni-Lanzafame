package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class GameListRenderable extends Renderable {
    private final LightLobbyList lightLobbyList;

    public GameListRenderable(String name, LightLobbyList lightLobbyList, CommandPrompt[] relatedCommands, ControllerInterface controller) {
        super(name, relatedCommands, controller);
        this.lightLobbyList = lightLobbyList;
    }

    @Override
    public void render() {
        if (lightLobbyList.getLobbies().isEmpty()) {
            PromptStyle.printInABox("No games available", 70);
        } else {
            List<String> lobbyNames = lightLobbyList.getLobbies().stream().map(LightLobby::name).toList();
            PromptStyle.printListInABox("Available games", lobbyNames, 70, 1);
        }
    }

    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()) {
            case CommandPrompt.DISPLAY_GAME_LIST:
                this.render();
                break;
            case CommandPrompt.JOIN_GAME:
                try {
                    controller.joinLobby(answer.getAnswer(0));
                }
                catch (RemoteException e) {
                    System.out.println("Error while joining the game.");
                }
                break;
            case CommandPrompt.CREATE_GAME:
                try {
                    String lobbyName = answer.getAnswer(0);
                    int maxPlayers = Integer.parseInt(answer.getAnswer(1));
                    controller.createLobby(lobbyName, maxPlayers);
                }
                catch (RemoteException e) {
                    System.out.println("Error while creating the game.");
                }
                break;
            default:
                break;
        }
    }
}
