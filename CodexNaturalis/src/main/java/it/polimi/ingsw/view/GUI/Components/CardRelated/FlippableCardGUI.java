package it.polimi.ingsw.view.GUI.Components.CardRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;

public class FlippableCardGUI extends CardGUI{
    public FlippableCardGUI(LightCard target) {
        super(target, CardFace.FRONT);
        this.setOnTap(e -> this.flip());
    }

    public void setFace(CardFace face) {
        this.face = face;
        this.update();
    }

    public void flip(){
        if(face == CardFace.FRONT)
            face = CardFace.BACK;
        else
            face = CardFace.FRONT;
        this.update();
    }
}
