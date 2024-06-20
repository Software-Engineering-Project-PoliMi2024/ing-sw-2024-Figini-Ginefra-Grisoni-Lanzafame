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

public class PopUp {
    private final StackPane container = new StackPane();
    private final AnchorPane content = new AnchorPane();
    private final Timeline openingAnimation = new Timeline();
    private final Timeline closingAnimation = new Timeline();
    private final AnchorPane parent;

    private boolean isLocked = false;

    private static KeyValue createOpacityKeyValue(Node target, double value) {
        return new KeyValue(target.opacityProperty(), value, Interpolator.EASE_BOTH);
    }

    private static KeyValue createScaleKeyValue(Node target, double value) {
        return new KeyValue(target.scaleXProperty(), value, Interpolator.EASE_BOTH);
    }

    private void setUpOpeningAnimation() {
        openingAnimation.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), createScaleKeyValue(content, 0.0)),
                new KeyFrame(Duration.millis(300), createScaleKeyValue(content, 1.0)),

                new KeyFrame(Duration.millis(0), createOpacityKeyValue(container, 0.0)),
                new KeyFrame(Duration.millis(300), createOpacityKeyValue(container, 1.0))
        );
    }

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

    public void open() {
        parent.getChildren().add(container);
        openingAnimation.play();
    }

    public void close() {
        closingAnimation.play();
    }

    public AnchorPane getContent() {
        return content;
    }

    public void setLocked(boolean isLocked){
        this.isLocked = isLocked;
    }
}
