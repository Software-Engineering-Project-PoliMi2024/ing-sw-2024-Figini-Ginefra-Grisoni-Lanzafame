package it.polimi.ingsw.view.TUI.Renderables.Forms;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

/**
 * This class is a Renderable that represents a form. That is, a renderable that has nothing to render, it is just an holder for the right commands.
 */
public class FormRenderable extends Renderable {
    /**
     * Creates a new FormRenderable.
     * @param name The name of the renderable.
     * @param commandPrompts The command prompts related to this renderable.
     * @param controller The controller to interact with.
     */
    public FormRenderable(String name, CommandPrompt[] commandPrompts, ControllerInterface controller) {
        super(name, commandPrompts, controller);

    }

    /**
     * Renders the form. In this case it does nothing.
     */
    public void render(){
        return;
    }


    /**
     * Updates the renderable based on the command prompt result. In this case it does nothing.
     * @param command The command prompt result.
     */
    public void updateCommand(CommandPromptResult command){
        return;
    }

}
