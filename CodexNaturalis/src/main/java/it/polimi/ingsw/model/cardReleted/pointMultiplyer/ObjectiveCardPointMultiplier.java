package it.polimi.ingsw.model.cardReleted.pointMultiplyer;

import it.polimi.ingsw.model.playerReleted.Codex;

import java.io.Serializable;

public interface ObjectiveCardPointMultiplier extends Serializable {
    /**
     * This method returns the multiplier of the objective card given the codex to evaluate
     * @param codex the codex to evaluate
     * @return the multiplier of the objective card points given the codex
     */
    int getMultiplier(Codex codex);

    /** @return a copy of the ObjectiveCardPointMultiplier */
    ObjectiveCardPointMultiplier getCopy();
}
