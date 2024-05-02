package it.polimi.ingsw.view.TUI.Renderables.Forms;

import it.polimi.ingsw.controller2.ConnectionLayer.ConnectionLayerClient;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public class ConnectFormRenderable extends FormRenderable {
    private final ConnectionLayerClient controller;
    private final ViewInterface view;
    public ConnectFormRenderable(String name, ViewInterface view, CommandPrompt[] relatedCommands, ConnectionLayerClient controller) {
        super(name, relatedCommands, null);
        this.controller = controller;
        this.view = view;
    }

    public void updateCommand(CommandPromptResult command){
        String ip = command.getAnswer(0);
        int port = Integer.parseInt(command.getAnswer(1));
        controller.connect(ip, port, view);
    }
}