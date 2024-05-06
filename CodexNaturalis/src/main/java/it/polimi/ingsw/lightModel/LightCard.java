package it.polimi.ingsw.lightModel;

import java.io.Serializable;
import java.util.Objects;

public record LightCard(int id) implements Serializable {
    /**
     * The constructor of the class
     * @param id the id of the card
     */
    public LightCard {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LightCard lightCard = (LightCard) o;
        return id == lightCard.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
