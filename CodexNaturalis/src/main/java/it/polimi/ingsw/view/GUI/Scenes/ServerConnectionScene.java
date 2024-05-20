package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.GUI.Root;
import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.scene.control.Label;

public class ServerConnectionScene extends SceneGUI{
    public ServerConnectionScene() {
        super();
        content.getChildren().add(Root.SERVER_CONNECTION_FORM.getRoot());

        content.getChildren().add(new Label("Server Connection Scene"));
    }
}
