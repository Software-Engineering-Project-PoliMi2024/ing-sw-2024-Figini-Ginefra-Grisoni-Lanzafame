package it.polimi.ingsw.view.TUI.inputs;

public class CommandPromptResult {
    String [] answers;

    CommandPrompt command;

    public CommandPromptResult(CommandPrompt command, String [] answers){
        this.command = command;
        this.answers = answers;
    }

    public String[] getAnswers(){
        return answers;
    }

    public String getAnswer(int index){
        return answers[index];
    }

    public void setAnswer(int index, String answer){
        answers[index] = answer;
    }

    public void setAnswers(String [] answers){
        this.answers = answers;
    }

    public CommandPrompt getCommand(){
        return command;
    }

}
