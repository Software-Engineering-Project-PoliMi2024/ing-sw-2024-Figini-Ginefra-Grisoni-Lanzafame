package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.view.TUI.Printing.Printable;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Styles.*;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

import java.util.*;

/**
 * This class is the renderable that displays the active commands.
 */
public class CommandDisplayRenderable extends Renderable{
    /** The active local prompts. */
    private final Map<CommandPrompt, Integer> activeLocalPrompts;

    /** The active non-local prompts. */
    private final Map<CommandPrompt, Integer> activeActionPrompts;
    CommandPrompt currentPrompt;

    /**
     * Creates a new CommandDisplayRenderable.
     * @param name The name of the renderable.
     */
    public CommandDisplayRenderable(String name) {
        super(name, new CommandPrompt[0], null);
        this.activeLocalPrompts = new LinkedHashMap<>();
        this.activeActionPrompts = new LinkedHashMap<>();
    }

    /**
     * Renders the active commands.
     */
    @Override
    public void render() {
        PromptStyle.printInABoxDouble("Active Commands", 50);


        List<String> commands = new ArrayList<>();

        if(!activeLocalPrompts.isEmpty()) {
            for (int i = 0; i < activeLocalPrompts.size(); i++) {
                CommandPrompt prompt = getPromptAtIndex(i);
                String CommandLabel = new DecoratedString(prompt.getCommandName(), StringStyle.UNDERLINE).toString();

                String CommandNumber = new DecoratedString("[" + i + "]", StringStyle.BOLD).toString();

                commands.add(CommandNumber);
                commands.add(CommandLabel);
            }

            PromptStyle.printListInABox("Active Display Commands", commands, 50, 2);
        }


        if(!activeActionPrompts.isEmpty()) {
            commands.clear();
            for (int i = 0; i < activeActionPrompts.size(); i++) {
                CommandPrompt prompt = getPromptAtIndex(i + activeLocalPrompts.size());
                String CommandLabel = new DecoratedString(prompt.getCommandName(), StringStyle.UNDERLINE).toString();

                String CommandNumber = new DecoratedString("[" + (i + activeLocalPrompts.size()) + "]", StringStyle.BOLD).toString();

                commands.add(CommandNumber);
                commands.add(CommandLabel);
            }
            PromptStyle.printListInABox("Active Action Commands", commands, 50, 2);
        }

        Printable printable = new Printable("");
        printable.println("What do you want to do ❔");
        printable.print("\t");
        Printer.print(printable);
    }

    /**
     * Returns the prompt at the given index.
     * @param index The index of the prompt.
     * @return The prompt at the given index.
     */
    private CommandPrompt getPromptAtIndex(int index){
        if(index < activeLocalPrompts.size())
            return (CommandPrompt)activeLocalPrompts.keySet().toArray()[index];
        else
            return (CommandPrompt) activeActionPrompts.keySet().toArray()[index - activeLocalPrompts.size()];
    }

    /**
     * Updates the renderable based on the input from the terminal.
     * @param input The input form the terminal.
     */
    @Override
    public void updateInput(String input) {
        //Checks if there is a current prompt, if not it will select the prompt
        if(this.currentPrompt == null){
            //Checks if the input is a number
            if(!input.matches("\\d+")){
                Printable printable = new Printable("");
                printable.println("Invalid input, please insert a number");
                printable.print("\t");
                Printer.print(printable);
                return;
            }

            //Parses the input
            int index = Integer.parseInt(input);

            //Checks if the number is in the range of the active prompts
            if(index < 0 || index >= activeLocalPrompts.size() + activeActionPrompts.size()){
                Printable printable = new Printable("");
                printable.println("Invalid input, please insert a number between 0 and " + (activeLocalPrompts.size() + activeActionPrompts.size() - 1));
                printable.print("\t");
                Printer.print(printable);
                return;
            }

            //Gets the prompt at the given index
            this.currentPrompt = getPromptAtIndex(index);

            //Prints the prompt selection message
            PromptStyle.printInABox("You selected " + new DecoratedString(currentPrompt.getCommandName(), StringStyle.UNDERLINE).toString(), 50);

            //Prints the first prompt message
            if(currentPrompt.hasNext()) {
                Printer.printlnt(currentPrompt.next());
            }
            else{
                //If the prompt has no next, it will notify the observers
                //This is used for commands that don't require any input
                System.out.print("\033[H\033[2J");

                currentPrompt.notifyObservers();
                currentPrompt.reset();

                if(currentPrompt.isLocal())
                    this.render();

                currentPrompt = null;

            }
        }
        else{
            //If there is a current prompt, it will parse the input
            if(currentPrompt.parseInput(input)){
                //If the input is valid, it will print the next prompt message
                if(currentPrompt.hasNext()){
                    Printer.printlnt(currentPrompt.next());
                }
                else{
                    //If the prompt has no next, it will notify the observers, reset the prompt and set the current prompt to null
                    System.out.print("\033[H\033[2J");
                    currentPrompt.notifyObservers();
                    currentPrompt.reset();

                    if(currentPrompt.isLocal())
                        this.render();

                    currentPrompt = null;

                }
            }
        }
    }

    /**
     * Adds a command prompt to the given map.
     * @param map The map to add the command prompt to.
     * @param prompt The command prompt to add.
     */
    private void addCommandPromptTo(Map<CommandPrompt, Integer> map, CommandPrompt prompt){
        if(!map.containsKey(prompt))
            map.put(prompt, 1);
        else
            map.put(prompt, map.get(prompt) + 1);
    }

    /**
     * Removes a command prompt from the given map.
     * @param map The map to remove the command prompt from.
     * @param prompt The command prompt to remove.
     */
    private void removeCommandPromptFrom(Map<CommandPrompt, Integer> map, CommandPrompt prompt){
        if(!map.containsKey(prompt))
            throw new IllegalArgumentException("Prompt not found");

        if(map.get(prompt) == 1)
            map.remove(prompt);
        else
            map.put(prompt, map.get(prompt) - 1);
    }

    /**
     * Adds a command prompt to the active prompts.
     * @param prompt The command prompt to add.
     */
    public void addCommandPrompt(CommandPrompt prompt){
        if(prompt.isLocal())
            addCommandPromptTo(activeLocalPrompts, prompt);
        else
            addCommandPromptTo(activeActionPrompts, prompt);
    }

    /**
     * Removes a command prompt from the active prompts.
     * @param prompt The command prompt to remove.
     */
    public void removeCommandPrompt(CommandPrompt prompt){
        if(prompt.isLocal())
            removeCommandPromptFrom(activeLocalPrompts, prompt);
        else
            removeCommandPromptFrom(activeActionPrompts, prompt);
    }

    /**
     * Clears all the active command prompts.
     */
    public void clearCommandPrompts(){
        activeLocalPrompts.clear();
        activeActionPrompts.clear();
    }
}
