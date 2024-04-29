package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterfaceClient;
import it.polimi.ingsw.lightModel.LightLobbyList;
import it.polimi.ingsw.lightModel.diffs.LobbyListDiff;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public class GameListRenderable extends Renderable {
    private final LightLobbyList lightLobbyList;

    public GameListRenderable(String name, CommandPrompt[] relatedCommands, ControllerInterfaceClient controller) {
        super(name, relatedCommands, controller);
        this.lightLobbyList = new LightLobbyList();
    }

    @Override
    public void render() {
        if (lightLobbyList.getLobbies().isEmpty()) {
            System.out.println("No games available to join.");
        } else {
            System.out.println("Available games:");
            lightLobbyList.getLobbies().forEach(lobby -> {
                System.out.println("Game: " + lobby.nicknames());
                for(int i = 0; i < lobby.nicknames().size(); i++)
                    System.out.println("\t[" + i + "] " + lobby.nicknames().get(i));
            });
        }
    }

    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()) {
            case CommandPrompt.DISPLAY_GAME_LIST:
                this.render();
                break;
            case CommandPrompt.JOIN_GAME:
                int lobbyIndex = Integer.parseInt(answer.getAnswer(0));
                controller.joinLobby(lightLobbyList.getLobbies().get(lobbyIndex).name());
                break;
            case CommandPrompt.CREATE_GAME:
                String lobbyName = answer.getAnswer(0);
                int maxPlayers = Integer.parseInt(answer.getAnswer(1));
                controller.createLobby(lobbyName, maxPlayers);
                break;
            default:
                break;
        }
    }
}
