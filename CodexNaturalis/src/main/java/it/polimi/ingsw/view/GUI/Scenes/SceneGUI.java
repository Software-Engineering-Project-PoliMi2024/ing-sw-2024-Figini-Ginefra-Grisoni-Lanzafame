package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.GUI.StateGUI;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public abstract class SceneGUI {
    protected final AnchorPane content = new AnchorPane();

    public SceneGUI() {
        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
    }

    public AnchorPane getContent() {
        return content;
    }

    public void add(Node node) {
        content.getChildren().add(node);
    }

    public void remove(Node node) {
        content.getChildren().remove(node);
    }
}
