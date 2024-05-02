package it.polimi.ingsw.view.TUI.Renderables.Forms;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.view.TUI.Renderables.Forms.FormRenderable;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.rmi.RemoteException;

public class LoginFormRenderable extends FormRenderable {
    public LoginFormRenderable(String name, CommandPrompt[] commandPrompts, ControllerInterface controller) {
        super(name, commandPrompts, controller);
    }

    public void render(){
        System.out.println("Insert your username:");
    }
    public void updateCommand(CommandPromptResult command){
        try{
            controller.login(command.getAnswer(0));
        }catch (RemoteException r){
            r.printStackTrace();
        }
    }
}
