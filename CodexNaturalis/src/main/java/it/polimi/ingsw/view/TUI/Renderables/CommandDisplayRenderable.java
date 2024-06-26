package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.model.utilities.Pair;
import it.polimi.ingsw.utils.OSRelated;
import it.polimi.ingsw.view.TUI.Printing.Printable;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Styles.*;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;

import java.util.*;

/**
 * This class is the renderable that displays the active commands.
 * It is also responsible to handle the user input for the commands.
 * It stores the active local and non-local commands and displays them.
 * When a prompt is selected by the user, it becomes the current prompt and it gradually asks the user for the required input.
 */
public class CommandDisplayRenderable extends Renderable{
    /** The active local prompts. */
    private final Map<CommandPrompt, Integer> activeLocalPrompts;

    /** The active non-local prompts. */
    private final Map<CommandPrompt, Integer> activeActionPrompts;

    /**
     * The current prompt index.
     * -1 if there is no current prompt.
     */
    private int currentPromptIndex = -1;

    /** The current prompt. */
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
        PromptStyle.printInABoxDouble("Active Commands", 102);

        List<String> commands = new ArrayList<>();

        Printable activeLocalPromtsPrintable = new Printable("");
        for (int i = 0; i < activeLocalPrompts.size(); i++) {
            CommandPrompt prompt = getPromptAtIndex(i);
            String CommandLabel = new DecoratedString(prompt.getCommandName(), StringStyle.UNDERLINE).toString();

            String CommandNumber = new DecoratedString("[" + i + "]", StringStyle.BOLD).toString();

            commands.add(CommandNumber);
            commands.add(CommandLabel);
        }

        PromptStyle.printListInABox(activeLocalPromtsPrintable,"Active Display Commands", commands, 50, 2);

        Printable activeActionPromptsPrintable = new Printable("");
        commands.clear();
        for (int i = 0; i < activeActionPrompts.size(); i++) {
            CommandPrompt prompt = getPromptAtIndex(i + activeLocalPrompts.size());
            String CommandLabel = new DecoratedString(prompt.getCommandName(), StringStyle.UNDERLINE).toString();

            String CommandNumber = new DecoratedString("[" + (i + activeLocalPrompts.size()) + "]", StringStyle.BOLD).toString();

            commands.add(CommandNumber);
            commands.add(CommandLabel);
        }
        PromptStyle.printListInABox(activeActionPromptsPrintable,"Active Action Commands", commands, 50, 2);


        Printer.printStackedHorizontally(List.of(activeLocalPromtsPrintable, activeActionPromptsPrintable));

        Printable printable = new Printable("");
        printable.println("What do you want to do ❔");
        printable.print("\t");
        Printer.print(printable);

        printCurrentPrompt();
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

            //Sets the current prompt index
            this.currentPromptIndex = index;

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
                OSRelated.clearTerminal();

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
                    OSRelated.clearTerminal();

                    currentPromptIndex = -1;

                    currentPrompt.notifyObservers();

                    if(currentPrompt != null) {
                        currentPrompt.reset();

                        if (currentPrompt.isLocal())
                            this.render();

                        currentPrompt = null;
                    }

                }
            }
        }
    }

    /**
     * Prints the current prompt.
     * IT prints the current selection and the questions and answers asked/answered so far.
     */
    private void printCurrentPrompt(){
        if(currentPrompt == null || !currentPrompt.hasLast() || currentPromptIndex == -1)
            return;

        Printer.print(this.currentPromptIndex + "\n");

        PromptStyle.printInABox("You selected " + new DecoratedString(currentPrompt.getCommandName(), StringStyle.UNDERLINE).toString(), 50);

        List<Pair<String, String>> qna = currentPrompt.getQnASoFar();

        Printable qnaPrintable = new Printable("");

        for(Pair<String, String> pair : qna){
            String question = pair.first();
            String answer = pair.second();

            qnaPrintable.println(question);
            qnaPrintable.print("\t");
            qnaPrintable.println(answer);
        }
        Printer.print(qnaPrintable);

        if(currentPrompt.hasLast())
            Printer.printlnt(currentPrompt.last());
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
