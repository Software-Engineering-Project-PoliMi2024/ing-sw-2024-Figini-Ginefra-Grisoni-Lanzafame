package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.socket.SocketController;
import it.polimi.ingsw.view.TUI.Renderables.CommandDisplayRenderable;
import it.polimi.ingsw.view.TUI.Renderables.EchoRenderable;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.InputHandler;
import it.polimi.ingsw.view.View;

import java.util.function.Predicate;

public class TUI extends View{
    private final InputHandler inputHandler = new InputHandler();

    private final CommandDisplayRenderable commandDisplay = new CommandDisplayRenderable(null);

    public TUI(Controller controller){
        super(controller);
        inputHandler.attach(commandDisplay);

        Renderable echoRenderable = new EchoRenderable(new CommandPrompt[]{CommandPrompt.ECHO});

        CommandPrompt.ECHO.attach(echoRenderable);

        commandDisplay.addCommandPrompt(CommandPrompt.ECHO);
    }

    public void run() {
        inputHandler.start();
        commandDisplay.setActive(true);
        commandDisplay.render();
    }

    public static void main(String[] args) {
        TUI tui = new TUI(new SocketController());
        tui.run();
    }

}
