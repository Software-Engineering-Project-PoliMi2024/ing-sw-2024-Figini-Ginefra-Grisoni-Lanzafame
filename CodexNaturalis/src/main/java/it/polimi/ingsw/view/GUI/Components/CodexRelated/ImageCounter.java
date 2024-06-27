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

/**
 * This class represents the GUI component to display the number of a specific collectable that the player has collected so far.
 */
public class ImageCounter {
    /** The image of the collectable. */
    private final ImageView image;
    /** The label of the counter. */
    private final Text counterLabel;
    /** The container of the image and the label. */
    private final StackPane container = new StackPane();
    /** The increase animation of the counter. */
    private final Timeline increaseAnimation = new Timeline();
    /** The decrease animation of the counter. */
    private final Timeline decreaseAnimation = new Timeline();
    /** The current counter. */
    private int counter = 0;

    /**
     * Creates a new ImageCounter.
     * @param image the image of the collectable.
     */
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
        labelBg.setStyle("-fx-fill: rgb(216, 196, 113);");


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

        counterLabel.setStyle("-fx-font-size: 20;");

        container.getChildren().addAll(this.image, labelBg, counterLabel);


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

    /**
     * Sets the counter of the collectable to the given value.
     * @param counter the new counter of the collectable.
     */
    public void setCounter(int counter) {
        counterLabel.setText(String.valueOf(counter));

        if(this.counter < counter){
            increaseAnimation.playFromStart();
        } else if(this.counter > counter){
            decreaseAnimation.playFromStart();
        }

        this.counter = counter;
    }

    /**
     * Returns the content of the counter.
     * @return the content of the counter.
     */
    public Node getContent() {
        return container;
    }

}
