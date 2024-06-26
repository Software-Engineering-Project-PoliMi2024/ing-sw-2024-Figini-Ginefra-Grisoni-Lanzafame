package it.polimi.ingsw.view.TUI.inputs;

/**
 * This class represents a prompt that will be triggered at the start of a state.
 * Moreover, since some prompts require some answers, this class extends CommandPromptResult and when triggered it will send itself to all the observer of the target command.
 */
public class StartUpPrompt extends CommandPromptResult{
    /**
     * Creates a new StartUpPrompt with no answers. This is useful for prompts that do not require any input.
     * @param command The command prompt that generated this result.
     */
    public StartUpPrompt(CommandPrompt command) {
        super(command, new String[0]);
    }

    /**
     * Sends the current answers to the command prompt notifying all the observers.
     */
    public void trigger(){
        this.getCommand().notifyObservers(this);
    }
}
