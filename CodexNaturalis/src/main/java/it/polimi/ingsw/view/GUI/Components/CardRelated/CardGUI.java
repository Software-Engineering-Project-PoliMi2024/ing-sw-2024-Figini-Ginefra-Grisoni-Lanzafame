package it.polimi.ingsw.view.GUI.Components.CardRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.GUI.AssetsGUI;
import it.polimi.ingsw.view.GUI.Components.Utils.AnimationStuff;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.function.Consumer;

/**
 * This class represents the GUI version of a card.
 */
public class CardGUI {
    /** The target card */
    private LightCard target;

    /** The face of the card */
    protected CardFace face;

    /** The image view of the card */
    private final ImageView imageView = new ImageView();

    /** The consumer of the tap event */
    private Consumer<MouseEvent> onTap = null;

    /** The consumer triggered when the hold is detected */
    private Consumer<MouseEvent> onHold = null;

    /** The consumer triggered when the hold is released */
    private Consumer<MouseEvent> onHoldRelease = null;

    /** The hold timer */
    private Timeline holdTimer = null;

    /** The hold detected flag */
    private boolean holdDetected = false;

    /** The last event. Useful to detect the hold */
    private MouseEvent lastEvent = null;

    /** The animation played between the mouse click and the hold detection */
    private final Timeline holdingAnimation = new Timeline();

    /** The animation placed when the card is added to a container */
    protected final Timeline addingAnimation = new Timeline();

    /** The animation placed when the card is removed from a container */
    protected final Timeline removingAnimation = new Timeline();

    /** The scale of the card */
    private double scale = 1;

    /**
     * Creates a new CardGUI. It setups the image view, the clip, the hold timer, the functions to interact with the user, the holding animation, the adding animation and the removing animation.
     * @param target the target card
     * @param face the face of the card
     */
    public CardGUI(LightCard target, CardFace face) {
        Rectangle clip = new Rectangle(GUIConfigs.cardWidth, GUIConfigs.cardHeight);
        clip.setArcWidth(GUIConfigs.cardBorderRadius); // Set the horizontal radius of the arc
        clip.setArcHeight(GUIConfigs.cardBorderRadius); // Set the vertical radius of the arc

        clip.widthProperty().bind(imageView.fitWidthProperty());
        clip.heightProperty().bind(imageView.fitHeightProperty());
        clip.arcWidthProperty().bindBidirectional(clip.arcHeightProperty());
        clip.arcWidthProperty().bind(imageView.fitWidthProperty().multiply(GUIConfigs.cardBorderRadius / GUIConfigs.cardWidth));

        imageView.setClip(clip);

        this.target = target;
        this.face = face;
        this.update();

        imageView.setFitWidth(GUIConfigs.cardWidth);
        imageView.fitHeightProperty().bind(imageView.fitWidthProperty().multiply(GUIConfigs.cardHeight / GUIConfigs.cardWidth));

        holdTimer = new Timeline();
        //Make an empty keyframe at holdDuration
        holdTimer.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(GUIConfigs.holdDuration))
        );

        //set hold detected to false
        holdTimer.setOnFinished(t -> {
            if(onHold != null && !holdDetected)
                onHold.accept(lastEvent);
            holdDetected = true;
        });

        //Set up the holding animation
        holdingAnimation.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), AnimationStuff.createScaleXKeyValue(imageView, 1)),
                new KeyFrame(Duration.millis(0), AnimationStuff.createScaleYKeyValue(imageView, 1)),
                new KeyFrame(Duration.millis(GUIConfigs.holdDuration), AnimationStuff.createScaleXKeyValue(imageView, 0.8)),
                new KeyFrame(Duration.millis(GUIConfigs.holdDuration), AnimationStuff.createScaleYKeyValue(imageView, 0.8))
        );

        imageView.setOnMousePressed(e -> {
            holdDetected = false;
            holdTimer.playFromStart();

            if(onHold != null) {
                holdingAnimation.play();
            }

            lastEvent = e;
        });

        imageView.setOnMouseReleased(e -> {
            if(holdDetected) {
                if(onHoldRelease != null) {
                    onHoldRelease.accept(e);
                }
            } else if (onTap != null) {
                onTap.accept(e);
            }
            holdTimer.stop();
            holdDetected = false;

            if(onHold != null) {
                holdingAnimation.stop();
                this.setScale();
            }
        });


        //Set up the adding animation
        addingAnimation.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(0), AnimationStuff.createScaleXKeyValue(imageView, 0)),
                new KeyFrame(Duration.millis(0), AnimationStuff.createScaleYKeyValue(imageView, 0)),
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleXKeyValue(imageView, 1)),
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleYKeyValue(imageView, 1))
        );

        //Set up the removing animation
        removingAnimation.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleXKeyValue(imageView, 0)),
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleYKeyValue(imageView, 0))
        );

    }

    /**
     * Creates a new CardGUI by copying another CardGUI.
     * @param other the other CardGUI
     */
    public CardGUI(CardGUI other){
        this(other.target, other.face);
        scale = other.scale;
    }

    /**
     * Creates a new CardGUI by only setting the back of the card.
     * @param back the back of the card
     */
    public CardGUI(LightBack back){
        this(new LightCard(0, back.idBack()), CardFace.BACK);
    }

    /**
     * Updates the image view of the card based on the target and the face.
     */
    public void update(){
        if(target == null) {
            imageView.setImage(null);
            return;
        }

        if(face == CardFace.FRONT) {
            imageView.setImage(AssetsGUI.loadCardFront(target.idFront()));
        }
        else {
            imageView.setImage(AssetsGUI.loadCardBack(target.idBack()));
        }

    }

    /**
     * Plays the adding animation.
     */
    public void playAddingAnimation(){
        addingAnimation.getKeyFrames().set(2,
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleXKeyValue(imageView, this.scale)));
        addingAnimation.getKeyFrames().set(3,
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleYKeyValue(imageView, this.scale)));
        addingAnimation.play();
    }

    /**
     * Sets the target of the card and updates the image view accordingly.
     * @param target the target card
     */
    public void setTarget(LightCard target) {
        this.target = target;
        this.update();
    }

    /**
     * Sets the target of the card by only setting the back of the card.
     * @param back the back of the card
     */
    public void setTarget(LightBack back) {
        this.target = new LightCard(0, back.idBack());
        this.update();
    }

    /**
     * Runs the given runnable after the removing animation is played and then plays the adding animation.
     * @param runnable the runnable to run
     */
    public void runBetweenRemovingAndAddingAnimation(Runnable runnable){
        removingAnimation.play();
        removingAnimation.setOnFinished(e -> {
            runnable.run();
            this.playAddingAnimation();
        });
    }

    /**
     * Sets the translation of the card.
     * @param x the x translation
     * @param y the y translation
     */
    public void setTranslation(double x, double y) {
        this.imageView.setTranslateX(x);
        this.imageView.setTranslateY(y);
    }

    /**
     * Gets the image view of the card.
     * @return the image view of the card
     */
    public ImageView getImageView() {
        return imageView;
    }

    /**
     * Gets the target of the card.
     * @return the target of the card
     */
    public LightCard getTarget() {
        return target;
    }

    /**
     * Sets the scale of the card. It also updates the scale of the image view and the adding animation accordingly.
     * @param scale the scale of the card
     */
    public void setScale(double scale){
        this.scale = scale;
        imageView.setScaleX(scale);
        imageView.setScaleY(scale);

        this.addingAnimation.getKeyFrames().set(2,
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleXKeyValue(imageView, this.scale)));

        this.addingAnimation.getKeyFrames().set(3,
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleYKeyValue(imageView, this.scale)));
    }

    /**
     * Updates the scale of the image view and the adding animation according to the current scale.
     */
    public void setScale(){
        imageView.setScaleX(this.scale);
        imageView.setScaleY(this.scale);

        this.addingAnimation.getKeyFrames().set(2,
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleXKeyValue(imageView, this.scale)));

        this.addingAnimation.getKeyFrames().set(3,
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleYKeyValue(imageView, this.scale)));
    }

    /**
     * Gets the face of the card.
     * @return the face of the card
     */
    public CardFace getFace() {
        return face;
    }

    /**
     * Sets the scale of the card and updates the translation of the card accordingly.
     * @param scale the scale
     * @param center the center of the scaling
     */
    public void setScaleAndUpdateTranslation(double scale, Point2D center){
        imageView.setTranslateX((imageView.getTranslateX() - center.getX()) * scale / imageView.getScaleX() + center.getX());
        imageView.setTranslateY((imageView.getTranslateY() - center.getY()) * scale / imageView.getScaleY() + center.getY());

        this.setScale(scale);
    }

    /**
     * Set the consumer triggered when the tap is detected.
     * @param onTap the consumer of the tap event
     */
    public void setOnTap(Consumer<MouseEvent> onTap) {
        this.onTap = onTap;
    }

    /**
     * Set the consumer triggered when the hold is detected.
     * @param onHold the consumer of the hold event
     */
    public void setOnHold(Consumer<MouseEvent> onHold) {
        this.onHold = onHold;
    }

    /**
     * Set the consumer triggered when the hold is released.
     * @param onHoldRelease the consumer of the hold release event
     */
    public void setOnHoldRelease(Consumer<MouseEvent> onHoldRelease) {
        this.onHoldRelease = onHoldRelease;
    }

    /**
     * Disables the card. The card is not clickable and its opacity is set to 0.5.
     */
    public void disable(){
        imageView.setDisable(true);

        //set the opacity to 0.5
        imageView.setOpacity(0.8);
    }

    /**
     * Enables the card. The card is clickable and its opacity is set to 1.
     */
    public void enable(){
        imageView.setDisable(false);

        //set the opacity to 1
        imageView.setOpacity(1);
    }

    /**
     * Adds the card to the given parent and plays the adding animation.
     * @param parent the parent to add the card to
     */
    public void addThisTo(Pane parent){
        parent.getChildren().add(imageView);
        this.playAddingAnimation();
    }

    /**
     * Adds the card to the given parent at the given index and plays the adding animation.
     * @param parent the parent to add the card to
     * @param index the index to add the card at
     */
    public void addThisTo(Pane parent, int index){
        parent.getChildren().add(index, imageView);
        this.playAddingAnimation();
    }

    /**
     * Returns whether the card is equal to another card by comparing the target.
     * @param other the other card
     * @return whether the card is equal to the other card
     */
    public boolean equals(CardGUI other){
        if (other == null)
            return false;

        return this.target.equals(other.target);
    }
}
