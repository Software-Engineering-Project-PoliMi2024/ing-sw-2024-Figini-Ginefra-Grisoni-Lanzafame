package it.polimi.ingsw.view.GUI.Scenes;

import it.polimi.ingsw.view.GUI.AssetsGUI;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.*;

/**
 * The abstract class that represents a scene in the GUI
 */
public abstract class SceneGUI {
    /** The content of the scene */
    protected final AnchorPane content = new AnchorPane();

    /**
     * Constructor of the scene
     */
    public SceneGUI() {
        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);

        //Set the background
        this.setBackground();
    }

    /**
     * Getter of the content
     * @return the content of the scene
     */
    public AnchorPane getContent() {
        return content;
    }

    /**
     * Add a node to the content
     * @param node the node to add
     */
    public void add(Node node) {
        content.getChildren().add(node);
    }

    /**
     * Remove a node from the content
     * @param node the node to remove
     */
    public void remove(Node node) {
        content.getChildren().remove(node);
    }

    /**
     * Set the background of the scene
     */
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
