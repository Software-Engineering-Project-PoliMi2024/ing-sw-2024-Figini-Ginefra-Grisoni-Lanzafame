package it.polimi.ingsw.view.GUI.Components.Utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * This class represents a popup that is anchored to a side of the parent.
 * The popup will open and close with a slide animation when the mouse enters and exits the content.
 */
public class AnchoredPopUp {
    /** The content of the popup */
    private final AnchorPane content = new AnchorPane();
    /** The slide animation used to open the Popup */
    private final Timeline openAnimation = new Timeline();
    /** The slide animation used to close the Popup */
    private final Timeline closeAnimation = new Timeline();
    /** Whether the popup is open */
    private boolean isOpen = false;
    /** Whether the popup is locked, meaning that it will not close when the mouse exits the content nor open when it enters */
    private boolean isLocked = false;

    /** The duration of the animation */
    private final int animationDuration = 200;

    /**
     * Creates a new anchored popup
     * @param parent the parent of the popup
     * @param widthPercentage the width of the popup as a percentage of the parent's width
     * @param heightPercentage the height of the popup as a percentage of the parent's height
     * @param alignment the alignment of the popup. The available options are Pos.TOP_CENTER, Pos.BOTTOM_CENTER, Pos.CENTER_RIGHT, Pos.CENTER_LEFT
     * @param closedPercentage the percentage of the respective side that the popup will be once closed
     * @param closedPercentage the percentage of the respective side that the popup will be once closed
     */
    public AnchoredPopUp(AnchorPane parent, float widthPercentage, float heightPercentage, Pos alignment, float closedPercentage) {

        //align the content using the alignment parameter
        switch (alignment) {
            case TOP_CENTER:
                //content.setLayoutX(0);
                AnchorPane.setTopAnchor(content, 0.0);
                break;
            case BOTTOM_CENTER:
                //content.setLayoutX(0);
                AnchorPane.setBottomAnchor(content, 0.0);
                break;
            case Pos.CENTER_RIGHT:
                content.setLayoutY(0);
                AnchorPane.setRightAnchor(content, 0.0);
                break;
            case Pos.CENTER_LEFT:
                content.setLayoutY(0);
                AnchorPane.setLeftAnchor(content, 0.0);
                break;
            default:
                throw new IllegalArgumentException("Invalid alignment");
        }

        content.maxWidthProperty().bind(parent.widthProperty().multiply(widthPercentage));
        content.maxHeightProperty().bind(parent.heightProperty().multiply(heightPercentage));

        //Set Translation Bindings for centering the content
        switch(alignment){
            case Pos.TOP_CENTER:
            case Pos.BOTTOM_CENTER:
                content.translateXProperty().bind(
                        Bindings.createDoubleBinding(
                                () -> (parent.getWidth() - content.getWidth()) / 2,
                                parent.widthProperty(),
                                content.widthProperty(),
                                content.maxWidthProperty()
                        )
                );
                break;

            case Pos.CENTER_RIGHT:
            case Pos.CENTER_LEFT:
                content.translateYProperty().bind(
                        Bindings.createDoubleBinding(
                                () -> (parent.getHeight() - content.getHeight()) / 2,
                                parent.heightProperty(),
                                content.heightProperty(),
                                content.maxHeightProperty()
                        )
                );
                break;
        }


        //set the content background color and radius
        content.getStyleClass().add("bordersCodexStyle");

        switch (alignment) {
            case BOTTOM_CENTER:
                content.setStyle(content.getStyle() + "-fx-background-radius: 20 20 0 0; -fx-border-radius: 20 20 0 0;");
                break;
            case TOP_CENTER:
                content.setStyle(content.getStyle() + "-fx-background-radius: 0 0 20 20; -fx-border-radius: 0 0 20 20;");
                break;
            case Pos.CENTER_RIGHT:
                content.setStyle(content.getStyle() + "-fx-background-radius: 20 0 0 20; -fx-border-radius: 20 0 0 20;");
                break;
            case Pos.CENTER_LEFT:
                content.setStyle(content.getStyle() + "-fx-background-radius: 0 20 20 0; -fx-border-radius: 0 20 20 0;");
                break;
        }

        //Set up the Open and Close animation
        switch (alignment){
            case Pos.TOP_CENTER:
            case BOTTOM_CENTER:
                openAnimation.getKeyFrames().addAll(
                        new KeyFrame(Duration.millis(animationDuration), AnimationStuff.createTranslateYKeyValue(content, 0))
                );

                closeAnimation.getKeyFrames().addAll(
                        new KeyFrame(Duration.millis(animationDuration), new KeyValue(content.translateYProperty(), 0)));
                break;

            case Pos.CENTER_RIGHT:
            case Pos.CENTER_LEFT:
                openAnimation.getKeyFrames().addAll(
                        new KeyFrame(Duration.millis(animationDuration), AnimationStuff.createTranslateXKeyValue(content, 0))
                );

                closeAnimation.getKeyFrames().addAll(
                        new KeyFrame(Duration.millis(animationDuration), new KeyValue(content.translateXProperty(), 0)));
                break;

        }

        //add a listener to the content height to update the close animation

        switch (alignment){
            case Pos.TOP_CENTER:
                content.heightProperty().addListener((observable, oldValue, newValue) -> {
                    closeAnimation.getKeyFrames().set(0,
                            new KeyFrame(Duration.millis(animationDuration), AnimationStuff.createTranslateYKeyValue(content, -content.getHeight() * (1 - closedPercentage))));
                    if(!isOpen)
                        content.setTranslateY(-content.getHeight() * (1 - closedPercentage));
                });
                break;
            case BOTTOM_CENTER:
                content.heightProperty().addListener((observable, oldValue, newValue) -> {
                    closeAnimation.getKeyFrames().set(0,
                            new KeyFrame(Duration.millis(animationDuration), AnimationStuff.createTranslateYKeyValue(content, content.getHeight() * (1 - closedPercentage))));
                    if(!isOpen)
                        content.setTranslateY(content.getHeight() * (1 - closedPercentage));
                });
                break;
            case Pos.CENTER_RIGHT:
                content.widthProperty().addListener((observable, oldValue, newValue) -> {
                    closeAnimation.getKeyFrames().set(0,
                            new KeyFrame(Duration.millis(animationDuration), AnimationStuff.createTranslateXKeyValue(content, content.getWidth() * (1 - closedPercentage))));
                    if(!isOpen)
                        content.setTranslateX(content.getWidth() * (1 - closedPercentage));
                });
                break;
            case Pos.CENTER_LEFT:
                content.widthProperty().addListener((observable, oldValue, newValue) -> {
                    closeAnimation.getKeyFrames().set(0,
                            new KeyFrame(Duration.millis(animationDuration), AnimationStuff.createTranslateXKeyValue(content, -content.getWidth() * (1 - closedPercentage))));
                    if(!isOpen)
                        content.setTranslateX(-content.getWidth() * (1 - closedPercentage));
                });
                break;

        }


        // add listeners to the content to open and close the popup
        content.setOnMouseEntered(e -> {
            if(isLocked)
                return;
            open();
        });

        content.setOnMouseExited(e -> {
            if(isLocked)
                return;
            close();
        });

        parent.getChildren().add(content);
    }

    /**
     * Opens the popup
     */
    public void open() {
        if (isOpen) {
            return;
        }
        isOpen = true;
        openAnimation.play();
    }

    /**
     * Closes the popup
     */
    public void close() {
        if (!isOpen) {
            return;
        }
        isOpen = false;
        closeAnimation.play();
    }

    /**
     * Locks the popup, meaning that it will not close when the mouse exits the content nor open when it enters
     * @param isLocked true to lock the popup, false to unlock it
     */
    public void setLocked(boolean isLocked){
        this.isLocked = isLocked;
    }

    /**
     * @return the reference to the content of the popup. Use this to add elements to the popup
     */
    public AnchorPane getContent() {
        return content;
    }
}
