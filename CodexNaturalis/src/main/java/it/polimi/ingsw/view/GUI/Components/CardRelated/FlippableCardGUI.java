package it.polimi.ingsw.view.GUI.Components.CardRelated;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;

public class FlippableCardGUI extends CardGUI{
    public FlippableCardGUI(LightCard target) {
        super(target, CardFace.FRONT);
        this.getImageView().setOnMouseClicked(e -> this.switchSide());
    }

    public void setFace(CardFace face) {
        this.face = face;
        this.update();
    }

    public void switchSide(){
        if(face == CardFace.FRONT)
            face = CardFace.BACK;
        else
            face = CardFace.FRONT;
        this.update();
    }
}
