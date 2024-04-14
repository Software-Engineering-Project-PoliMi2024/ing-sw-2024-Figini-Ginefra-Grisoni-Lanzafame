package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.socket.SocketController;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.view.TUI.Renderables.EchoRenderable;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.View;

import java.util.List;
import java.util.Scanner;

public class TUI extends View{
    private final InputHandler inputHandler = new InputHandler();

    private final Renderable echo = new EchoRenderable();

    public TUI(Controller controller){
        super(controller);
        inputHandler.attach(echo);
    }

    public void run() {
        Thread inputThread = new Thread(inputHandler);
        inputThread.start();
    }

    public static void main(String[] args) {
        TUI tui = new TUI(new SocketController());
        tui.run();
    }

}
