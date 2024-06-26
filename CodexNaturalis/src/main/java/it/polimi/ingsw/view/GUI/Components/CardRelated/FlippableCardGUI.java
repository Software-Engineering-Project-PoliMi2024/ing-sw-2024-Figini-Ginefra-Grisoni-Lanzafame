package it.polimi.ingsw.view.GUI.Components.CardRelated;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;

import java.util.function.Consumer;

/**
 * This class represents a card that can be flipped by the player.
 */
public class FlippableCardGUI extends CardGUI{
    /**
     * creates a new FlippableCardGUI.
     * @param target the card that this GUI represents.
     */
    public FlippableCardGUI(LightCard target) {
        super(target, CardFace.FRONT);
        this.setOnTap(e -> this.flip());
    }

    /**
     * Sets the face of the card.
     * @param face the new face of the card.
     */
    public void setFace(CardFace face) {
        this.face = face;
        this.update();
    }

    /**
     * Flips the card.
     */
    public void flip(){
        if(face == CardFace.FRONT)
            face = CardFace.BACK;
        else
            face = CardFace.FRONT;
        this.update();
    }
}
