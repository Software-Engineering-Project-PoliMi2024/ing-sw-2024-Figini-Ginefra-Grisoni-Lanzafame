package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.view.GUI.Components.Utils.AnimationStuff;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class AnchoredPopUp {
    private final AnchorPane content = new AnchorPane();
    private final Timeline openAnimation = new Timeline();
    private final Timeline closeAnimation = new Timeline();
    private boolean isOpen = false;
    private boolean isLocked = false;

    private final int animationDuration = 200;
    private final Pos alignment;

    /**
     * Creates a new anchored popup
     * @param parent the parent of the popup
     * @param widthPercentage the width of the popup as a percentage of the parent's width
     * @param heightPercentage the height of the popup as a percentage of the parent's height
     * @param alignment the alignment of the popup. The available options are Pos.TOP_CENTER, Pos.BOTTOM_CENTER, Pos.CENTER_RIGHT, Pos.CENTER_LEFT
     * @param closedPercentage the percentage of the respective side that the popup will be once closed
     */
    public AnchoredPopUp(AnchorPane parent, float widthPercentage, float heightPercentage, Pos alignment, float closedPercentage) {
        final float backedPercentage = 1 - closedPercentage;
        this.alignment = alignment;

        //align the content using the alignment parameter
        switch (alignment) {
            case TOP_CENTER:
                content.setLayoutX(0);
                AnchorPane.setTopAnchor(content, 0.0);
                break;
            case BOTTOM_CENTER:
                content.setLayoutX(0);
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

        // add listeners to parent's width and height properties to update the content's width and height
        parent.widthProperty().addListener((obs, oldVal, newVal) -> {
            content.setPrefWidth(newVal.doubleValue() * widthPercentage);

            switch (alignment) {
                case Pos.CENTER_RIGHT:
                    closeAnimation.getKeyFrames().set(0,
                            new KeyFrame(Duration.millis(animationDuration), AnimationStuff.createTranslateXKeyValue(content, content.getPrefWidth() * backedPercentage)));

                    if(isOpen)
                        content.setTranslateX(0);
                    else
                        content.setTranslateX(content.getPrefWidth() * backedPercentage);
                    break;
                case Pos.CENTER_LEFT:
                    closeAnimation.getKeyFrames().set(0,
                            new KeyFrame(Duration.millis(animationDuration), AnimationStuff.createTranslateXKeyValue(content, -content.getPrefWidth() * backedPercentage)));

                    if(isOpen)
                        content.setTranslateX(0);
                    else
                        content.setTranslateX(-content.getPrefWidth() * backedPercentage);
                    break;
                case TOP_CENTER:
                case BOTTOM_CENTER:
                    content.setLayoutX((newVal.doubleValue() - content.getPrefWidth()) / 2);
                    break;
            }
        });

        parent.heightProperty().addListener((obs, oldVal, newVal) -> {
            double newHeight = newVal.doubleValue() * heightPercentage;
            content.setPrefHeight(newHeight);

            switch (alignment) {
                case TOP_CENTER:
                    closeAnimation.getKeyFrames().set(0,
                            new KeyFrame(Duration.millis(animationDuration), AnimationStuff.createTranslateYKeyValue(content, -newHeight * backedPercentage)));

                    if(isOpen)
                        content.setTranslateY(0);
                    else
                        content.setTranslateY(-newHeight * backedPercentage);

                    break;
                case BOTTOM_CENTER:
                    closeAnimation.getKeyFrames().set(0,
                            new KeyFrame(Duration.millis(animationDuration), AnimationStuff.createTranslateYKeyValue(content, newHeight * backedPercentage)));

                    if(isOpen)
                        content.setTranslateY(0);
                    else
                        content.setTranslateY(newHeight * backedPercentage);
                    break;

                case Pos.CENTER_RIGHT:
                case Pos.CENTER_LEFT:
                    content.setLayoutY((newVal.doubleValue() - content.getPrefHeight()) / 2);
                    break;
            }


        });

        //set the content background color and radius
        content.setStyle("-fx-background-color: black; -fx-background-radius: 0 0 20 20;");

        switch (alignment) {
            case BOTTOM_CENTER:
                content.setStyle(content.getStyle() + "-fx-background-radius: 20 20 0 0;");
                break;
            case TOP_CENTER:
                content.setStyle(content.getStyle() + "-fx-background-radius: 0 0 20 20;");
                break;
            case Pos.CENTER_RIGHT:
                content.setStyle(content.getStyle() + "-fx-background-radius: 20 0 0 20;");
                break;
            case Pos.CENTER_LEFT:
                content.setStyle(content.getStyle() + "-fx-background-radius: 0 20 20 0;");
                break;
        }

        //Set up the animations
        openAnimation.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(animationDuration), AnimationStuff.createTranslateXKeyValue(content, 0)),
                new KeyFrame(Duration.millis(animationDuration), AnimationStuff.createTranslateYKeyValue(content, 0))
        );

        closeAnimation.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(animationDuration), AnimationStuff.createTranslateXKeyValue(content, 0))
        );


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
