package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.controller2.ControllerInterfaceClient;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public class LoginFormRenderable extends FormRenderable{
    public LoginFormRenderable(String name, CommandPrompt[] commandPrompts, ControllerInterfaceClient controller) {
        super(name, commandPrompts, controller);
    }

    public void render(){
        System.out.println("Insert your username:");
    }

    public void updateCommand(CommandPromptResult command){
        controller.login(command.getAnswer(0));
    }
}
