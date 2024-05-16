package it.polimi.ingsw.view.GUI.Components;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.w3c.dom.css.Rect;

public class PopUp {
    private final Rectangle bg = new Rectangle();
    private final AnchorPane content = new AnchorPane();
    private final Timeline openingAnimation = new Timeline();
    private final Timeline closingAnimation = new Timeline();
    private AnchorPane parent;

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

                new KeyFrame(Duration.millis(0), createOpacityKeyValue(bg, 0.0)),
                new KeyFrame(Duration.millis(300), createOpacityKeyValue(bg, 1.0))
        );
    }

    private void setUpClosingAnimation() {
        closingAnimation.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), createScaleKeyValue(content, 1.0)),
                new KeyFrame(Duration.millis(300), createScaleKeyValue(content, 0.0)),

                new KeyFrame(Duration.millis(0), createOpacityKeyValue(bg, 1.0)),
                new KeyFrame(Duration.millis(300), createOpacityKeyValue(bg, 0.0))
        );

        closingAnimation.setOnFinished(e -> {
            parent.getChildren().remove(bg);
            parent.getChildren().remove(content);
        });
    }

    public PopUp(AnchorPane parent) {
        this.parent = parent;

        bg.setStyle("-fx-fill: rgba(0, 0, 0, 0.5);");
        content.setStyle("-fx-background-color: white; -fx-background-radius: 20;");

        content.scaleYProperty().bind(content.scaleXProperty());

        AnchorPane.setTopAnchor(bg, 0.0);
        AnchorPane.setRightAnchor(bg, 0.0);
        AnchorPane.setBottomAnchor(bg, 0.0);
        AnchorPane.setLeftAnchor(bg, 0.0);

        bg.widthProperty().bind(parent.widthProperty());
        bg.heightProperty().bind(parent.heightProperty());

        AnchorPane.setTopAnchor(content, 50.0);
        AnchorPane.setRightAnchor(content, 50.0);
        AnchorPane.setBottomAnchor(content, 50.0);
        AnchorPane.setLeftAnchor(content, 50.0);

        bg.setOnMouseClicked(e -> close());


        // Make clip as big as the content
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(content.widthProperty());
        clip.heightProperty().bind(content.heightProperty());

        clip.setArcWidth(20);
        clip.setArcHeight(20);

        content.setClip(clip);


        this.setUpOpeningAnimation();
        this.setUpClosingAnimation();
    }

    public void open() {
        parent.getChildren().add(bg);
        parent.getChildren().add(content);
        openingAnimation.play();
    }

    public void close() {
        closingAnimation.play();
    }

    public AnchorPane getContent() {
        return content;
    }
}
