package it.polimi.ingsw.view.GUI.Components.CodexRelated;

import it.polimi.ingsw.view.GUI.Components.Utils.AnimationStuff;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class ImageCounter {
    private final ImageView image;
    private final Text counterLabel;
    private final StackPane container = new StackPane();

    private final Timeline increaseAnimation = new Timeline();
    private final Timeline decreaseAnimation = new Timeline();

    private int counter = 0;

    public ImageCounter(Image image) {
        this.image = new ImageView(image);
        // Preserve the image ratio
        this.image.setPreserveRatio(true);
        this.image.setFitWidth(GUIConfigs.collectablesWidth);


        this.counterLabel = new Text("0");
        //Remove space inter line
        this.counterLabel.setLineSpacing(0);

        Rectangle labelBg = new Rectangle(50, 50);
        labelBg.setArcWidth(10);
        labelBg.setArcHeight(10);
        labelBg.setStyle("-fx-fill: white;");


        //make the bg as big as the text
        counterLabel.boundsInLocalProperty().addListener((obs, oldBounds, newBounds) -> {
            labelBg.setWidth(newBounds.getWidth());
            labelBg.setHeight(newBounds.getHeight());
        });

        //make the bg follow the text
        counterLabel.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            labelBg.setLayoutX(newBounds.getCenterX());
            labelBg.setLayoutY(newBounds.getCenterY());
        });

        counterLabel.setStyle("-fx-font-size: 20; -fx-background-color: white;");

        container.getChildren().addAll(this.image, labelBg, counterLabel);

        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2);");

        StackPane.setAlignment(labelBg, Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(counterLabel, Pos.BOTTOM_RIGHT);

        increaseAnimation.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), new KeyValue(counterLabel.fillProperty(), Color.GREEN)),
                new KeyFrame(Duration.millis(GUIConfigs.counterGlowDuration), new KeyValue(counterLabel.fillProperty(), Color.BLACK, Interpolator.EASE_BOTH))
        );

        decreaseAnimation.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), new KeyValue(counterLabel.fillProperty(), Color.RED)),
                new KeyFrame(Duration.millis(GUIConfigs.counterGlowDuration), new KeyValue(counterLabel.fillProperty(), Color.BLACK, Interpolator.EASE_BOTH))
        );

    }

    public void setCounter(int counter) {
        counterLabel.setText(String.valueOf(counter));

        if(this.counter < counter){
            increaseAnimation.playFromStart();
        } else if(this.counter > counter){
            decreaseAnimation.playFromStart();
        }

        this.counter = counter;
    }

    public Node getContent() {
        return container;
    }

}
