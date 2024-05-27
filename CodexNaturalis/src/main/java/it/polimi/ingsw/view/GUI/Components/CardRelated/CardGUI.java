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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

            if(onHold != null)
                holdingAnimation.stop();

            imageView.setScaleX(1);
            imageView.setScaleY(1);
        });

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

    public void setTarget(LightCard target) {
        this.target = target;
        this.update();
    }

    public void setTarget(LightBack back) {
        this.target = new LightCard(0, back.idBack());
        this.update();
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
        imageView.setScaleX(scale);
        imageView.setScaleY(scale);
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
        imageView.setOpacity(0.5);
    }

    public void enable(){
        imageView.setDisable(false);

        //set the opacity to 1
        imageView.setOpacity(1);
    }
}
