package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.util.*;

public class CommandDisplayRenderable extends Renderable{
    private final Map<CommandPrompt, Integer> activePrompts;

    CommandPrompt currentPrompt;

    public CommandDisplayRenderable(CommandPrompt[] relatedCommands) {
        super(relatedCommands);
        this.activePrompts = new LinkedHashMap<>();
    }

    @Override
    public void render() {
        System.out.println("Active Commands:");
        for(int i = 0; i < activePrompts.size(); i++){
            CommandPrompt prompt = getPromptAtIndex(i);
            System.out.println("\t[" + i + "]" + prompt.getCommandName());
        }

        System.out.println("What do you want to do ❔");
    }

    @Override
    public void update() {
        // Do nothing
    }

    private CommandPrompt getPromptAtIndex(int index){
        return (CommandPrompt) activePrompts.keySet().toArray()[index];
    }

    @Override
    public void updateInput(String input) {
        if(this.currentPrompt == null){
            //Checks if the input is a number
            if(!input.matches("\\d+")){
                System.out.println("Invalid input, please insert a number");
                return;
            }

            int index = Integer.parseInt(input);

            if(index < 0 || index >= activePrompts.size()){
                System.out.println("Invalid input, please insert a number between 0 and " + (activePrompts.size() - 1));
                return;
            }

            this.currentPrompt = getPromptAtIndex(index);


            System.out.println("You selected " + currentPrompt.getCommandName());

            System.out.println(currentPrompt.next());
        }
        else{
            if(currentPrompt.parseInput(input)){
                if(currentPrompt.hasNext()){
                    System.out.println(currentPrompt.next());
                }
                else{
                    System.out.println("Command completed");
                    currentPrompt.notifyObservers();
                    currentPrompt.reset();
                    currentPrompt = null;
                    this.render();
                }
            }
        }
    }

    public void addCommandPrompt(CommandPrompt prompt){
        if(!activePrompts.containsKey(prompt))
            activePrompts.put(prompt, 1);
        else
            activePrompts.put(prompt, activePrompts.get(prompt) + 1);
    }

    public void removeCommandPrompt(CommandPrompt prompt){
        if(!activePrompts.containsKey(prompt))
            throw new IllegalArgumentException("Prompt not found");

        if(activePrompts.get(prompt) == 1)
            activePrompts.remove(prompt);
        else
            activePrompts.put(prompt, activePrompts.get(prompt) - 1);
    }
}
