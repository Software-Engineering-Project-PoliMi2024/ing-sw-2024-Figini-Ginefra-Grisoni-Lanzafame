package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.socket.SocketController;
import it.polimi.ingsw.controller2.ControllerInterfaceClient;
import it.polimi.ingsw.lightModel.*;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.lightModel.diffs.LobbyListDiff;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Hand;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.view.TUI.Renderables.*;
import it.polimi.ingsw.view.TUI.States.StateTUI;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.InputHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;
import java.util.List;

public class TUI extends View{
    private final InputHandler inputHandler = new InputHandler();
    private final CommandDisplayRenderable commandDisplay;

    private final Renderable[] renderables;

    private final ConnectFormRenderable connectForm;

    private final LoginFormRenderable loginForm;

    private final GameListRenderable gameList;

    private final LightGame lightGame = new LightGame();

    private final LightLobby lightLobby = new LightLobby();

    private final LightLobbyList lightLobbyList = new LightLobbyList();


    public TUI(ControllerInterfaceClient controller){
        super(controller);
        commandDisplay = new CommandDisplayRenderable("Commands", null, controller);

        inputHandler.attach(commandDisplay);

        commandDisplay.addCommandPrompt(CommandPrompt.CONNECT);

        connectForm = new ConnectFormRenderable("Connect form", new CommandPrompt[]{CommandPrompt.CONNECT}, controller);
        StateTUI.SERVER_CONNECTION.attach(connectForm);

        loginForm = new LoginFormRenderable("Login Form", new CommandPrompt[]{CommandPrompt.DISPLAY_LOBBY, CommandPrompt.LOGIN}, controller);
        StateTUI.LOGIN_FORM.attach(loginForm);

        gameList = new GameListRenderable("Game List", new CommandPrompt[]{CommandPrompt.DISPLAY_GAME_LIST, CommandPrompt.JOIN_GAME}, controller);
        StateTUI.JOIN_GAME.attach(gameList);

        renderables = new Renderable[]{commandDisplay, connectForm, loginForm};

        this.setState(ViewState.SERVER_CONNECTION);

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
