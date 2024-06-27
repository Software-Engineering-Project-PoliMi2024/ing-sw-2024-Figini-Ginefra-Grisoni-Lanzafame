package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.List;

/**
 * This class represents the Renderable that displays the winners of the game and handles post-game commands.
 */
public class PostGameStateRenderable extends Renderable {
    /** The LightGame object representing the game's state */
    private final LightGame lightGame;

    /**
     * Constructs a PostGameStateRenderable object.
     *
     * @param name The name of the Renderable.
     * @param lightGame The LightGame object representing the game's state.
     * @param relatedCommands The commands related to this Renderable.
     * @param view The ControllerProvider object for controller access.
     */
    public PostGameStateRenderable(String name, LightGame lightGame, CommandPrompt[] relatedCommands, ControllerProvider view) {
        super(name, relatedCommands, view);
        this.lightGame = lightGame;
    }

    /**
     * Renders the post-game state.
     * Displays the winners of the game.
     */
    @Override
    public void render() {
        List<String> winnersList = lightGame.getWinners();
        String winners = winnersList
                .stream()
                .map(name -> new DecoratedString(name, StringStyle.GOLD_FOREGROUND).toString())
                .reduce("", (acc, name) -> acc + name + ", ");

        winners = winners.substring(0, winners.length() - 2);

        PromptStyle.printInABox("Game Over! Congratulations to the winners:", 70);
        PromptStyle.printInABox(winners, 70);
    }

    /**
     * Updates the command based on the CommandPromptResult.
     *
     * @param answer The CommandPromptResult containing the user's command and input.
     */
    @Override
    public void updateCommand(CommandPromptResult answer) {
        switch (answer.getCommand()) {
            case DISPLAY_POSTGAME:
                this.render();
                break;
            case LEAVE:
                try {
                    view.getController().leave();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                break;
        }
    }
}
