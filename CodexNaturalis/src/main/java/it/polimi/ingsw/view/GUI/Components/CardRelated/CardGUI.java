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

public class CardGUI {
    private LightCard target;
    protected CardFace face;
    private final ImageView imageView = new ImageView();

    private Consumer<MouseEvent> onTap = null;
    private Consumer<MouseEvent> onHold = null;

    private Consumer<MouseEvent> onHoldRelease = null;

    private Timeline holdTimer = null;

    private boolean holdDetected = false;

    private MouseEvent lastEvent = null;

    private final Timeline holdingAnimation = new Timeline();

    protected final Timeline addingAnimation = new Timeline();

    protected final Timeline removingAnimation = new Timeline();

    private double scale = 1;


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

    public CardGUI(CardGUI other){
        this(other.target, other.face);
    }

    public CardGUI(LightBack back){
        this(new LightCard(0, back.idBack()), CardFace.BACK);
    }

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

    public void playAddingAnimation(){
        addingAnimation.getKeyFrames().set(2,
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleXKeyValue(imageView, this.scale)));
        addingAnimation.getKeyFrames().set(3,
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleYKeyValue(imageView, this.scale)));
        addingAnimation.play();
    }

    public void setTarget(LightCard target) {
        this.target = target;
        this.update();
    }

    public void setTarget(LightBack back) {
        this.target = new LightCard(0, back.idBack());
        this.update();
    }

    public void runWithAddingAnimation(Runnable runnable){
        this.playAddingAnimation();
        runnable.run();
    }

    public void runWithRemovingAnimation(Runnable runnable){
        removingAnimation.play();
        removingAnimation.setOnFinished(e -> runnable.run());
    }

    public void runBetweenRemovingAndAddingAnimation(Runnable runnable){
        removingAnimation.play();
        removingAnimation.setOnFinished(e -> {
            runnable.run();
            this.playAddingAnimation();
        });
    }

    public void setPosition(double x, double y) {
        imageView.setX(x);
        imageView.setY(y);
    }

    public void setTranslation(double x, double y) {
        this.imageView.setTranslateX(x);
        this.imageView.setTranslateY(y);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public LightCard getTarget() {
        return target;
    }

    public void setScale(double scale){
        this.scale = scale;
        imageView.setScaleX(scale);
        imageView.setScaleY(scale);

        this.addingAnimation.getKeyFrames().set(2,
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleXKeyValue(imageView, this.scale)));

        this.addingAnimation.getKeyFrames().set(3,
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleYKeyValue(imageView, this.scale)));
    }

    public void setScale(){
        imageView.setScaleX(this.scale);
        imageView.setScaleY(this.scale);

        this.addingAnimation.getKeyFrames().set(2,
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleXKeyValue(imageView, this.scale)));

        this.addingAnimation.getKeyFrames().set(3,
                new KeyFrame(Duration.millis(GUIConfigs.cardAddRemAnimationDuration), AnimationStuff.createScaleYKeyValue(imageView, this.scale)));
    }

    public CardFace getFace() {
        return face;
    }
    public void setScaleAndUpdateTranslation(double scale, Point2D center){
        imageView.setTranslateX((imageView.getTranslateX() - center.getX()) * scale / imageView.getScaleX() + center.getX());
        imageView.setTranslateY((imageView.getTranslateY() - center.getY()) * scale / imageView.getScaleY() + center.getY());

        this.setScale(scale);
    }

    public void setOnTap(Consumer<MouseEvent> onTap) {
        this.onTap = onTap;
    }

    public void setOnHold(Consumer<MouseEvent> onHold) {
        this.onHold = onHold;
    }

    public void setOnHoldRelease(Consumer<MouseEvent> onHoldRelease) {
        this.onHoldRelease = onHoldRelease;
    }

    public void disable(){
        imageView.setDisable(true);

        //set the opacity to 0.5
        imageView.setOpacity(0.8);
    }

    public void enable(){
        imageView.setDisable(false);

        //set the opacity to 1
        imageView.setOpacity(1);
    }

    public void addThisTo(Pane parent){
        parent.getChildren().add(imageView);
        this.playAddingAnimation();
    }

    public void addThisTo(Pane parent, int index){
        parent.getChildren().add(index, imageView);
        this.playAddingAnimation();
    }

    public void addThisBy(Consumer<CardGUI> parentAdder){
        parentAdder.accept(this);
        this.playAddingAnimation();
    }

    public void removeThisFrom(Pane parent){
        this.removingAnimation.play();
        this.removingAnimation.setOnFinished(e -> {
            parent.getChildren().remove(imageView);
        });
    }

    public void removeThisFrom(Pane parent, int index){
        this.removingAnimation.play();
        this.removingAnimation.setOnFinished(e -> {
            parent.getChildren().remove(index);
        });
    }

    public void removeThisBy(Consumer<CardGUI> parentRemover){
        this.removingAnimation.play();
        this.removingAnimation.setOnFinished(e -> {
            parentRemover.accept(this);
        });
    }

    public boolean equals(CardGUI other){
        if (other == null)
            return false;

        return this.target.equals(other.target);
    }
}
