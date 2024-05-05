package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is a Renderable that represents a lobby.
 */
public class LobbyRenderable extends Renderable {
    private final LightLobby lightLobby;

    /**
     * Creates a new LobbyRenderable.
     * @param name The name of the renderable.
     * @param lightLobby The lightLobby to render.
     * @param relatedCommands The commands related to this renderable.
     * @param controller The controller to interact with.
     */
    public LobbyRenderable(String name, LightLobby lightLobby, CommandPrompt[] relatedCommands, ControllerInterface controller) {
        super(name, relatedCommands, controller);
        this.lightLobby = lightLobby;
    }

    /**
     * Renders the lobby.
     */
    @Override
    public void render() {
        String lobbyName = lightLobby.name();
        List<String> nicknames = new LinkedList<>(lightLobby.nicknames());
        int numberOfPlayers = nicknames.size();
        int numberOfMaxPlayer = lightLobby.numberMaxPlayer();

        for(int i = numberOfPlayers; i < numberOfMaxPlayer; i++){
            nicknames.add("?");
        }

        PromptStyle.printListInABox(lobbyName + " [" + numberOfPlayers + "/" + numberOfMaxPlayer + "]", nicknames, 70, 1);
    }

    /**
     * Updates the renderable based on the command prompt result.
     * @param answer The command prompt result.
     */
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
