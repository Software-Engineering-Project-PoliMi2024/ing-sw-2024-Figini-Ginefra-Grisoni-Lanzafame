package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller2.ConnectionLayer.ConnectionLayerClient;
import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Renderables.*;
import it.polimi.ingsw.view.TUI.Renderables.CardRelated.*;
import it.polimi.ingsw.view.TUI.Renderables.CodexRelated.CodexRenderable;
import it.polimi.ingsw.view.TUI.Renderables.CodexRelated.CodexRenderableOthers;
import it.polimi.ingsw.view.TUI.Renderables.Forms.ConnectFormRenderable;
import it.polimi.ingsw.view.TUI.Renderables.Forms.DrawCardForm;
import it.polimi.ingsw.view.TUI.Renderables.Forms.LoginFormRenderable;
import it.polimi.ingsw.view.TUI.Renderables.Forms.PlaceCardForm;
import it.polimi.ingsw.view.TUI.States.StateTUI;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.Styles.StringStyle;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseum;
import it.polimi.ingsw.view.TUI.cardDrawing.CardMuseumFactory;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.InputHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TUI extends View{
    private final InputHandler inputHandler = new InputHandler();
    private final CommandDisplayRenderable commandDisplay;

    private final List<Renderable> renderables;

    private final CardMuseum cardMuseum = new CardMuseumFactory(Configs.CardFolder).getCardMuseum();

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

    private DrawCardForm drawCardForm;

    private final LightGame lightGame = new LightGame();

    private final LightLobby lightLobby = new LightLobby();

    private final LightLobbyList lightLobbyList = new LightLobbyList();


    public TUI(ConnectionLayerClient controller){
        super(controller);
        System.out.println(PromptStyle.Title);
        commandDisplay = new CommandDisplayRenderable("Commands", null, null);

        inputHandler.attach(commandDisplay);

        connectForm = new ConnectFormRenderable("Connect form", this, new CommandPrompt[]{CommandPrompt.CONNECT}, controller);
        StateTUI.SERVER_CONNECTION.attach(connectForm);

        renderables = new ArrayList<>();
        renderables.add(commandDisplay);
        renderables.add(connectForm);


        this.transitionTo(ViewState.SERVER_CONNECTION);

    }

    @Override
    public void postConnectionInitialization(ControllerInterface controller){
        loginForm = new LoginFormRenderable("Login Form", new CommandPrompt[]{CommandPrompt.LOGIN}, controller);
        StateTUI.LOGIN_FORM.attach(loginForm);
        renderables.add(loginForm);

        gameList = new GameListRenderable("Game List", lightLobbyList, new CommandPrompt[]{CommandPrompt.DISPLAY_GAME_LIST, CommandPrompt.JOIN_GAME, CommandPrompt.CREATE_GAME}, controller);
        StateTUI.JOIN_LOBBY.attach(gameList);
        renderables.add(gameList);

        lobbyRenderable = new LobbyRenderable("Lobby", lightLobby, new CommandPrompt[]{CommandPrompt.DISPLAY_LOBBY, CommandPrompt.LEAVE_LOBBY}, controller);
        StateTUI.LOBBY.attach(lobbyRenderable);
        renderables.add(lobbyRenderable);

        chooseStartCardRenderable = new ChooseStartCardRenderable(
                "Choose Start Card",
                cardMuseum,
                lightGame,
                new CommandPrompt[]{CommandPrompt.DISPLAY_START_FRONT, CommandPrompt.DISPLAY_START_BACK, CommandPrompt.CHOOSE_START_SIDE},
                controller);
        StateTUI.CHOOSE_START_CARD.attach(chooseStartCardRenderable);
        renderables.add(chooseStartCardRenderable);

        chooseObjectiveCardRenderable = new ChooseObjectiveCardRenderable(
                "Choose Objective Card",
                cardMuseum,
                lightGame,
                new CommandPrompt[]{CommandPrompt.DISPLAY_OBJECTIVE_OPTIONS, CommandPrompt.CHOOSE_OBJECTIVE_CARD},
                controller);
        StateTUI.SELECT_OBJECTIVE.attach(chooseObjectiveCardRenderable);
        renderables.add(chooseObjectiveCardRenderable);

        handRenderable = new HandRenderable(
                "Hand",
                cardMuseum,
                lightGame,
                new CommandPrompt[]{CommandPrompt.DISPLAY_HAND_FRONT, CommandPrompt.DISPLAY_HAND_BACK, CommandPrompt.DISPLAY_SECRET_OBJECTIVE},
                controller);
        StateTUI.SELECT_OBJECTIVE.attach(handRenderable);
        StateTUI.PLACE_CARD.attach(handRenderable);
        StateTUI.IDLE.attach(handRenderable);
        StateTUI.DRAW_CARD.attach(handRenderable);
        renderables.add(handRenderable);

        placeCardForm = new PlaceCardForm(
                "Place Card",
                lightGame,
                new CommandPrompt[]{CommandPrompt.PLACE_CARD},
                controller);
        StateTUI.PLACE_CARD.attach(placeCardForm);
        renderables.add(placeCardForm);

        codexRenderable = new CodexRenderable(
                "Codex",
                lightGame,
                cardMuseum,
                new CommandPrompt[]{CommandPrompt.DISPLAY_CODEX},
                controller);
        StateTUI.SELECT_OBJECTIVE.attach(codexRenderable);
        StateTUI.IDLE.attach(codexRenderable);
        StateTUI.DRAW_CARD.attach(codexRenderable);
        StateTUI.PLACE_CARD.attach(codexRenderable);
        renderables.add(codexRenderable);

        handOthersRenderable = new HandOthersRenderable(
                "Hand Others",
                this,
                cardMuseum,
                lightGame,
                new CommandPrompt[]{CommandPrompt.PEEK},
                controller);
        StateTUI.SELECT_OBJECTIVE.attach(handOthersRenderable);
        StateTUI.IDLE.attach(handOthersRenderable);
        StateTUI.DRAW_CARD.attach(handOthersRenderable);
        StateTUI.PLACE_CARD.attach(handOthersRenderable);
        renderables.add(handOthersRenderable);

        codexRenderableOthers = new CodexRenderableOthers(
                "Codex Others",
                this,
                lightGame,
                cardMuseum,
                new CommandPrompt[]{CommandPrompt.PEEK},
                controller);
        StateTUI.SELECT_OBJECTIVE.attach(codexRenderableOthers);
        StateTUI.IDLE.attach(codexRenderableOthers);
        StateTUI.DRAW_CARD.attach(codexRenderableOthers);
        StateTUI.PLACE_CARD.attach(codexRenderableOthers);
        renderables.add(codexRenderableOthers);

        deckRenderable = new DeckRenderable(
                "Deck",
                cardMuseum,
                lightGame,
                new CommandPrompt[]{CommandPrompt.DISPLAY_DECKS},
                controller);
        StateTUI.SELECT_OBJECTIVE.attach(deckRenderable);
        StateTUI.IDLE.attach(deckRenderable);
        StateTUI.DRAW_CARD.attach(deckRenderable);
        StateTUI.PLACE_CARD.attach(deckRenderable);
        renderables.add(deckRenderable);

        drawCardForm = new DrawCardForm(
                "Draw Card",
                new CommandPrompt[]{CommandPrompt.DRAW_CARD},
                controller);
        StateTUI.DRAW_CARD.attach(drawCardForm);
        renderables.add(drawCardForm);
    }

    public void run() {
        inputHandler.start();
        commandDisplay.setActive(true);
    }

    @Override
    public void transitionTo(ViewState state){
        this.setState(state);

        StateTUI stateTUI = Arrays.stream(StateTUI.values()).reduce((a, b) -> a.references(state) ? a : b).orElse(null);

        if(stateTUI == null){
            throw new IllegalArgumentException("Current View State(" + stateTUI.name() + ") not supported by TUI");
        }

        for(Renderable renderable : renderables){
            renderable.setActive(false);
        }

        for(Renderable renderable : stateTUI.getRenderables()){
            renderable.setActive(true);
        }

        updateCommands();

        commandDisplay.render();
    }

    @Override
    public void log(String logMsg) {
        Printer.println("");
        PromptStyle.printInABox(logMsg,50, StringStyle.GREEN_FOREGROUND);
        Printer.println("");
    }

    @Override
    public void logErr(String logMsg) {
        Printer.println("");
        PromptStyle.printInABox(logMsg,50, StringStyle.RED_FOREGROUND);
        Printer.println("");
        commandDisplay.render();
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
    public void setFinalRanking(String[] nicks, int[] points) {

    }


    private void updateCommands(){
        commandDisplay.clearCommandPrompts();

        for(Renderable renderable : renderables){
            if(renderable.isActive()) {
                for (CommandPrompt commandPrompt : renderable.getRelatedCommands()) {
                    commandDisplay.addCommandPrompt(commandPrompt);
                }
            }
        }
    }
    public static void main(String[] args) {
        TUI tui = new TUI(null);
        tui.run();
    }

    @Override
    public void isClientOn() {
        //No code but if this call goes through the client is still connected to the server
    }
}
