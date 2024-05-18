package it.polimi.ingsw.view.GUI.Components.CardRelated;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.GUI.CardMuseumGUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.function.Consumer;
import java.util.function.Function;

public class CardGUI {
    private LightCard target;
    protected CardFace face;
    private final ImageView imageView = new ImageView();
    private Image image;

    private Consumer<MouseEvent> onTap = null;
    private Consumer<MouseEvent> onHold = null;

    private Consumer<MouseEvent> onHoldRelease = null;

    private Timeline holdTimer = null;

    private boolean holdDetected = false;


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

        imageView.setFitWidth(image.getWidth() * 0.3);
        imageView.setFitHeight(image.getHeight() * 0.3);

        holdTimer = new Timeline();
        //Make an empty keyframe at holdDuration
        holdTimer.getKeyFrames().add(new KeyFrame(Duration.millis(GUIConfigs.holdDuration)));

        //set hold detected to false
        holdTimer.setOnFinished(t -> {
            if(onHold != null && !holdDetected)
                onHold.accept(null);
            holdDetected = true;
        });

        imageView.setOnMousePressed(e -> {
            holdDetected = false;
            holdTimer.playFromStart();
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
        });

    }

    public void update(){
        if(target == null) {
            imageView.setImage(null);
            return;
        }

        if(face == CardFace.FRONT) {
            image = CardMuseumGUI.loadCardFront(target.id());
            imageView.setImage(image);
        }
        else {
            image = CardMuseumGUI.loadCardBack(target.id());
            imageView.setImage(image);
        }

    }

    public void setTarget(LightCard target) {
        this.target = target;
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
        imageView.setTranslateX(imageView.getTranslateX() * scale / imageView.getScaleX());
        imageView.setTranslateY(imageView.getTranslateY() * scale / imageView.getScaleY());

        imageView.setScaleX(scale);
        imageView.setScaleY(scale);

    }

    public CardFace getFace() {
        return face;
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
}
