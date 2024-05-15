package it.polimi.ingsw.view.GUI.Components.CardRelated;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.GUI.CardMuseumGUI;
import it.polimi.ingsw.view.GUI.GUIConfigs;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class CardGUI {
    private LightCard target;
    protected CardFace face;
    private final ImageView imageView = new ImageView();
    private Image image;

    public CardGUI(LightCard target, CardFace face) {
        Rectangle clip = new Rectangle(GUIConfigs.cardWidth, GUIConfigs.cardHeight);
        clip.setArcWidth(GUIConfigs.cardBorderRadius); // Set the horizontal radius of the arc
        clip.setArcHeight(GUIConfigs.cardBorderRadius); // Set the vertical radius of the arc

        imageView.setClip(clip);

        this.target = target;
        this.face = face;
        this.update();

        imageView.setFitWidth(image.getWidth() * 0.3);
        imageView.setFitHeight(image.getHeight() * 0.3);

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

}
