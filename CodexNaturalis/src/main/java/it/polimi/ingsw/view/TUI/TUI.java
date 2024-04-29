package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.socket.SocketController;
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
import it.polimi.ingsw.view.TUI.Renderables.CommandDisplayRenderable;
import it.polimi.ingsw.view.TUI.Renderables.EchoRenderable;
import it.polimi.ingsw.view.TUI.Renderables.GameListRenderable;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.States.StateTUI;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.InputHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;

import java.util.Arrays;
import java.util.List;

public class TUI extends View{
    private final InputHandler inputHandler = new InputHandler();
    private final CommandDisplayRenderable commandDisplay = new CommandDisplayRenderable(null);

    private final Renderable[] renderables;

    private final GameListRenderable gameListRenderable;


    public TUI(Controller controller){
        super(controller);
        inputHandler.attach(commandDisplay);

        commandDisplay.addCommandPrompt(CommandPrompt.ECHO);

        Renderable echoRenderable = new EchoRenderable(new CommandPrompt[]{CommandPrompt.ECHO});

        StateTUI.STATE0.attach(echoRenderable);

        renderables = new Renderable[]{echoRenderable};

        gameListRenderable = new GameListRenderable(new CommandPrompt[]{CommandPrompt.JOIN_GAME});

        StateTUI.STATE0.attach(gameListRenderable);

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
    public void updateGameList(ModelDiffs<LightLobby> diff) {

    }

    @Override
    public void updateLobby(ModelDiffs<LightLobby> diff) {

    }

    @Override
    public void setStartCard(LightCard startCard) {

    }

    @Override
    public void updateDecks(ModelDiffs<LightDeck> deck) {

    }

    @Override
    public void setSecretObjectiveOptions(List<LightCard> objectiveCards) {

    }

    @Override
    public void leaderBoardSetNicks(String[] nicks) {

    }

    @Override
    public void leaderBoardUpdatePoints(int[] points) {

    }

    @Override
    public void playerOrderUpdateActivesNicks(String[] nicks) {

    }

    @Override
    public void playerOrderSetPlayerOrder(String[] nicks) {

    }

    @Override
    public void playerOrderUpdateCurrentPlayer(String nick) {

    }

    @Override
    public void updateCodex(ModelDiffs<LightCodex> codex) {

    }

    @Override
    public void updateHand(ModelDiffs<LightHand> hand) {

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
        TUI tui = new TUI(new SocketController());
        tui.run();
    }
}
