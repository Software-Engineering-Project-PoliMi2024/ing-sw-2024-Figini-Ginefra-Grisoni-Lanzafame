package it.polimi.ingsw.view.GUI.Components.CardRelated;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.GUI.CardMuseumGUI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardGUI {
    private LightCard target;
    protected CardFace face;
    private final ImageView imageView = new ImageView();
    private Image image;

    public CardGUI(LightCard target, CardFace face) {
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

    public ImageView getImageView() {
        return imageView;
    }

    public LightCard getTarget() {
        return target;
    }

}
