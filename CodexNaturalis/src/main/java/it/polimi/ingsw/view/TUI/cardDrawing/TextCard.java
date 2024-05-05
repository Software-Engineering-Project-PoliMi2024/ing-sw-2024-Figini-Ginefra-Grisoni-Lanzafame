package it.polimi.ingsw.view.TUI.cardDrawing;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;

import java.io.Serializable;

/**
 * This class is just a container for the front and back of a rendered card. Those are stored as Drawables.
 */
public class TextCard implements Serializable {
    /** The front of the card. */
    private final Drawable front;

    /** The back of the card. */
    private final Drawable back;

    /**
     * Creates a new TextCard.
     * @param front The front of the card.
     * @param back The back of the card.
     */
    public TextCard(Drawable front, Drawable back){
        this.front = front;
        this.back = back;
    }

    /**
     * Gets the front or back of the card.
     * @param face The face of the card to get.
     * @return The front or back of the card.
     * @throws IllegalArgumentException If the face is null.
     */
    public Drawable get(CardFace face){
        if(face == null)
            throw new IllegalArgumentException("Card face cannot be null");
        if(face == CardFace.FRONT)
            return this.front;
        else
            return this.back;
    }
}
