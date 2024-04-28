package it.polimi.ingsw.view.TUI.cardDrawing;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.view.TUI.Renderables.drawables.Drawable;

import java.io.Serializable;

public class TextCard implements Serializable {
    private final Drawable front;
    private final Drawable back;

    public TextCard(Drawable front, Drawable back){
        this.front = front;
        this.back = back;
    }

    public Drawable get(CardFace face){
        if(face == null)
            throw new IllegalArgumentException("Card face cannot be null");
        if(face == CardFace.FRONT)
            return this.front;
        else
            return this.back;
    }
}
