package it.polimi.ingsw.lightModel.lightPlayerRelated;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents the front of a card in the light model. It contains the unique id of such card and the id of the back card.
 * @param idFront
 * @param idBack
 */
public record LightCard(int idFront, int idBack) implements Serializable {
    /**
     * The constructor of the class
     * @param idFront the unique id of the card
     * @param idBack the id of the card that identifies is back
     */
    public LightCard {
    }
    public LightCard(int idFront){
        this(idFront, 1);
    }

    /**
     * Two cards are equal if they have the same idFront and idBack.
     * @param o   the reference object with which to compare.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LightCard lightCard = (LightCard) o;
        return idFront == lightCard.idFront && idBack == lightCard.idBack;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFront, idBack);
    }
}
