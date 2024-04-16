package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public class EchoRenderable extends Renderable{
    private String input = "";

    public EchoRenderable(CommandPrompt[] relatedCommands){
        super(relatedCommands);
    }

    @Override
    public void render() {
        System.out.println("Echo: " + input);
    }

    @Override
    public void update() {
        render();
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
