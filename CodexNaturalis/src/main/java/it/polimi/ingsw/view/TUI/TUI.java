package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.utils.OSRelated;
import it.polimi.ingsw.controller.Interfaces.ControllerInterface;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.utils.designPatterns.Observed;
import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.view.TUI.Printing.Printable;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Renderables.*;
import it.polimi.ingsw.view.TUI.Renderables.CardRelated.*;
import it.polimi.ingsw.view.TUI.Renderables.CodexRelated.CodexRenderable;
import it.polimi.ingsw.view.TUI.Renderables.CodexRelated.CodexRenderableOthers;
import it.polimi.ingsw.view.TUI.Renderables.Forms.ConnectFormRenderable;
import it.polimi.ingsw.view.TUI.Renderables.Forms.DrawCardForm;
import it.polimi.ingsw.view.TUI.Renderables.Forms.LoginFormRenderable;
import it.polimi.ingsw.view.TUI.Renderables.Forms.PlaceCardForm;
import it.polimi.ingsw.view.TUI.Renderables.LogRelated.LoggerRenderable;
import it.polimi.ingsw.view.TUI.Renderables.PawnChoiceRenderable;
import it.polimi.ingsw.view.TUI.States.StateTUI;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseumFactory;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;
import it.polimi.ingsw.view.TUI.inputs.InputHandler;
import it.polimi.ingsw.view.TUI.observers.CommandObserver;
import it.polimi.ingsw.view.ViewState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class represents the Text User Interface.
 * It setups and contains all the renderables as well as the logic to switch between them.
 * It also contains the logic to handle the commands and the input.
 */
public class TUI implements ActualView, CommandObserver, Observer {
    /** The controller to interact with. */
    private ControllerInterface controller;
    /** The current view state. */
    private ViewState state;
    /** The component responsible for handling the user input. */
    private final InputHandler inputHandler = new InputHandler();
    /** The component responsible for displaying the commands. */
    private final CommandDisplayRenderable commandDisplay;
    /** The list of renderables that will be displayed. */
    private final List<Renderable> renderables;

    /** The component responsible for displaying the logs. */
    private final LoggerRenderable logger;


    //==============================================================================================
    // LIGHT MODEL
    //==============================================================================================

    /** The part of the light model responsible for all that is related to the game. */
    private final LightGame lightGame = new LightGame();

    /** The part of the light model responsible for all that is related to the lobby. */
    private final LightLobby lightLobby = new LightLobby();

    /** The part of the light model responsible for all that is related to the lobby list. */
    private final LightLobbyList lightLobbyList = new LightLobbyList();

    //==============================================================================================

    /**
     * A flag that indicates if the TUI is transitioning between states.
     * This is used to avoid multiple transitions at the same time.
     * */
    private final AtomicBoolean isTransitioning = new AtomicBoolean(false);

    /**
     * Creates a new TUI.
     * It setups all the renderables by injecting the necessary dependencies and by setting the related commands.
     * Each renderable is then attached to the states that it should be displayed in and is added to the list of renderables.
     * Finally, the states startup prompts are set.
     */
    public TUI(){
        super();
        commandDisplay = new CommandDisplayRenderable("Commands");

        inputHandler.attach(commandDisplay);

        ConnectFormRenderable connectForm = new ConnectFormRenderable("Connect form", this, new CommandPrompt[]{CommandPrompt.CONNECT});
        StateTUI.SERVER_CONNECTION.attach(connectForm);

        renderables = new ArrayList<>();
        renderables.add(commandDisplay);
        renderables.add(connectForm);

        LoginFormRenderable loginForm = new LoginFormRenderable("Login Form", new CommandPrompt[]{CommandPrompt.LOGIN}, this);
        StateTUI.LOGIN_FORM.attach(loginForm);
        renderables.add(loginForm);

        GameListRenderable gameList = new GameListRenderable("Game List", lightLobbyList, new CommandPrompt[]{CommandPrompt.DISPLAY_GAME_LIST, CommandPrompt.JOIN_GAME, CommandPrompt.CREATE_GAME}, this);
        StateTUI.JOIN_LOBBY.attach(gameList);
        renderables.add(gameList);

        LobbyRenderable lobbyRenderable = new LobbyRenderable("Lobby", lightLobby, new CommandPrompt[]{CommandPrompt.DISPLAY_LOBBY, CommandPrompt.LEAVE_LOBBY}, this);
        StateTUI.LOBBY.attach(lobbyRenderable);
        renderables.add(lobbyRenderable);

        ChatRenderable chatRenderable = new ChatRenderable("Chat Options",
                new CommandPrompt[]{CommandPrompt.SEND_PUBLIC_MESSAGE, CommandPrompt.SEND_PRIVATE_MESSAGE, CommandPrompt.VIEW_MESSAGE},
                lightGame,
                this);
        StateTUI.CHOOSE_START_CARD.attach(chatRenderable);
        StateTUI.CHOOSE_PAWN.attach(chatRenderable);
        StateTUI.SELECT_OBJECTIVE.attach(chatRenderable);
        StateTUI.IDLE.attach(chatRenderable);
        StateTUI.WAITING_STATE.attach(chatRenderable);
        StateTUI.PLACE_CARD.attach(chatRenderable);
        StateTUI.DRAW_CARD.attach(chatRenderable);
        StateTUI.GAME_ENDING.attach(chatRenderable);
        renderables.add(chatRenderable);

        CardMuseum cardMuseum = new CardMuseumFactory(Configs.CardResourcesFolderPath, OSRelated.cardFolderDataPath).getCardMuseum();
        ChooseStartCardRenderable chooseStartCardRenderable = new ChooseStartCardRenderable(
                "Choose Start Card",
                cardMuseum,
                lightGame,
                new CommandPrompt[]{CommandPrompt.DISPLAY_START_FRONT, CommandPrompt.DISPLAY_START_BACK, CommandPrompt.CHOOSE_START_SIDE},
                this);
        StateTUI.CHOOSE_START_CARD.attach(chooseStartCardRenderable);
        renderables.add(chooseStartCardRenderable);

        ChooseObjectiveCardRenderable chooseObjectiveCardRenderable = new ChooseObjectiveCardRenderable(
                "Choose Objective Card",
                cardMuseum,
                lightGame,
                new CommandPrompt[]{CommandPrompt.DISPLAY_OBJECTIVE_OPTIONS, CommandPrompt.CHOOSE_OBJECTIVE_CARD},
                this,
                lightGame);
        StateTUI.SELECT_OBJECTIVE.attach(chooseObjectiveCardRenderable);
        renderables.add(chooseObjectiveCardRenderable);

        HandRenderable handRenderable = new HandRenderable(
                "Hand",
                cardMuseum,
                lightGame,
                new CommandPrompt[]{CommandPrompt.DISPLAY_HAND},
                this);
        StateTUI.SELECT_OBJECTIVE.attach(handRenderable);
        StateTUI.WAITING_STATE.attach(handRenderable);
        StateTUI.PLACE_CARD.attach(handRenderable);
        StateTUI.IDLE.attach(handRenderable);
        StateTUI.DRAW_CARD.attach(handRenderable);
        renderables.add(handRenderable);

        PlaceCardForm placeCardForm = new PlaceCardForm(
                "Place Card",
                lightGame,
                new CommandPrompt[]{CommandPrompt.PLACE_CARD},
                this);
        StateTUI.PLACE_CARD.attach(placeCardForm);
        renderables.add(placeCardForm);

        CodexRenderable codexRenderable = new CodexRenderable(
                "Codex",
                lightGame,
                cardMuseum,
                new CommandPrompt[]{CommandPrompt.DISPLAY_CODEX, CommandPrompt.MOVE_CODEX, CommandPrompt.RECENTER_CODEX},
                this);
        StateTUI.SELECT_OBJECTIVE.attach(codexRenderable);
        StateTUI.IDLE.attach(codexRenderable);
        StateTUI.WAITING_STATE.attach(codexRenderable);
        StateTUI.DRAW_CARD.attach(codexRenderable);
        StateTUI.PLACE_CARD.attach(codexRenderable);
        renderables.add(codexRenderable);

        HandOthersRenderable handOthersRenderable = new HandOthersRenderable(
                "Hand Others",
                cardMuseum,
                lightGame,
                new CommandPrompt[]{CommandPrompt.PEEK},
                this);
        StateTUI.SELECT_OBJECTIVE.attach(handOthersRenderable);
        StateTUI.WAITING_STATE.attach(handOthersRenderable);
        StateTUI.IDLE.attach(handOthersRenderable);
        StateTUI.DRAW_CARD.attach(handOthersRenderable);
        StateTUI.PLACE_CARD.attach(handOthersRenderable);
        renderables.add(handOthersRenderable);

        CodexRenderableOthers codexRenderableOthers = new CodexRenderableOthers(
                "Codex Others",
                this,
                lightGame,
                cardMuseum,
                new CommandPrompt[]{CommandPrompt.PEEK});
        StateTUI.SELECT_OBJECTIVE.attach(codexRenderableOthers);
        StateTUI.WAITING_STATE.attach(handOthersRenderable);
        StateTUI.IDLE.attach(codexRenderableOthers);
        StateTUI.DRAW_CARD.attach(codexRenderableOthers);
        StateTUI.PLACE_CARD.attach(codexRenderableOthers);
        renderables.add(codexRenderableOthers);

        DeckRenderable deckRenderable = new DeckRenderable(
                "Deck",
                cardMuseum,
                lightGame,
                new CommandPrompt[]{CommandPrompt.DISPLAY_DECKS},
                this);
        StateTUI.CHOOSE_START_CARD.attach(deckRenderable);
        StateTUI.SELECT_OBJECTIVE.attach(deckRenderable);
        StateTUI.WAITING_STATE.attach(handOthersRenderable);
        StateTUI.IDLE.attach(deckRenderable);
        StateTUI.DRAW_CARD.attach(deckRenderable);
        StateTUI.PLACE_CARD.attach(deckRenderable);
        renderables.add(deckRenderable);

        PawnChoiceRenderable pawnChoiceRenderable = new PawnChoiceRenderable(
                "Choose Pawn",
                new CommandPrompt[]
                        {CommandPrompt.CHOOSE_PAWN, CommandPrompt.DISPLAY_PAWN_OPTIONS},
                this,
                lightGame);
        StateTUI.CHOOSE_PAWN.attach((pawnChoiceRenderable));
        renderables.add(pawnChoiceRenderable);

        DrawCardForm drawCardForm = new DrawCardForm(
                "Draw Card",
                new CommandPrompt[]{CommandPrompt.DRAW_CARD},
                this);
        StateTUI.DRAW_CARD.attach(drawCardForm);
        renderables.add(drawCardForm);

        LeaderboardRenderable leaderboardRenderable = new LeaderboardRenderable(
                "Leaderboard",
                lightGame,
                new CommandPrompt[]{CommandPrompt.DISPLAY_LEADERBOARD},
                this);
        StateTUI.CHOOSE_START_CARD.attach(leaderboardRenderable);
        StateTUI.SELECT_OBJECTIVE.attach(leaderboardRenderable);
        StateTUI.WAITING_STATE.attach(leaderboardRenderable);
        StateTUI.IDLE.attach(leaderboardRenderable);
        StateTUI.PLACE_CARD.attach(leaderboardRenderable);
        StateTUI.DRAW_CARD.attach(leaderboardRenderable);
        StateTUI.GAME_ENDING.attach(leaderboardRenderable);
        renderables.add(leaderboardRenderable);

        PostGameStateRenderable postGameStateRenderable = new PostGameStateRenderable(
                "Post Game",
                lightGame,
                new CommandPrompt[]{CommandPrompt.DISPLAY_POSTGAME, CommandPrompt.LEAVE},
                this);

        StateTUI.GAME_ENDING.attach(postGameStateRenderable);
        renderables.add(postGameStateRenderable);

        logger = new LoggerRenderable("Logger");

        //==============================================================================================================
        // SETUP STARTUP PROMPTS FOR THE STATES
        //==============================================================================================================

        StateTUI.JOIN_LOBBY.addStartupPrompt(CommandPrompt.DISPLAY_GAME_LIST);

        StateTUI.LOBBY.addStartupPrompt(CommandPrompt.DISPLAY_LOBBY);

        StateTUI.CHOOSE_START_CARD.addStartupPrompt(CommandPrompt.DISPLAY_LEADERBOARD);
        StateTUI.CHOOSE_START_CARD.addStartupPrompt(CommandPrompt.DISPLAY_DECKS);
        StateTUI.CHOOSE_START_CARD.addStartupPrompt(CommandPrompt.DISPLAY_START_FRONT);
        StateTUI.CHOOSE_START_CARD.addStartupPrompt(CommandPrompt.DISPLAY_START_BACK);

        StateTUI.CHOOSE_PAWN.addStartupPrompt(CommandPrompt.DISPLAY_PAWN_OPTIONS);

        StateTUI.SELECT_OBJECTIVE.addStartupPrompt(CommandPrompt.DISPLAY_OBJECTIVE_OPTIONS);

        StateTUI.WAITING_STATE.addStartupPrompt(CommandPrompt.DISPLAY_LEADERBOARD);

        StateTUI.IDLE.addStartupPrompt(CommandPrompt.DISPLAY_LEADERBOARD);
        StateTUI.IDLE.addStartupPrompt(CommandPrompt.DISPLAY_CODEX);
        StateTUI.IDLE.addStartupPrompt(CommandPrompt.DISPLAY_HAND);

        StateTUI.PLACE_CARD.addStartupPrompt(CommandPrompt.DISPLAY_LEADERBOARD);
        StateTUI.PLACE_CARD.addStartupPrompt(CommandPrompt.DISPLAY_CODEX);
        StateTUI.PLACE_CARD.addStartupPrompt(CommandPrompt.DISPLAY_HAND);

        StateTUI.DRAW_CARD.addStartupPrompt(CommandPrompt.DISPLAY_LEADERBOARD);
        StateTUI.DRAW_CARD.addStartupPrompt(CommandPrompt.DISPLAY_CODEX);
        StateTUI.DRAW_CARD.addStartupPrompt(CommandPrompt.DISPLAY_HAND);
        StateTUI.DRAW_CARD.addStartupPrompt(CommandPrompt.DISPLAY_DECKS);

        StateTUI.GAME_ENDING.addStartupPrompt(CommandPrompt.DISPLAY_POSTGAME);

        CommandPrompt.REFRESH.attach(this);
        this.transitionTo(ViewState.SERVER_CONNECTION);
    }

    /**
     * Starts the TUI.
     * It starts the input handler and sets the command display as active.
     */
    @Override
    public void run() {
        inputHandler.start();
        commandDisplay.setActive(true);
    }

    /**
     * Transitions to a new view state.
     * In doing so the terminal is cleared, the renderables and the commands are updated, the logger is rendered, the startup prompts are triggered and the command display is rendered.
     * When transitioning to the same state, the view is refreshed.
     * @param state The new view state to transition to.
     */
    @Override
    public synchronized void transitionTo(ViewState state){
        isTransitioning.set(true);

        OSRelated.clearTerminal();

        StateTUI stateTUI = Arrays.stream(StateTUI.values()).reduce((a, b) -> a.references(state) ? a : b).orElse(null);

        if (stateTUI == null) {
            throw new IllegalArgumentException("Current View State(" + state.name() + ") not supported by TUI");
        }

        if(this.state != state){
            this.state = state;

            for (Renderable renderable : renderables) {
                renderable.setActive(false);

                List<Observed> observed = renderable.getObservedLightModel();
                for (Observed observedLightModel : observed) {
                    observedLightModel.detach(this);
                }
            }

            for (Renderable renderable : stateTUI.getRenderables()) {
                renderable.setActive(true);

                List<Observed> observed = renderable.getObservedLightModel();
                for (Observed observedLightModel : observed) {
                    observedLightModel.attach(this);
                }
            }

            updateCommands();

            //logger.clearLogs();
        }


        logger.render();
        stateTUI.triggerStartupPrompts();
        commandDisplay.render();

        isTransitioning.set(false);

    }

    /**
     * Logs a message to the logger.
     * @param logMsg The message to log.
     */
    @Override
    public void log(String logMsg) {
        logger.addLog(logMsg, StringStyle.GREEN_FOREGROUND);
    }

    /**
     * Logs a message related other player activities to the logger with a specific style.
     * @param logMsg The message to log.
     */
    @Override
    public void logOthers(String logMsg){
        logger.addLog(logMsg, StringStyle.PURPLE_FOREGROUND);
        transitionTo(state);
    }

    /**
     * Logs a message related to the game to the logger with a specific style.
     * @param logMsg The message to log.
     */
    @Override
    public void logGame(String logMsg){
        logger.addLog(logMsg, StringStyle.BLUE_FOREGROUND);
    }

    /**
     * Logs a message related to the chat to the logger with a specific style.
     * @param logMsg The message to log.
     */
    @Override
    public void logChat(String logMsg) {
        logger.addLog(logMsg, StringStyle.GOLD_FOREGROUND);
        this.transitionTo(state);
    }

    /**
     * Logs an error message to the logger with a specific style.
     * @param logMsg The message to log.
     */
    @Override
    public void logErr(String logMsg) {
        logger.addLog(logMsg, StringStyle.RED_FOREGROUND);
    }

    /**
     * Applies the given diff to the light lobby list.
     * @param diff The diff to apply.
     */
    @Override
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff) {
        diff.apply(lightLobbyList);
    }

    /**
     * Applies the given diff to the light lobby.
     * @param diff The diff to apply.
     */
    @Override
    public void updateLobby(ModelDiffs<LightLobby> diff) {
        diff.apply(lightLobby);
    }

    /**
     * Applies the given diff to the light game.
     * @param diff The diff to apply.
     */
    @Override
    public void updateGame(ModelDiffs<LightGame> diff) {
        diff.apply(lightGame);
    }

    private void updateCommands(){
        StateTUI stateTUI = Arrays.stream(StateTUI.values()).reduce((a, b) -> a.references(state) ? a : b).orElse(null);
        assert stateTUI != null;

        commandDisplay.clearCommandPrompts();

        commandDisplay.addCommandPrompt(CommandPrompt.REFRESH);

        for(Renderable renderable : renderables){
            if(renderable.isActive()) {
                for (CommandPrompt commandPrompt : renderable.getRelatedCommands()) {
                    if(!stateTUI.isStartupPrompt(commandPrompt))
                        commandDisplay.addCommandPrompt(commandPrompt);
                }
            }
        }
    }

    /**
     * Returns the controller.
     * @return The controller.
     */
    @Override
    public ControllerInterface getController() {
        return controller;
    }

    /**
     * Sets the controller.
     * @param controller The controller to set.
     */
    @Override
    public void setController(ControllerInterface controller) {
        this.controller = controller;
    }

    /**
     * Refreshes the view when the refresh command is triggered.
     */
    @Override
    public void updateCommand(CommandPromptResult input) {
        if(input.getCommand() == CommandPrompt.REFRESH){
            this.transitionTo(state);
        }
    }

    /**
     * Refreshes the view when the observed light model changes.
     */
    @Override
    public void update() {
        if(!isTransitioning.get())
            this.transitionTo(state);
    }
}
