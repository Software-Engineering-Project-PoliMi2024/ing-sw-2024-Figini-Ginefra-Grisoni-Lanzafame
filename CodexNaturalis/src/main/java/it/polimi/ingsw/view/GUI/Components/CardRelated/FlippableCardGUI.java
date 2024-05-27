package it.polimi.ingsw.view.GUI.Components.CardRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;

import java.util.function.Consumer;

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

    public void addThisByFlippable(Consumer<FlippableCardGUI> parentAdder){
        parentAdder.accept(this);
        this.playAddingAnimation();
    }

    public void removeThisByFlippable(Consumer<FlippableCardGUI> parentRemover){
        this.removingAnimation.setOnFinished(e -> parentRemover.accept(this));
        this.removingAnimation.play();
    }
}
