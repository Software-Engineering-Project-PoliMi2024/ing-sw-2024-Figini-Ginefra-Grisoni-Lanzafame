package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.GUI.Root;
import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.scene.control.Label;

/**
 * The scene that contains the server connection form
 */
public class ServerConnectionScene extends SceneGUI{
    public ServerConnectionScene() {
        super();
        content.getChildren().add(Root.SERVER_CONNECTION_FORM.getRoot());
    }
}
