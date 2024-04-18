package it.polimi.ingsw.view.TUI.inputs;

import it.polimi.ingsw.view.TUI.observers.CommandObserved;
import it.polimi.ingsw.view.TUI.observers.CommandObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public enum CommandPrompt implements Iterator<String>, CommandObserved {
    ECHO("echo", new String[]{"What do you want to echo?"}, new Predicate[]{s -> true}),
    CREATE_GAME("create game",
            new String[]{
                    "What's the name of the game?",
                    "How many players will be playing?",
            },
            new Predicate[]{
                    s -> true,
                    s -> {
                        try {
                            int i = Integer.parseInt(s.toString());
                            return i > 1 && i < 5;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
            });

    private final String[] questions;
    private final Predicate<String>[] validators;

    private final List<CommandObserver> observers = new ArrayList<>();

    private final String commandName;

    private CommandPromptResult currentResult;

    private int currentQuestion = 0;

    CommandPrompt(String name, String[] prompts, Predicate<String>[] validators) {
        this.commandName = name;
        this.questions = prompts;
        this.validators = validators;
        this.currentResult = new CommandPromptResult(new String[prompts.length]);
    }

    public String[] getQuestions() {
        return questions;
    }

    public Predicate<String>[] getValidators() {
        return validators;
    }

    public String getCommandName() {
        return commandName;
    }

    @Override
    public boolean hasNext() {
        return currentQuestion < questions.length;
    }

    @Override
    public String next() {
        return questions[currentQuestion++];
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void reset() {
        currentQuestion = 0;
        currentResult = new CommandPromptResult(new String[questions.length]);
    }

    public boolean validate(String answer) {
        return validators[currentQuestion-1].test(answer);
    }

    public boolean parseInput(String input){
        if(!validate(input)){
            System.out.println("Invalid input, please try again");
            return false;
        }
        currentResult.setAnswer(currentQuestion-1, input);
        return true;
    }

    @Override
    public String toString() {
        return this.questions[currentQuestion-1];
    }

    public CommandPromptResult getCurrentResult() {
        return currentResult;
    }

    public void attach(CommandObserver observer) {
        observers.add(observer);
    }

    public void detach(CommandObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (CommandObserver observer : observers) {
            observer.updateCommand(this.currentResult);
        }
    }

}
