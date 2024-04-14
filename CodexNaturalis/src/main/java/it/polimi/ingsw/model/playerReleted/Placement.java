package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.cards.CardWithCorners;

import java.io.Serializable;

/**
 * This class represents a placement of a card on the board
 * It contains the card, the position, the card face and the consequences of the placement
 */
public record Placement(Position position, CardWithCorners card, CardFace face) implements Serializable{
    /**
     * The constructor of the class
     *
     * @param position     the position of the placement
     * @param card         the card to place
     * @param face         the face of the card
     */
    public Placement {
    }

}
