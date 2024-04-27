package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.model.lightModel.LightLobbyList;
import it.polimi.ingsw.model.lightModel.diffs.LobbyListDiff;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

public class GameListRenderable extends Renderable {
    private final LightLobbyList lightLobbyList;

    public GameListRenderable(CommandPrompt[] relatedCommands) {
        super(relatedCommands);
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

    public void update(LobbyListDiff diff) {
        diff.apply(lightLobbyList);
        render();
    }

    public void updateCommand(CommandPrompt commandPrompt) {
        //Send Stuff to Controller
        System.out.println("Sending stuff to controller");
    }
}
