package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterfaceClient;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public class ConnectFormRenderable extends FormRenderable {
    public ConnectFormRenderable(String name, CommandPrompt[] relatedCommands, ControllerInterfaceClient controller) {
        super(name, relatedCommands, controller);
    }

    public void updateCommand(CommandPromptResult command){
        String ip = command.getAnswer(0);
        int port = Integer.parseInt(command.getAnswer(1));
        controller.connect(ip, port);
    }
}