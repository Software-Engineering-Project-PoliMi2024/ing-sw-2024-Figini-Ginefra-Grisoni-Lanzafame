package it.polimi.ingsw.view.TUI.inputs;

import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.observers.CommandObserved;
import it.polimi.ingsw.view.TUI.observers.CommandObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public enum CommandPrompt implements Iterator<String>, CommandObserved {
    ECHO("echo", new String[]{"What do you want to echo?"}, new Predicate[]{s -> true}, true),
    CONNECT(new DecoratedString("Connect", StringStyle.YELLOW_FOREGROUND).toString(),
            new String[]{
                    "What's the server IP?",
                    "What's the server port?",
            },
            new Predicate[]{
                    s -> true,
                    s -> {
                        try {
                            int i = Integer.parseInt(s.toString());
                            return i > 0 && i < 65536;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
            },
            false),
    LOGIN("Login",
            new String[]{
                    "What's your username?",
            },
            new Predicate[]{
                    s -> true,
            },
            false),
    CREATE_GAME("Create game",
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
            },
            false),

    JOIN_GAME("Join game",
            new String[]{
                    "What's the name of the game?",
            },
            new Predicate[]{
                    s -> true,
            },
            false),

    DISPLAY_GAME_LIST("Display game list", true),

    DISPLAY_LOBBY("Display lobby", true),

    LEAVE_LOBBY("Leave lobby", true),

    DISPLAY_START_FRONT("Display start card front", true),

    DISPLAY_START_BACK("Display start card back", true),

    CHOOSE_START_SIDE("Choose start side",
            new String[]{
                    "Which side do you want to choose? (front/back)",
            },
            new Predicate[]{
                    s -> s.equals("front") || s.equals("back"),
            },
            false),

    DISPLAY_OBJECTIVE_OPTIONS("Display objective options", true),

    CHOOSE_OBJECTIVE_CARD("Choose objective card",
            new String[]{
                    "Which objective card do you want to choose?",
            },
            new Predicate[]{
                    s -> {
                        try {
                            int i = Integer.parseInt(s.toString());
                            return i >= 0 && i < 3;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    },
            },
            false),

    DISPLAY_HAND_FRONT("Display hand front", true),
    DISPLAY_HAND_BACK("Display hand back", true),

    DISPLAY_SECRET_OBJECTIVE("Display secret objective", true),

    PLACE_CARD("Place card",
            new String[]{
                    "Which card do you want to place?",
                    "Face up or face down? (0/1)",
                    "Where do you want to place it?",
            },
            new Predicate[]{
                    s -> {
                        try {
                            int i = Integer.parseInt(s.toString());
                            return i >= 0 && i < 3;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    },
                    s -> s.equals("0") || s.equals("1"),
                    s -> {
                        try {
                            int i = Integer.parseInt(s.toString());
                            return i >= 0 && i < 100;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    },
            },
            true);



    private final String[] questions;
    private final Predicate<String>[] validators;

    private final List<CommandObserver> observers = new ArrayList<>();

    private final String commandName;

    private CommandPromptResult currentResult;

    private int currentQuestion = 0;

    // If the command is local, it doesn't interact with the controller
    private boolean isLocal = false;

    CommandPrompt(String name, String[] prompts, Predicate<String>[] validators, boolean isLocal) {
        this.commandName = name;
        this.questions = prompts;
        this.validators = validators;
        this.currentResult = new CommandPromptResult(this, new String[prompts.length]);
        this.isLocal = isLocal;
    }

    CommandPrompt(String name, boolean isLocal) {
        this.commandName = name;
        this.questions = new String[]{};
        this.validators = new Predicate[]{};
        this.currentResult = new CommandPromptResult(this, new String[0]);
        this.isLocal = isLocal;
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
        currentResult = new CommandPromptResult(this, new String[questions.length]);
    }

    public boolean validate(String answer) {
        return validators[currentQuestion-1].test(answer);
    }

    public boolean parseInput(String input){
        if(!validate(input)){
            System.out.println("Invalid input, please try again");
            System.out.print("\t");
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

    public boolean isLocal() {
        return isLocal;
    }
}
