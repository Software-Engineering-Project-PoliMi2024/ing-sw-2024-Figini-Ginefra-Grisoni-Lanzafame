package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

/**
 * This is a sample renderable that echoes the input it receives.
 */
public class EchoRenderable extends Renderable{
    private String input = "";

    /**
     * Creates a new EchoRenderable.
     * @param name The name of the renderable.
     * @param relatedCommands The commands related to this renderable.
     * @param controller The controller to interact with.
     */
    public EchoRenderable(String name, CommandPrompt[] relatedCommands, ControllerInterface controller){
        super(name, relatedCommands, controller);
    }

    /**
     * Renders the input.
     */
    @Override
    public void render() {
        System.out.println("Echo: " + input);
    }

    /**
     * Sets the terminal's input as the renderable's input attribute.
     * @param input The new input.
     */
    @Override
    public void updateInput(String input) {
        this.input = input;
        this.render();
    }

    /**
     * Updates the renderable based on the command prompt result.
     * @param input The command prompt result.
     */
    @Override
    public void updateCommand(CommandPromptResult input){
        this.input = input.getAnswer(0);
        this.render();
    }

}
