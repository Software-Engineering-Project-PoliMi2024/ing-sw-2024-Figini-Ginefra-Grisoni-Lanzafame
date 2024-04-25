package it.polimi.ingsw.model.lightModel;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.playerReleted.Position;

public record LightPlacement(Position position, LightCard card, CardFace face) {
    public LightPlacement{
    }
}
