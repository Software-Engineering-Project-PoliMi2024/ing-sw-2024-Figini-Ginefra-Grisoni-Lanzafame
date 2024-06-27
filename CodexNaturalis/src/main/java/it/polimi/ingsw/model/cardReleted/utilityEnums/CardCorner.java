package it.polimi.ingsw.model.cardReleted.utilityEnums;

import it.polimi.ingsw.model.playerReleted.Position;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Enumerates the four corners of a card.
 */
public enum CardCorner implements Serializable {

    BR(1, -1), // Bottom Right
    TR(1, 1), // Top Right
    TL(-1, 1), // Top Left
    BL(-1, -1); // Bottom Left
    /** The offset of the corner from the center of the card. */
    final private Position offset;

    /** Constructor for the CardCorner enum.
     * @param x The x offset of the corner from the center of the card.
     * @param y The y offset of the corner from the center of the card.
     */
    CardCorner(int x, int y){
        this.offset = new Position(x, y);
    }

    /** @return The offset of the corner from the center of the card.*/
    public Position getOffset(){
        return new Position(offset);
    }

    /** @return The opposite corner.*/
    public CardCorner getOpposite(){
        Position offset = this.getOffset().multiply(-1);
        return Arrays.stream(CardCorner.values()).filter(corner -> corner.getOffset().equals(offset)).findFirst().orElse(null);
    }

}

