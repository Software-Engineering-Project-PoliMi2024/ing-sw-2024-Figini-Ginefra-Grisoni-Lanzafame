package it.polimi.ingsw.lightModel.lightPlayerRelated;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Position;

public record LightPlacement(Position position, LightCard card, CardFace face) implements Differentiable {
    /**
     * Represents a placement in the light model
     * @param position the position of the placement
     * @param card the lightCard of the placement
     * @param face the face of the card
     */
    public LightPlacement{
    }

    public LightPlacement(LightPlacement other){
        this(other.position(), other.card(), other.face());
    }
}
