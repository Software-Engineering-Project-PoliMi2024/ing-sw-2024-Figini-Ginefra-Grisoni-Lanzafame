package it.polimi.ingsw.model.cardReleted.cards;

import it.polimi.ingsw.model.cardReleted.pointMultiplyer.ObjectiveCardPointMultiplier;
import it.polimi.ingsw.model.playerReleted.Codex;

public class ObjectiveCard extends Card {
    private final ObjectiveCardPointMultiplier multiplier;

    /** @param points given by the card
     * @param multiplier the multiplier of the points */
    public ObjectiveCard(int id, int points, ObjectiveCardPointMultiplier multiplier){
        super(id, points);
        this.multiplier = multiplier;
    }

    /** @param other the card to copy */
    public ObjectiveCard(ObjectiveCard other){
        this(other.getId(), other.getPoints(), other.getMultiplier());
    }

    /** @return the points of the card multiplied by the multiplier
     * @param codex the codex from which calc the points */
    @Override
    public int getPoints(Codex codex){
        return multiplier.getMultiplier(codex) * super.getPoints();
    }

    /** @return the multiplier of the card */
    public ObjectiveCardPointMultiplier getMultiplier(){
        return multiplier.getCopy();
    }
}
