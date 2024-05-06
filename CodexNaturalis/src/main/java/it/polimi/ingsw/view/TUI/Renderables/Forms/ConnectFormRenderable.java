package it.polimi.ingsw.view.TUI.Renderables.Forms;

import it.polimi.ingsw.controller2.ConnectionLayer.ConnectionLayerClient;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

import java.rmi.RemoteException;

/**
 * This class is a Renderable that represents a connect form.
 */
public class ConnectFormRenderable extends FormRenderable {
    private final ConnectionLayerClient controller;
    private final ViewInterface view;

    /**
     * Creates a new ConnectFormRenderable.
     * @param name The name of the renderable.
     * @param view The view reference that will be sent to the model.
     * @param relatedCommands The commands related to this renderable.
     * @param controller The controller to interact with.
     */
    public ConnectFormRenderable(String name, ViewInterface view, CommandPrompt[] relatedCommands, ConnectionLayerClient controller) {
        super(name, relatedCommands, null);
        this.controller = controller;
        this.view = view;
    }

    /**
     * Renders the connect form.
     */
    public void updateCommand(CommandPromptResult command){
        String ip = command.getAnswer(0);
        int port = Integer.parseInt(command.getAnswer(1));
        controller.connect(ip, port, view);

    }
}