package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public class EchoRenderable extends Renderable{
    private String input = "";

    public EchoRenderable(String name, CommandPrompt[] relatedCommands, ControllerInterface controller){
        super(name, relatedCommands, controller);
    }

    @Override
    public void render() {
        System.out.println("Echo: " + input);
    }

    @Override
    public void updateInput(String input) {
        this.input = input;
        this.render();
    }

    @Override
    public void updateCommand(CommandPromptResult input){
        this.input = input.getAnswer(0);
        this.render();
    }

}
