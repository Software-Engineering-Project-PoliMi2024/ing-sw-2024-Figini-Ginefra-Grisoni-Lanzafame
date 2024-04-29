package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterfaceClient;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public class FormRenderable extends Renderable{
    public FormRenderable(String name, CommandPrompt[] commandPrompts, ControllerInterfaceClient controller) {
        super(name, commandPrompts, controller);
    }

    public void render(){
        return;
    }

    public void updateCommand(CommandPromptResult command){
        return;
    }

}
