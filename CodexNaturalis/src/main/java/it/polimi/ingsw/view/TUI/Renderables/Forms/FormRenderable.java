package it.polimi.ingsw.view.TUI.Renderables.Forms;

import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

/**
 * This class is a Renderable that represents a form. That is, a renderable that has nothing to render. It is just a holder for the right commands.
 */
public class FormRenderable extends Renderable {
    /**
     * Creates a new FormRenderable.
     * @param name The name of the renderable.
     * @param commandPrompts The command prompts related to this renderable.
     * @param view The controller provider.
     */
    public FormRenderable(String name, CommandPrompt[] commandPrompts, ControllerProvider view) {
        super(name, commandPrompts, view);

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
