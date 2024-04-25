package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.model.lightModel.LightGameList;
import it.polimi.ingsw.model.lightModel.LightLobby;
import it.polimi.ingsw.model.lightModel.diffs.ModelDiff;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

import java.util.List;

public class GameListRenderable extends Renderable {
    private final LightGameList lightGameList;

    public GameListRenderable(CommandPrompt[] relatedCommands) {
        super(relatedCommands);
        this.lightGameList = new LightGameList();
    }

    @Override
    public void render() {
        if (lightGameList.getLobbies().isEmpty()) {
            System.out.println("No games available to join.");
        } else {
            System.out.println("Available games:");
            lightGameList.getLobbies().forEach(lobby -> {
                System.out.println("Game: " + lobby.nicknames());
                for(int i = 0; i < lobby.nicknames().size(); i++)
                    System.out.println("\t[" + i + "] " + lobby.nicknames().get(i));
            });
        }
    }

    public void update(ModelDiff<LightLobby> diff) {
        lightGameList.applyDiff(diff);
        render();
    }

    public void updateCommand(CommandPrompt commandPrompt) {
        //Send Stuff to Controller
        System.out.println("Sending stuff to controller");
    }
}
