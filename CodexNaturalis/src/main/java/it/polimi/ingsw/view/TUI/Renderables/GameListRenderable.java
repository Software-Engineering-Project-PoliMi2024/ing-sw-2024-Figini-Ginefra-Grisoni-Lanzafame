package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.utils.designPatterns.Observed;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.List;

/**
 * This class is a Renderable that represents a list of games.
 */
public class GameListRenderable extends Renderable {
    /** The lightLobbyList to render. */
    private final LightLobbyList lightLobbyList;

    /**
     * Creates a new GameListRenderable.
     * @param name The name of the renderable.
     * @param lightLobbyList The lightLobbyList to render.
     * @param relatedCommands The commands related to this renderable.
     * @param view The controller provider.
     */
    public GameListRenderable(String name, LightLobbyList lightLobbyList, CommandPrompt[] relatedCommands, ControllerProvider view) {
        super(name, relatedCommands, view);
        this.lightLobbyList = lightLobbyList;
    }

    /**
     * Renders the list of games.
     */
    @Override
    public void render() {
        if (lightLobbyList.getLobbies().isEmpty()) {
            PromptStyle.printInABox("No lobbies available", 70);
        } else {
            List<String> lobbyNames = lightLobbyList.getLobbies().stream().map(LightLobby::name).toList();
            PromptStyle.printListInABox("Available lobbies", lobbyNames, 70, 1);
        }
    }

    /**
     * Updates the renderable based on the command prompt result.
     * @param answer The command prompt result.
     */
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()) {
            case CommandPrompt.DISPLAY_GAME_LIST:
                this.render();
                break;
            case CommandPrompt.JOIN_GAME:
                try {
                    view.getController().joinLobby(answer.getAnswer(0));
                }
                catch (Exception e) {
                    System.out.println("Error while joining the game.");
                }
                break;
            case CommandPrompt.CREATE_GAME:
                try {
                    String lobbyName = answer.getAnswer(0);
                    int maxPlayers = Integer.parseInt(answer.getAnswer(1));
                    int numberOfAgents = Integer.parseInt(answer.getAnswer(2));
                    view.getController().createLobby(lobbyName, maxPlayers, numberOfAgents);
                }
                catch (Exception e) {
                    System.out.println("Error while creating the game.");
                }
                break;
            default:
                break;
        }
    }

    /**
     * Get the part of the light model which holds information relevant to the renderable.
     * @return The observed light model.
     */
    @Override
    public List<Observed> getObservedLightModel(){
        return List.of(lightLobbyList);
    }
}
