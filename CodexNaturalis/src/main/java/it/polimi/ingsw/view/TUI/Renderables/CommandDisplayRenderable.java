package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.view.TUI.Styles.*;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

import java.util.*;

public class CommandDisplayRenderable extends Renderable{
    private final Map<CommandPrompt, Integer> activePrompts;

    CommandPrompt currentPrompt;

    public CommandDisplayRenderable(String name, CommandPrompt[] relatedCommands, ControllerInterface controller) {
        super(name, relatedCommands, controller);
        this.activePrompts = new LinkedHashMap<>();
    }



    @Override
    public void render() {
        System.out.println(PromptStyle.HorizontalDoubleSeparator.repeat(52));

        PromptStyle.printBetweenSeparators("Active commands", 50);

        System.out.println(PromptStyle.HorizontalDoubleSeparator.repeat(52));


        for(int i = 0; i < activePrompts.size(); i++){
            CommandPrompt prompt = getPromptAtIndex(i);
            String CommandLabel = new DecoratedString(prompt.getCommandName(), StringStyle.UNDERLINE).toString();

            String CommandNumber = new DecoratedString("[" + i + "]", StringStyle.BOLD).toString();

            PromptStyle.printBetweenSeparators(CommandNumber, 50);
            PromptStyle.printBetweenSeparators(CommandLabel, 50);

            PromptStyle.printSeparator(52);
        }

        System.out.println("What do you want to do ❔");
        System.out.print("\t");
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
                System.out.print("\t");
                return;
            }

            int index = Integer.parseInt(input);

            if(index < 0 || index >= activePrompts.size()){
                System.out.println("Invalid input, please insert a number between 0 and " + (activePrompts.size() - 1));
                System.out.print("\t");
                return;
            }

            this.currentPrompt = getPromptAtIndex(index);


            PromptStyle.printInABox("You selected " + new DecoratedString(currentPrompt.getCommandName(), StringStyle.UNDERLINE).toString(), 50);

            if(currentPrompt.hasNext()) {
                System.out.println(currentPrompt.next());
                System.out.print("\t");
            }
            else{
                PromptStyle.printInABox(new DecoratedString(currentPrompt.getCommandName(), StringStyle.UNDERLINE).toString() + " completed", 50);
                currentPrompt.notifyObservers();
                currentPrompt.reset();
                currentPrompt = null;
                this.render();
            }
        }
        else{
            if(currentPrompt.parseInput(input)){
                if(currentPrompt.hasNext()){
                    System.out.println(currentPrompt.next());
                    System.out.print("\t");
                }
                else{
                    PromptStyle.printInABox(new DecoratedString(currentPrompt.getCommandName(), StringStyle.UNDERLINE).toString() + " completed", 50);
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

    public void clearCommandPrompts(){
        activePrompts.clear();
    }
}
