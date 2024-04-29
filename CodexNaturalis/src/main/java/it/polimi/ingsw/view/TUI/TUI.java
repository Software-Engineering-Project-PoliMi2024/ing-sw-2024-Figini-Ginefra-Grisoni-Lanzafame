package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.controller2.ConnectionLayer.ConnectionLayerClient;
import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.TUI.Renderables.*;
import it.polimi.ingsw.view.TUI.States.StateTUI;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
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

    private List<Renderable> renderables;

    private final ConnectFormRenderable connectForm;

    private LoginFormRenderable loginForm;

    private GameListRenderable gameList;

    private LobbyRenderable lobbyRenderable;

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

        gameList = new GameListRenderable("Game List", new CommandPrompt[]{CommandPrompt.DISPLAY_GAME_LIST, CommandPrompt.JOIN_GAME, CommandPrompt.CREATE_GAME}, controller);
        StateTUI.JOIN_LOBBY.attach(gameList);
        renderables.add(gameList);

        lobbyRenderable = new LobbyRenderable("Lobby", lightLobby, new CommandPrompt[]{CommandPrompt.DISPLAY_LOBBY}, controller);
        StateTUI.LOBBY.attach(lobbyRenderable);
        renderables.add(lobbyRenderable);
    }

    public void run() {
        inputHandler.start();
        commandDisplay.setActive(true);
        commandDisplay.render();
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
    }

    @Override
    public void log(String logMsg) {
        System.out.println(logMsg);
    }

    @Override
    public void updateLobbyList(ModelDiffs<LightLobbyList> diff) {

    }


    @Override
    public void updateLobby(ModelDiffs<LightLobby> diff) {

    }

    @Override
    public void updateGame(ModelDiffs<LightGame> diff) {

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
}
