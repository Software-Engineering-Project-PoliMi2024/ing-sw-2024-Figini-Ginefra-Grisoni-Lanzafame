package it.polimi.ingsw.model.cardReleted.cards;

import it.polimi.ingsw.model.cardReleted.pointMultiplyer.ObjectiveCardPointMultiplier;
import it.polimi.ingsw.model.playerReleted.Codex;

/** this class represents an objective card */
public class ObjectiveCard extends Card {
    /** the multiplier of the points, how many times the points are multiplied depending on the codex */
    private final ObjectiveCardPointMultiplier multiplier;

    /** @param points given by the card
     * @param multiplier the multiplier of the points */
    public ObjectiveCard(int idFront, int idBack, int points, ObjectiveCardPointMultiplier multiplier){
        super(idFront, idBack, points);
        this.multiplier = multiplier;
    }

    /** @param other the card to copy */
    public ObjectiveCard(ObjectiveCard other){
        this(other.getIdFront(), other.getIdBack(), other.getPoints(), other.getMultiplier());
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
