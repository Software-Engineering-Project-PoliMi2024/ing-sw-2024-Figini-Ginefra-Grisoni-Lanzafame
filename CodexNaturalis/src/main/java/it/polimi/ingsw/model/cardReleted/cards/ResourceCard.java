package it.polimi.ingsw.model.cardReleted.cards;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

import java.util.Map;

public class ResourceCard extends CardInHand {
    public ResourceCard(int id, Resource permanentResource, int points, Map<CardCorner, Collectable> frontCorners){
        super(id, permanentResource, points, frontCorners);
    }
}
