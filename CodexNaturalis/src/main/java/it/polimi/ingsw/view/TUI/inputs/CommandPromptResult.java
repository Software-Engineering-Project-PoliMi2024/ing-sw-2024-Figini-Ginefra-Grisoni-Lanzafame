package it.polimi.ingsw.view.TUI.inputs;

/**
 * This class represents the result of a command prompt.
 */
public class CommandPromptResult {
    /** The answers to the command prompt. */
    String [] answers;

    /** The command prompt that generated this result. */
    CommandPrompt command;

    /**
     * Creates a new CommandPromptResult.
     * @param command The command prompt that generated this result.
     * @param answers The answers to the command prompt.
     */
    public CommandPromptResult(CommandPrompt command, String [] answers){
        this.command = command;
        this.answers = answers;
    }

    /**
     * Gets the answers to the command prompt.
     * @return The answers to the command prompt.
     */
    public String[] getAnswers(){
        return answers;
    }

    /**
     * Gets the answer at the specified index.
     * @param index The index of the answer to get.
     * @return The answer at the specified index.
     */
    public String getAnswer(int index){
        return answers[index];
    }

    /**
     * Sets the answer at the specified index.
     * @param index The index of the answer to set.
     * @param answer The answer to set.
     */
    public void setAnswer(int index, String answer){
        answers[index] = answer;
    }

    /**
     * Sets the answers to the command prompt.
     * @param answers The answers to the command prompt.
     */
    public void setAnswers(String [] answers){
        this.answers = answers;
    }

    /**
     * Gets the command prompt that generated this result.
     * @return The command prompt that generated this result.
     */
    public CommandPrompt getCommand(){
        return command;
    }

}
