package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.GUI.Root;
import javafx.scene.layout.AnchorPane;

public class PostGameScene extends SceneGUI {
    public PostGameScene() {
        super();
        content.getChildren().add(Root.POST_GAME.getRoot());
    }
}