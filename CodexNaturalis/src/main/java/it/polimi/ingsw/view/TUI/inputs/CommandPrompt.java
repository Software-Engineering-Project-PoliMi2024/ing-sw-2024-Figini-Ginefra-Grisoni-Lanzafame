package it.polimi.ingsw.view.TUI.inputs;

import it.polimi.ingsw.model.utilities.Pair;
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
 * Each command has a name, a list of questions to ask the user, a list of validators to validate the user input.
 * It is also possible to attach observers to the command that are notified when the command prompt is filled out by the user.
 * The command prompt is also iterable, so it is possible to iterate over the questions to ask the user.
 */
public enum CommandPrompt implements Iterator<String>, CommandObserved {
    /** The command to connect to the server */
    CONNECT(new DecoratedString("Connect", StringStyle.YELLOW_FOREGROUND).toString(),
            new String[]{
                    "Which protocol do you want to use? (0/Socket - 1/RMI)",
                    "What's the server IP?",
                    "What's the server port?",
            },
            new Predicate[]{
                    s -> s.equals("0") || s.equals("1"),
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
    JOIN_GAME("Join lobby",
            new String[]{
                    "What's the name of the lobby?",
            },
            new Predicate[]{
                    s -> true,
            },
            false),
    /** The command to display the current list of games with free spots */
    DISPLAY_GAME_LIST("Display lobbies list", true),

    /** The command to display the lobby the player is currently in*/
    DISPLAY_LOBBY("Display lobby", true),

    /** The command to leave the lobby */
    LEAVE_LOBBY("Leave lobby", false),

    /** The command to let the player choose the color of its pawn */
    CHOOSE_PAWN("Choose pawn",
            new String[]{
                    "Which pawn do you want to choose? (RED/BLUE/YELLOW/GREEN)"
            },
            new Predicate[]{
                    s -> {
                        String input = s.toString().toUpperCase();
                        return input.equals("RED") || input.equals("BLUE") || input.equals("YELLOW") || input.equals("GREEN");
                    }
            },
            false),

    /** The command to display the pawn options */
    DISPLAY_PAWN_OPTIONS("Display pawn", true),

    /** The command to display the assigned start card front */
    DISPLAY_START_FRONT("Display start card front", true),

    /** The command to display the assigned start card back */
    DISPLAY_START_BACK("Display start card back", true),

    /** The command to choose the side of the start card */
    CHOOSE_START_SIDE("Choose start card side",
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
    DISPLAY_HAND("Display hand",
            new String[]{},
            new Predicate[]{},
            true),

    /** The command to place a card */
    PLACE_CARD("Place card",
            new String[]{
                    "Which card do you want to place? (1/2/3)",
                    "Face up or face down? (0/1)",
                    "Where do you want to place it?",
            },
            new Predicate[]{
                    s -> {
                        try {
                            int i = Integer.parseInt(s.toString());
                            return i > 0 && i < 4;
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
            false),

    /** The command to display the codex */
    DISPLAY_CODEX("Display codex", true),

    /** The command to move the center of the codex */
    MOVE_CODEX("Move Codex",
            new String[]{
                    "Insert a Δx value in number of cards (positive to the right)",
                    "Insert a Δy value in number of cards (positive upwards)",
            },
            new Predicate[]{
                    s -> {
                        try {
                            int i = Integer.parseInt(s.toString());
                            return true;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    },
                    s -> {
                        try {
                            int i = Integer.parseInt(s.toString());
                            return true;
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    },
            },
            true),

    /** The command to recenter the codex */
    RECENTER_CODEX("Recenter Codex", true),

    /** The command to peek at a player */
    PEEK("Peek",
            new String[]{
                    "Which Player do you want to peek? Insert the nickname",
            },
            new Predicate[]{
                    s -> true,
            },
            true),

    /** The command to draw a card */
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

    /** The command to display the leaderboard */
    DISPLAY_LEADERBOARD("Display leaderboard", true),

    /** The command to display the decks */
    DISPLAY_DECKS("Display decks", true),

    /** The command to display the winner list after the game has ended */
    DISPLAY_POSTGAME("Display winners", true),

    /** The command to send a public message */
    SEND_PUBLIC_MESSAGE("Send public message",
            new String[]{
                    "What do you want to say?",
            },
            new Predicate[]{
                    s -> true,
            },
            false),

    /** The command to send a private message */
    SEND_PRIVATE_MESSAGE("Send private message",
            new String[]{
                    "Who do you want to send the message to?",
                    "What do you want to say?",
            },
            new Predicate[]{
                    s -> true,
                    s -> true,
            }, false),

    /** The command to view the chat history */
    VIEW_MESSAGE("View message",
        new String[]{
                "View chat history - View received private messages - View sent messages (0/1/2)",
        }, new Predicate[]{
                s -> s.equals("0") || s.equals("1") || s.equals("2"),
        }, true),

    /** The command to leave the game */
    LEAVE("Leave", false),

    /** The command to refresh the display */
    REFRESH("Refresh", false);

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
    private final boolean isLocal;

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
     * Gets the command name.
     * @return The command name.
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * @return Whether there are more questions yet to be asked.
     */
    @Override
    public boolean hasNext() {
        return currentQuestion < questions.length;
    }

    /**
     * Gets the next question to ask and increments the current question.
     * @return The next question to ask.
     */
    @Override
    public String next() {
        return questions[currentQuestion++];
    }

    /**
     * Gets the current question.
     * @return The current question.
     */
    public String current(){
        return questions[currentQuestion];
    }

    /**
     * Gets the last question.
     * @return the last question.
     */
    public String last(){
        return questions[currentQuestion-1];
    }

    /**
     * @return Whether there is a last question.
     */
    public boolean hasLast(){
        return currentQuestion > 0 && currentQuestion <= questions.length;
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
     * Resets the command. The current question is set to 0 and the current result is rested.
     */
    public void reset() {
        currentQuestion = 0;
        currentResult = new CommandPromptResult(this, new String[questions.length]);
    }

    /**
     * Validates the answer. it uses the validator relative to the last question asked.
     * @param answer The answer to validate.
     * @return Whether the answer is valid.
     */
    public boolean validate(String answer) {
        return validators[currentQuestion-1].test(answer);
    }

    /**
     * Parses the input by validating it and setting it as the answer to the current question when valid.
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
     * Returns the current question as a string.
     * @return The current question as a string.
     */
    @Override
    public String toString() {
        return this.questions[currentQuestion-1];
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
     * Notifies the observers with the current result.
     */
    public void notifyObservers() {
        for (CommandObserver observer : observers) {
            observer.updateCommand(this.currentResult);
        }
    }

    /**
     * Notifies the observers with the given result.
     * @param result The result to notify.
     */
    public void notifyObservers(CommandPromptResult result) {
        for (CommandObserver observer : observers) {
            observer.updateCommand(result);
        }
    }

    /**
     * @return Whether the command is local.
     */
    public boolean isLocal() {
        return isLocal;
    }

    /**
     * Gets the questions and answers asked/answered so far.
     * @return The questions and answers so far.
     */
    public List<Pair<String, String>> getQnASoFar(){
        List<Pair<String, String>> qna = new ArrayList<>();
        for(int i = 0; i < currentQuestion; i++){
            if(currentResult.getAnswer(i) != null){
                qna.add(new Pair<>(questions[i], currentResult.getAnswer(i)));
            }
        }
    return qna;
    }
}
