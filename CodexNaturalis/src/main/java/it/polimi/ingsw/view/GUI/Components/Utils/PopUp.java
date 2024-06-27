package it.polimi.ingsw.view.GUI.Components.Utils;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * This class represents a popup that can be used to display content on top of the parent.
 * The popup will animate its opening and closing.
 * The opening can be triggered by calling the open() method, while the closing can be triggered by calling the close() method or by clicking on the background.

 */
public class PopUp {
    /** The container of the popup */
    private final StackPane container = new StackPane();
    /** The content of the popup */
    private final AnchorPane content = new AnchorPane();
    /** The timeline used to animate the opening of the popup */
    private final Timeline openingAnimation = new Timeline();
    /** The timeline used to animate the closing of the popup */
    private final Timeline closingAnimation = new Timeline();
    /** The parent of the popup */
    private final AnchorPane parent;
    /** Whether the popup is open */
    private boolean isOpen = false;
    /** Whether the popup is locked, meaning that it will not close when the mouse clicks on the background*/
    private boolean isLocked = false;

    /** The duration of the animation */
    private static KeyValue createOpacityKeyValue(Node target, double value) {
        return new KeyValue(target.opacityProperty(), value, Interpolator.EASE_BOTH);
    }

    /** The duration of the animation */
    private static KeyValue createScaleKeyValue(Node target, double value) {
        return new KeyValue(target.scaleXProperty(), value, Interpolator.EASE_BOTH);
    }

    /** The duration of the animation */
    private void setUpOpeningAnimation() {
        openingAnimation.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), createScaleKeyValue(content, 0.0)),
                new KeyFrame(Duration.millis(300), createScaleKeyValue(content, 1.0)),

                new KeyFrame(Duration.millis(0), createOpacityKeyValue(container, 0.0)),
                new KeyFrame(Duration.millis(300), createOpacityKeyValue(container, 1.0))
        );
    }

    /** The duration of the animation */
    private void setUpClosingAnimation() {
        closingAnimation.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), createScaleKeyValue(content, 1.0)),
                new KeyFrame(Duration.millis(300), createScaleKeyValue(content, 0.0)),

                new KeyFrame(Duration.millis(0), createOpacityKeyValue(container, 1.0)),
                new KeyFrame(Duration.millis(300), createOpacityKeyValue(container, 0.0))
        );

        closingAnimation.setOnFinished(e -> {
            parent.getChildren().remove(container);
        });
    }

    /**
     * Creates a new PopUp linked to the parent
     * @param parent The AnchroPane that the PopUp will be added to and removed from when opened/closed
     * @param fitContent Whether the PopUp will fit the size of its content (true) or stay at a fixed size (false)
     */
    public PopUp(AnchorPane parent, boolean fitContent) {
        this.parent = parent;

        container.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: white; -fx-background-radius: 20;");

        content.scaleYProperty().bind(content.scaleXProperty());

        AnchorPane.setTopAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);
        AnchorPane.setBottomAnchor(container, 0.0);
        AnchorPane.setLeftAnchor(container, 0.0);


        container.prefWidthProperty().bind(parent.widthProperty());
        container.prefHeightProperty().bind(parent.heightProperty());

        if(!fitContent) {
            container.setPadding(new Insets(50));
        }
        else{
            content.setPrefHeight(.0);
            content.setPrefWidth(.0);
        }

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(content.widthProperty());
        clip.heightProperty().bind(content.heightProperty());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        content.setClip(clip);


        Rectangle backDrop = new Rectangle();
        backDrop.setStyle("-fx-fill: rgba(0, 0, 0, 0.8);");

        backDrop.widthProperty().bind(container.widthProperty());
        backDrop.heightProperty().bind(container.heightProperty());

        backDrop.setOnMouseClicked(e -> {
            if(!this.isLocked)
                close();
        });

        container.getChildren().add(backDrop);
        container.getChildren().add(content);



        this.setUpOpeningAnimation();
        this.setUpClosingAnimation();
    }

    /**
     * Creates a new PopUp of fixed size and linked to the parent
     * @param parent The AnchroPane that the PopUp will be added to and removed from when opened/closed
     */
    public PopUp(AnchorPane parent){
        this(parent, false);
    }

    /**
     * Opens the popup
     */
    public void open() {
        isOpen = true;
        parent.getChildren().add(container);
        openingAnimation.play();
    }

    /**
     * Closes the popup
     */
    public void close() {
        isOpen = false;
        closingAnimation.play();
    }

    /**
     * Gets the content of the popup
     * @return the content of the popup
     */
    public AnchorPane getContent() {
        return content;
    }

    /**
     * Locks the popup, meaning that it will not close when the mouse clicks on the background
     * @param isLocked true to lock the popup, false to unlock it
     */
    public void setLocked(boolean isLocked){
        this.isLocked = isLocked;
    }

    /**
     * @return true if the popup is open, false otherwise
     */
    public boolean isOpen() {
        return isOpen;
    }
}
