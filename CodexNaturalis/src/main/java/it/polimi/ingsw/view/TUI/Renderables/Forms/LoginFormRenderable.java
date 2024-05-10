package it.polimi.ingsw.view.TUI.Renderables.Forms;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.Renderables.Forms.FormRenderable;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.rmi.RemoteException;

/**
 * This class is a Renderable that represents a login form.
 */
public class LoginFormRenderable extends FormRenderable {
    /**
     * Creates a new LoginFormRenderable.
     * @param name The name of the renderable.
     * @param commandPrompts The command prompts related to this renderable.
     * @param view The controller provider.
     */
    public LoginFormRenderable(String name, CommandPrompt[] commandPrompts, ControllerProvider view) {
        super(name, commandPrompts, view);
    }

    /**
     * Renders the login form.
     */
    public void render(){
        System.out.println("Insert your username:");
    }

    /**
     * Updates the renderable based on the command prompt result.
     * @param command The command prompt result.
     */
    public void updateCommand(CommandPromptResult command){
        try{
            view.getController().login(command.getAnswer(0));
        }catch (Exception r){
            r.printStackTrace();
        }
    }
}
