package it.polimi.ingsw.lightModel.lightPlayerRelated;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents the back of a card in the light model. It contains the id back of such card.
 * Different lightCard (different ID) can have the same idBack
 * @param idBack the id of the back card
 */
public record LightBack(int idBack) implements Serializable {

    /**
     * Two back cards are equal if they have the same id.
     * @param o   the reference object with which to compare.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LightBack card = (LightBack) o;
        return idBack == card.idBack;
    }

    /**
     * Returns the hash code of the back card.
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(idBack);
    }
}
