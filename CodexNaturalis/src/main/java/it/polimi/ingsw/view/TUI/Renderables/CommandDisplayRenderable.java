package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.view.TUI.Printing.Printable;
import it.polimi.ingsw.view.TUI.Printing.Printer;
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
        List<String> commands = new ArrayList<>();

        for(int i = 0; i < activePrompts.size(); i++){
            CommandPrompt prompt = getPromptAtIndex(i);
            String CommandLabel = new DecoratedString(prompt.getCommandName(), StringStyle.UNDERLINE).toString();

            String CommandNumber = new DecoratedString("[" + i + "]", StringStyle.BOLD).toString();

            commands.add(CommandNumber);
            commands.add(CommandLabel);
        }
        PromptStyle.printListInABox("Active Commands", commands, 50, 2);

        Printable printable = new Printable("");
        printable.println("What do you want to do ❔");
        printable.print("\t");
    }


    private CommandPrompt getPromptAtIndex(int index){
        return (CommandPrompt) activePrompts.keySet().toArray()[index];
    }

    @Override
    public void updateInput(String input) {
        if(this.currentPrompt == null){
            //Checks if the input is a number
            if(!input.matches("\\d+")){
                Printable printable = new Printable("");
                printable.println("Invalid input, please insert a number");
                printable.print("\t");
                Printer.print(printable);
                return;
            }

            int index = Integer.parseInt(input);

            if(index < 0 || index >= activePrompts.size()){
                Printable printable = new Printable("");
                printable.println("Invalid input, please insert a number between 0 and " + (activePrompts.size() - 1));
                printable.print("\t");
                Printer.print(printable);
                return;
            }

            this.currentPrompt = getPromptAtIndex(index);


            PromptStyle.printInABox("You selected " + new DecoratedString(currentPrompt.getCommandName(), StringStyle.UNDERLINE).toString(), 50);

            if(currentPrompt.hasNext()) {
                Printer.printlnt(currentPrompt.next());
            }
            else{
                currentPrompt.notifyObservers();
                currentPrompt.reset();
                currentPrompt = null;
                this.render();
            }
        }
        else{
            if(currentPrompt.parseInput(input)){
                if(currentPrompt.hasNext()){
                    Printer.printlnt(currentPrompt.next());
                }
                else{
                    PromptStyle.printInABox(new DecoratedString(currentPrompt.getCommandName(), StringStyle.UNDERLINE).toString() + " completed", 50);
                    currentPrompt.notifyObservers();
                    currentPrompt.reset();

                    if(currentPrompt.isLocal())
                        this.render();

                    currentPrompt = null;

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
