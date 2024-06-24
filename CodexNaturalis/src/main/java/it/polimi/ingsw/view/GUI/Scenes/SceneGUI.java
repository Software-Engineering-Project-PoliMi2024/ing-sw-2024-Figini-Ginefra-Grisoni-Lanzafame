package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.GUI.StateGUI;
import it.polimi.ingsw.view.GUI.AssetsGUI;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.*;

import java.util.*;

public abstract class SceneGUI {
    protected final AnchorPane content = new AnchorPane();

    public SceneGUI() {
        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);

        //Set the background
        this.setBackground();
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

    private void setBackground() {
        double width = 50;
        double height = width;

        BackgroundImage bgImage = new BackgroundImage(
                AssetsGUI.bgTile,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                new BackgroundPosition(
                        null,
                        0,
                        false,
                        null,
                        0,
                        false
                ),
                new BackgroundSize(
                        width,
                        height,
                        false,
                        false,
                        false,
                        false
                ));
        content.setBackground(new Background(bgImage));
    }
}
