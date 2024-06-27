package it.polimi.ingsw.view.TUI.Renderables.Forms;

import it.polimi.ingsw.connectionLayer.VirtualLayer.VirtualController;
import it.polimi.ingsw.connectionLayer.RMI.VirtualRMI.VirtualControllerRMI;
import it.polimi.ingsw.connectionLayer.Socket.VirtualSocket.VirtualControllerSocket;
import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.view.TUI.Printing.Printer;
import it.polimi.ingsw.view.TUI.Styles.PromptStyle;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

/**
 * This class is a Renderable that represents a connect form.
 */
public class ConnectFormRenderable extends FormRenderable {
    /** The view reference that will be sent to the model. */
    private final ActualView view;

    /**
     * Creates a new ConnectFormRenderable.
     * @param name The name of the renderable.
     * @param view The view reference that will be sent to the model.
     * @param relatedCommands The commands related to this renderable.
     */
    public ConnectFormRenderable(String name, ActualView view, CommandPrompt[] relatedCommands) {
        super(name, relatedCommands, null);
        this.view = view;
    }

    /**
     * Renders the connect form.
     */
    public void updateCommand(CommandPromptResult command){
        int protocol = Integer.parseInt(command.getAnswer(0));
        VirtualController controller = protocol == 0 ? new VirtualControllerSocket() : new VirtualControllerRMI();
        try {
            view.setController(controller);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String ip = command.getAnswer(1);
        int port = Integer.parseInt(command.getAnswer(2));
        try {
            controller.connect(ip, port, view);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the renderable as active. In this case it also prints the title.
     */
    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (active) {
            Printer.println(PromptStyle.Title);
        }
    }
}