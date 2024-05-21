package it.polimi.ingsw.lightModel.lightPlayerRelated;

import java.io.Serializable;
import java.util.Objects;

public record LightCard(int idFront, int idBack) implements Serializable {
    /**
     * The constructor of the class
     * @param idFront the id of the card
     * @param idBack the id of the card
     */
    public LightCard {
    }
    public LightCard(int idFront){
        this(idFront, 1);
    }

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
