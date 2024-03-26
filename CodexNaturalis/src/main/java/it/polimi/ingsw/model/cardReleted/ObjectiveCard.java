package it.polimi.ingsw.model.cardReleted;

import it.polimi.ingsw.model.playerReleted.Codex;

public class ObjectiveCard extends Card{
    private final ObjectiveCardPointMultiplier multiplier;

    /** @param points given by the card
     * @param multiplier the multiplier of the points */
    public ObjectiveCard(int points, ObjectiveCardPointMultiplier multiplier){
        super(points);
        this.multiplier = multiplier;
    }

    /** @return the points of the card multiplied by the multiplier
     * @param codex the codex from which calc the points */
    @Override
    public int getPoints(Codex codex){
        return multiplier.getMultiplier(codex) * super.getPoints();
    }
}
