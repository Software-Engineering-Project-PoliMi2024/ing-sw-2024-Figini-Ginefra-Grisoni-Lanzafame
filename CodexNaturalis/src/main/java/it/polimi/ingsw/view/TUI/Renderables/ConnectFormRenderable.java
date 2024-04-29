package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ConnectionLayerClient;
import it.polimi.ingsw.controller2.ViewInterface;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public class ConnectFormRenderable extends FormRenderable {
    private final ViewInterface view;
    public ConnectFormRenderable(String name, ViewInterface view, CommandPrompt[] relatedCommands, ConnectionLayerClient controller) {
        super(name, relatedCommands, controller);
        this.view = view;
    }

    public void updateCommand(CommandPromptResult command){
        String ip = command.getAnswer(0);
        int port = Integer.parseInt(command.getAnswer(1));
        controller.connect(ip, port, view);
    }
}