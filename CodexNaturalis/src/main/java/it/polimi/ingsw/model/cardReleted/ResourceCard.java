package it.polimi.ingsw.model.cardReleted;

import java.util.Map;

public class ResourceCard extends CardInHand{
    public ResourceCard(Resource permanentResource, int points,  Map<CardCorner, Collectable> frontCorners){
        super(permanentResource, points, frontCorners);
    }
}
