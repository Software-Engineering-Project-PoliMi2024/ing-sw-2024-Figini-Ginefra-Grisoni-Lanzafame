package it.polimi.ingsw.view.GUI.Components;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.view.GUI.CardMuseumGUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import it.polimi.ingsw.view.GUI.Root;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class logoSwapAnimation {
    private final ImageView logoCenter = new ImageView(CardMuseumGUI.logoCenter);
    private final ImageView logoCircle = new ImageView(CardMuseumGUI.logoCircle);
    private final ImageView logoBackground = new ImageView(CardMuseumGUI.logoBackground);
    private final StackPane logoStack = new StackPane(logoBackground, logoCircle, logoCenter);
    private final Timeline timeline = new Timeline();

    /**
     * Creates a KeyValue for the scale of the target
     * @param target ImageView to scale
     * @param value value to scale to
     * @return KeyValue for the scale of the target
     */
    private static KeyValue createScaleKeyValue(Node target, double value) {
        return new KeyValue(target.scaleXProperty(), value, Interpolator.EASE_BOTH);
    }

    private static KeyValue createOpacityeKeyValue(Node target, double value) {
        return new KeyValue(target.opacityProperty(), value, Interpolator.EASE_BOTH);
    }

    /**
     * Creates a cycle of scaling for the target. It will scale from valueStart to valueEnd in duration seconds, then wait middleRest seconds and scale back to valueStart in duration seconds
     * @param target ImageView to scale
     * @param valueStart starting value
     * @param valueEnd ending value
     * @param startTime time to start the animation
     * @param duration duration of the scaling
     * @param middleRest time to wait in the middle
     * @return List of KeyFrames for the scaling
     */
    private static List<KeyFrame> scaleCycle(Node target, double valueStart, double valueEnd, double startTime, double duration, double middleRest) {
        return List.of(
                new KeyFrame(Duration.millis(startTime), createScaleKeyValue(target, valueStart)),
                new KeyFrame(Duration.millis(startTime + duration), createScaleKeyValue(target, valueEnd)),
                new KeyFrame(Duration.millis(startTime + duration + middleRest), createScaleKeyValue(target, valueEnd)),
                new KeyFrame(Duration.millis(startTime + 2 * duration + middleRest), createScaleKeyValue(target, valueStart))
        );
    }

    private static List<KeyFrame> opacitySwitch(Node target, double valueStart, double valueEnd, double switchTime) {
        return List.of(
                new KeyFrame(Duration.millis(switchTime), createOpacityeKeyValue(target, valueStart)),
                new KeyFrame(Duration.millis(0), createOpacityeKeyValue(target, valueStart)),
                new KeyFrame(Duration.millis(switchTime), createOpacityeKeyValue(target, valueEnd))
        );
    }

    public logoSwapAnimation(Stage primaryStage) {
        // Make the stack always fill the screen
        AnchorPane.setTopAnchor(logoStack, 0.0);
        AnchorPane.setLeftAnchor(logoStack, 0.0);
        AnchorPane.setRightAnchor(logoStack, 0.0);
        AnchorPane.setBottomAnchor(logoStack, 0.0);

        logoStack.setPrefSize(100, 100);
        logoStack.setTranslateX(0);
        logoStack.setTranslateY(0);

        // Add update size to the listener of width, height and fullscreen properties
        // Now the logo will always be the biggest possible
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> updateLogoSize(newVal.doubleValue(), primaryStage.getHeight()));
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> updateLogoSize(primaryStage.getWidth(), newVal.doubleValue()));
        primaryStage.fullScreenProperty().addListener((obs, oldVal, newVal) -> updateLogoSize(primaryStage.getWidth(), primaryStage.getHeight()));

        //Link the scaleX and Y of the images
        logoCenter.scaleXProperty().bindBidirectional(logoCenter.scaleYProperty());
        logoCircle.scaleXProperty().bindBidirectional(logoCircle.scaleYProperty());
        logoBackground.scaleXProperty().bindBidirectional(logoBackground.scaleYProperty());

        logoCenter.setScaleX(0);
        logoCircle.setScaleX(0);
        logoBackground.setScaleX(0);

        this.animationSetup();
    }

    /**
     * Setups the animation for the logo. It deals with the scaling of the images
     */
    private void animationSetup(){
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(scaleCycle(logoCenter, 0, 1, 0, GUIConfigs.swapAnimationDuration, GUIConfigs.swapAnimationPause + 4 * GUIConfigs.swapAnimationDelay));
        timeline.getKeyFrames().addAll(scaleCycle(logoCircle, 0, 1, GUIConfigs.swapAnimationDelay, GUIConfigs.swapAnimationDuration, GUIConfigs.swapAnimationPause + 2 * GUIConfigs.swapAnimationDelay));
        timeline.getKeyFrames().addAll(scaleCycle(logoBackground, 0, 1, 2 * GUIConfigs.swapAnimationDelay, GUIConfigs.swapAnimationDuration, GUIConfigs.swapAnimationPause));
    }

    public void playTransitionAnimation(AnchorPane mainRoot, Node oldRoot, Node newRoot){
        // Add the old root to the main root
        mainRoot.getChildren().add(newRoot);

        // Add the logo to the main root
        mainRoot.getChildren().add(logoStack);

        this.animationSetup();

        this.timeline.getKeyFrames().addAll(opacitySwitch(oldRoot, 1, 0, GUIConfigs.swapAnimationDuration + 2 * GUIConfigs.swapAnimationDelay + (double) GUIConfigs.swapAnimationPause / 2));
        this.timeline.getKeyFrames().addAll(opacitySwitch(newRoot, 0, 1, GUIConfigs.swapAnimationDuration + 2 * GUIConfigs.swapAnimationDelay + (double) GUIConfigs.swapAnimationPause / 2));

        timeline.setOnFinished(e -> {
            mainRoot.getChildren().remove(logoStack);
            mainRoot.getChildren().remove(oldRoot);
        });

        timeline.play();
    }

    /**
     * Sets the size of the logo to the maximum between w and h
     * @param w width
     * @param h height
     */
    public void updateLogoSize(double w, double h) {
        double size = Math.max(w, h);
        logoCenter.setFitWidth(size);
        logoCenter.preserveRatioProperty().set(true);

        logoCircle.setFitWidth(size);
        logoCircle.preserveRatioProperty().set(true);

        logoBackground.setFitWidth(size);
        logoBackground.preserveRatioProperty().set(true);
    }
}
