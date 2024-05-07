package it.polimi.ingsw.view.TUI.inputs;

import it.polimi.ingsw.view.TUI.Styles.DecoratedString;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.observers.CommandObserved;
import it.polimi.ingsw.view.TUI.observers.CommandObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * This enum represents the possible commands that can be prompted to the user.
 */
public enum CommandPrompt implements Iterator<String>, CommandObserved {
    /** The echo command. */
    ECHO("echo", new String[]{"What do you want to echo?"}, new Predicate[]{s -> true}, true),

    /** The command to connect to the server */
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
    /** The command to login */
    LOGIN("Login",
            new String[]{
                    "What's your username?",
            },
            new Predicate[]{
                    s -> true,
            },
            false),
    /** The command to create a new game */
    CREATE_GAME("Create game",
            new String[]{
                    "What's the name of the game?",
                    "How many players will be playing? (2-4)",
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
    /** The command to join an existing game */
    JOIN_GAME("Join game",
            new String[]{
                    "What's the name of the game?",
            },
            new Predicate[]{
                    s -> true,
            },
            false),
    /** The command to display the current list of games with free spots */
    DISPLAY_GAME_LIST("Display game list", true),

    /** The command to display the lobby the player is currently in*/
    DISPLAY_LOBBY("Display lobby", true),

    /** The command to leave the lobby */
    LEAVE_LOBBY("Leave lobby", false),

    /** The command to display the assigned start card front */
    DISPLAY_START_FRONT("Display start card front", true),

    /** The command to display the assigned start card back */
    DISPLAY_START_BACK("Display start card back", true),

    /** The command to choose the side of the start card */
    CHOOSE_START_SIDE("Choose start side",
            new String[]{
                    "Which side do you want to choose? (front/back)",
            },
            new Predicate[]{
                    s -> s.equals("front") || s.equals("back"),
            },
            false),

    /** The command to display the objective options */
    DISPLAY_OBJECTIVE_OPTIONS("Display objective options", true),

    /** The command to choose the objective card */
    CHOOSE_OBJECTIVE_CARD("Choose objective card",
            new String[]{
                    "Which objective card do you want to choose? (1/2)",
            },
            new Predicate[]{
                    s -> {
                        try {
                            int i = Integer.parseInt(s.toString());
                            return i == 1 || i == 2;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    },
            },
            false),

    /** The command to display the hand front */
    DISPLAY_HAND_FRONT("Display hand front", true),

    /** The command to display the hand back */
    DISPLAY_HAND_BACK("Display hand back", true),

    /** The command to display the secret objective */
    DISPLAY_SECRET_OBJECTIVE("Display secret objective", true),

    /** The command to place a card */
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
            true),

    /** The command to display the codex */
    DISPLAY_CODEX("Display codex", true),

    /** The command to peek at a player */
    PEEK("Peek",
            new String[]{
                    "Which Player do you want to peek? Insert the nickname",
            },
            new Predicate[]{
                    s -> true,
            },
            true),

    DRAW_CARD("Draw card",
            new String[]{
                    "Which deck do you want to draw from? (0/gold - 1/resource)",
                    "Where do you want to draw from? (0/Buffer - 1/Buffer - 2/NextDraw)",
            },
            new Predicate[]{
                    s -> s.equals("0") || s.equals("1"),
                    s -> s.equals("0") || s.equals("1") || s.equals("2"),
            },
            false),

    DISPLAY_LEADERBOARD("Display leaderboard", true),

    DISPLAY_DECKS("Display decks", true);

    /** The questions to ask the user. */
    private final String[] questions;

    /** The validators to validate the user input. */
    private final Predicate<String>[] validators;

    /** The observers of the command. */
    private final List<CommandObserver> observers = new ArrayList<>();

    /** The name of the command. */
    private final String commandName;

    /** The current result of the command. */
    private CommandPromptResult currentResult;

    /** The current question. */
    private int currentQuestion = 0;

    /** Whether the command is local. That means it doesn't interact with the controller. */
    private boolean isLocal = false;

    /**
     * Creates a new CommandPrompt.
     * @param name The name of the command.
     * @param prompts The questions to ask the user.
     * @param validators The validators to validate the user input.
     * @param isLocal Whether the command is local.
     */
    CommandPrompt(String name, String[] prompts, Predicate<String>[] validators, boolean isLocal) {
        this.commandName = name;
        this.questions = prompts;
        this.validators = validators;
        this.currentResult = new CommandPromptResult(this, new String[prompts.length]);
        this.isLocal = isLocal;
    }

    /**
     * Creates a new CommandPrompt. This constructor is used for commands that don't require any input.
     * @param name The name of the command.
     * @param isLocal Whether the command is local.
     */
    CommandPrompt(String name, boolean isLocal) {
        this.commandName = name;
        this.questions = new String[]{};
        this.validators = new Predicate[]{};
        this.currentResult = new CommandPromptResult(this, new String[0]);
        this.isLocal = isLocal;
    }

    /**
     * Gets the questions.
     * @return The questions.
     */
    public String[] getQuestions() {
        return questions;
    }

    /**
     * Gets the validators.
     * @return The validators.
     */
    public Predicate<String>[] getValidators() {
        return validators;
    }

    /**
     * Gets the command name.
     * @return The command name.
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * @return Whether there are more questions.
     */
    @Override
    public boolean hasNext() {
        return currentQuestion < questions.length;
    }

    /**
     * @return The next question.
     */
    @Override
    public String next() {
        return questions[currentQuestion++];
    }

    /**
     * Removes the current question. This operation is not supported.
     * @throws UnsupportedOperationException This operation is not supported.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Resets the command. The current question is set to 0 and the current result is reset.
     */
    public void reset() {
        currentQuestion = 0;
        currentResult = new CommandPromptResult(this, new String[questions.length]);
    }

    /**
     * Validates the answer.
     * @param answer The answer to validate.
     * @return Whether the answer is valid.
     */
    public boolean validate(String answer) {
        return validators[currentQuestion-1].test(answer);
    }

    /**
     * Parses the input.
     * @param input The input to parse.
     * @return Whether the input was parsed successfully.
     */
    public boolean parseInput(String input){
        if(!validate(input)){
            System.out.println("Invalid input, please try again");
            System.out.print("\t");
            return false;
        }
        currentResult.setAnswer(currentQuestion-1, input);
        return true;
    }

    /**
     * Retunrs the current question as a string.
     * @return The current question as a string.
     */
    @Override
    public String toString() {
        return this.questions[currentQuestion-1];
    }

    /**
     * Gets the current result.
     * @return The current result.
     */
    public CommandPromptResult getCurrentResult() {
        return currentResult;
    }

    /**
     * Attaches an observer.
     * @param observer The observer to attach.
     */
    public void attach(CommandObserver observer) {
        observers.add(observer);
    }

    /**
     * Detaches an observer.
     * @param observer The observer to detach.
     */
    public void detach(CommandObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies the observers.
     */
    public void notifyObservers() {
        for (CommandObserver observer : observers) {
            observer.updateCommand(this.currentResult);
        }
    }

    /**
     * @return Whether the command is local.
     */
    public boolean isLocal() {
        return isLocal;
    }
}
