package it.polimi.ingsw.lightModel.lightPlayerRelated;

import java.io.Serializable;
import java.util.Objects;

public record LightBack(int idBack) implements Serializable {
    public LightBack {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LightBack card = (LightBack) o;
        return idBack == card.idBack;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBack);
    }
}
