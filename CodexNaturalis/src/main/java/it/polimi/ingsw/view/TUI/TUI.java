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

public class TUI implements ActualView, CommandObserver, Observer {
    private ControllerInterface controller;
    private ViewState state;
    private final InputHandler inputHandler = new InputHandler();
    private final CommandDisplayRenderable commandDisplay;
    private final List<Renderable> renderables;
    private final CardMuseum cardMuseum = new CardMuseumFactory(Configs.CardResourcesFolderPath, OSRelated.cardFolderDataPath).getCardMuseum();
    private final ConnectFormRenderable connectForm;
    private LoginFormRenderable loginForm;
    private GameListRenderable gameList;
    private LobbyRenderable lobbyRenderable;
    private ChooseStartCardRenderable chooseStartCardRenderable;
    private ChooseObjectiveCardRenderable chooseObjectiveCardRenderable;
    private HandRenderable handRenderable;
    private PlaceCardForm placeCardForm;
    private CodexRenderable codexRenderable;
    private CodexRenderableOthers codexRenderableOthers;
    private HandOthersRenderable handOthersRenderable;
    private DeckRenderable deckRenderable;
    private PostGameStateRenderable postGameStateRenderable;
    private DrawCardForm drawCardForm;
    private LeaderboardRenderable leaderboardRenderable;
    private ChatRenderable chatRenderable;
    private PawnChoiceRenderable pawnChoiceRenderable;
    private LoggerRenderable logger;
    private final LightGame lightGame = new LightGame();
    private final LightLobby lightLobby = new LightLobby();
    private final LightLobbyList lightLobbyList = new LightLobbyList();

    private AtomicBoolean isTransitioning = new AtomicBoolean(false);

    public TUI(){
        super();
        System.out.println(PromptStyle.Title);
        commandDisplay = new CommandDisplayRenderable("Commands");

        inputHandler.attach(commandDisplay);

        connectForm = new ConnectFormRenderable("Connect form", this, new CommandPrompt[]{CommandPrompt.CONNECT});
        StateTUI.SERVER_CONNECTION.attach(connectForm);

        renderables = new ArrayList<>();
        renderables.add(commandDisplay);
        renderables.add(connectForm);

        loginForm = new LoginFormRenderable("Login Form", new CommandPrompt[]{CommandPrompt.LOGIN}, this);
        StateTUI.LOGIN_FORM.attach(loginForm);
        renderables.add(loginForm);

        gameList = new GameListRenderable("Game List", lightLobbyList, new CommandPrompt[]{CommandPrompt.DISPLAY_GAME_LIST, CommandPrompt.JOIN_GAME, CommandPrompt.CREATE_GAME}, this);
        StateTUI.JOIN_LOBBY.attach(gameList);
        renderables.add(gameList);

        lobbyRenderable = new LobbyRenderable("Lobby", lightLobby, new CommandPrompt[]{CommandPrompt.DISPLAY_LOBBY, CommandPrompt.LEAVE_LOBBY}, this);
        StateTUI.LOBBY.attach(lobbyRenderable);
        renderables.add(lobbyRenderable);

        chooseStartCardRenderable = new ChooseStartCardRenderable(
                "Choose Start Card",
                cardMuseum,
                lightGame,
                new CommandPrompt[]{CommandPrompt.DISPLAY_START_FRONT, CommandPrompt.DISPLAY_START_BACK, CommandPrompt.CHOOSE_START_SIDE},
                this);
        StateTUI.CHOOSE_START_CARD.attach(chooseStartCardRenderable);
        renderables.add(chooseStartCardRenderable);

        chooseObjectiveCardRenderable = new ChooseObjectiveCardRenderable(
                "Choose Objective Card",
                cardMuseum,
                lightGame,
                new CommandPrompt[]{CommandPrompt.DISPLAY_OBJECTIVE_OPTIONS, CommandPrompt.CHOOSE_OBJECTIVE_CARD},
                this,
                lightGame);
        StateTUI.SELECT_OBJECTIVE.attach(chooseObjectiveCardRenderable);
        renderables.add(chooseObjectiveCardRenderable);

        handRenderable = new HandRenderable(
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

        placeCardForm = new PlaceCardForm(
                "Place Card",
                lightGame,
                new CommandPrompt[]{CommandPrompt.PLACE_CARD},
                this);
        StateTUI.PLACE_CARD.attach(placeCardForm);
        renderables.add(placeCardForm);

        codexRenderable = new CodexRenderable(
                "Codex",
                lightGame,
                cardMuseum,
                new CommandPrompt[]{CommandPrompt.DISPLAY_CODEX},
                this);
        StateTUI.SELECT_OBJECTIVE.attach(codexRenderable);
        StateTUI.IDLE.attach(codexRenderable);
        StateTUI.WAITING_STATE.attach(codexRenderable);
        StateTUI.DRAW_CARD.attach(codexRenderable);
        StateTUI.PLACE_CARD.attach(codexRenderable);
        renderables.add(codexRenderable);

        handOthersRenderable = new HandOthersRenderable(
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

        codexRenderableOthers = new CodexRenderableOthers(
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

        deckRenderable = new DeckRenderable(
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

        pawnChoiceRenderable = new PawnChoiceRenderable(
                "Choose Pawn",
                new CommandPrompt[]
                        {CommandPrompt.CHOOSE_PAWN, CommandPrompt.DISPLAY_PAWN_OPTIONS},
                this,
                lightGame);
        StateTUI.CHOOSE_PAWN.attach((pawnChoiceRenderable));
        renderables.add(pawnChoiceRenderable);

        drawCardForm = new DrawCardForm(
                "Draw Card",
                new CommandPrompt[]{CommandPrompt.DRAW_CARD},
                this);
        StateTUI.DRAW_CARD.attach(drawCardForm);
        renderables.add(drawCardForm);

        leaderboardRenderable = new LeaderboardRenderable(
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

        postGameStateRenderable = new PostGameStateRenderable(
                "Post Game",
                lightGame,
                new CommandPrompt[]{CommandPrompt.DISPLAY_POSTGAME, CommandPrompt.LEAVE},
                this);

        StateTUI.GAME_ENDING.attach(postGameStateRenderable);
        renderables.add(postGameStateRenderable);

        chatRenderable = new ChatRenderable("Chat Options",
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

        //TODO: understand why it crushes if I add DISPLAY_OBJECTIVE_OPTIONS
        StateTUI.SELECT_OBJECTIVE.addStartupPrompt(CommandPrompt.DISPLAY_OBJECTIVE_OPTIONS);

        StateTUI.WAITING_STATE.addStartupPrompt(CommandPrompt.DISPLAY_LEADERBOARD);

        StateTUI.IDLE.addStartupPrompt(CommandPrompt.DISPLAY_LEADERBOARD);
        StateTUI.IDLE.addStartupPrompt(CommandPrompt.DISPLAY_DECKS);
        StateTUI.IDLE.addStartupPrompt(CommandPrompt.DISPLAY_CODEX);
        StateTUI.IDLE.addStartupPrompt(CommandPrompt.DISPLAY_HAND);

        StateTUI.PLACE_CARD.addStartupPrompt(CommandPrompt.DISPLAY_LEADERBOARD);
        StateTUI.PLACE_CARD.addStartupPrompt(CommandPrompt.DISPLAY_DECKS);
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

    @Override
    public void run() {
        inputHandler.start();
        commandDisplay.setActive(true);
    }

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

    @Override
    public void log(String logMsg) {
        logger.addLog(logMsg, StringStyle.GREEN_FOREGROUND);
    }

    @Override
    public void logOthers(String logMsg){
        logger.addLog(logMsg, StringStyle.PURPLE_FOREGROUND);
        transitionTo(state);
    }

    @Override
    public void logGame(String logMsg){
        logger.addLog(logMsg, StringStyle.BLUE_FOREGROUND);
    }

    @Override
    public void logChat(String logMsg) {
        logger.addLog(logMsg, StringStyle.GOLD_FOREGROUND);
        this.transitionTo(state);
    }


    @Override
    public void logErr(String logMsg) {
        logger.addLog(logMsg, StringStyle.RED_FOREGROUND);
    }

    @Override
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff) {
        diff.apply(lightLobbyList);
    }

    @Override
    public void updateLobby(ModelDiffs<LightLobby> diff) {
        diff.apply(lightLobby);
    }

    @Override
    public void updateGame(ModelDiffs<LightGame> diff) {
        diff.apply(lightGame);
    }

    @Override
    public void setFinalRanking(List<String> ranking) {

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

    @Override
    public ControllerInterface getController() {
        return controller;
    }

    @Override
    public void setController(ControllerInterface controller) {
        this.controller = controller;
    }

    @Override
    public void updateCommand(CommandPromptResult input) {
        if(input.getCommand() == CommandPrompt.REFRESH){
            this.transitionTo(state);
        }
    }

    @Override
    public void update() {
        if(!isTransitioning.get())
            this.transitionTo(state);
    }
}
