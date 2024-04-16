package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.socket.SocketController;
import it.polimi.ingsw.view.TUI.Renderables.CommandDisplayRenderable;
import it.polimi.ingsw.view.TUI.Renderables.EchoRenderable;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.States.StateTUI;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.InputHandler;
import it.polimi.ingsw.view.View;

import java.util.List;
import java.util.function.Predicate;

public class TUI extends View{
    private final InputHandler inputHandler = new InputHandler();
    private final CommandDisplayRenderable commandDisplay = new CommandDisplayRenderable(null);

    private final Renderable[] renderables;

    private StateTUI state = null;

    public TUI(Controller controller){
        super(controller);
        inputHandler.attach(commandDisplay);

        commandDisplay.addCommandPrompt(CommandPrompt.ECHO);

        Renderable echoRenderable = new EchoRenderable(new CommandPrompt[]{CommandPrompt.ECHO});

        StateTUI.STATE0.attach(echoRenderable);

        renderables = new Renderable[]{echoRenderable};

        this.setState(StateTUI.STATE0);

    }

    public void run() {
        inputHandler.start();
        commandDisplay.setActive(true);
        commandDisplay.render();
    }

    public void setState(StateTUI state){
        for(Renderable renderable : renderables){
            renderable.setActive(false);
        }

        for(Renderable renderable : state.getRenderables()){
            renderable.setActive(true);
        }

        this.state = state;

        updateCommands();
    }

    private void updateCommands(){
        commandDisplay.clearCommandPrompts();

        for(Renderable renderable : renderables){
            for(CommandPrompt commandPrompt : renderable.getRelatedCommands()){
                commandDisplay.addCommandPrompt(commandPrompt);
            }
        }
    }
    public static void main(String[] args) {
        TUI tui = new TUI(new SocketController());
        tui.run();
    }
}
