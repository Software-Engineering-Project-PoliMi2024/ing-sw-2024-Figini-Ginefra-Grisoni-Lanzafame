package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.rmi.RemoteException;
import java.util.Objects;

public class GameListRenderable extends Renderable {
    private final LightLobbyList lightLobbyList;

    public GameListRenderable(String name, CommandPrompt[] relatedCommands, ControllerInterface controller) {
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
                try {
                    controller.joinLobby(answer.getAnswer(0));
                }catch (RemoteException r){
                    r.printStackTrace();
                }
                break;
            case CommandPrompt.CREATE_GAME:
                String lobbyName = answer.getAnswer(0);
                int maxPlayers = Integer.parseInt(answer.getAnswer(1));
                try {
                    controller.createLobby(lobbyName, maxPlayers);
                }catch (RemoteException r){
                    r.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
