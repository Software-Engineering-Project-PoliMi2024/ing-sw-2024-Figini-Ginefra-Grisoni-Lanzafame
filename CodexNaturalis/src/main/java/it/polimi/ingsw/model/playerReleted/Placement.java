package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.cards.CardWithCorners;

import java.io.Serializable;

/**
 * This class represents a placement of a card on the board
 * It contains the card, the position, the card face and the consequences of the placement
 * @param position the position occupied by the card
 * @param card the card placed
 * @param face the face visible after the placement
 */
public record Placement(Position position, CardWithCorners card, CardFace face) implements Serializable{
    /**
     * Copy constructor
     * @param other the placement to copy
     */
    public Placement(Placement other){
        this(other.position(), other.card(), other.face());
    }

}
