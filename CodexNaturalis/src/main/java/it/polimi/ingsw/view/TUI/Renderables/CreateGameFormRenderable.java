package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public class CreateGameFormRenderable extends Renderable {

    public CreateGameFormRenderable(String name, CommandPrompt[] relatedCommands, ControllerInterface controller){
        super(name, relatedCommands, controller);
    }

    @Override
    public void render() {

        System.out.println("Enter the name of the new game - number of players:");
    }

    @Override
    public void updateInput(String input) {
        String[] nameAndNum = input.split("-");
        String name = nameAndNum[0];
        int players = Integer.parseInt(nameAndNum[1]);
        System.out.println(name);
        System.out.println(players);
    }

    @Override
    public void updateCommand(CommandPromptResult input){
        String name = input.getAnswer(0);
        int players = Integer.parseInt(input.getAnswer(1));
    }
}

