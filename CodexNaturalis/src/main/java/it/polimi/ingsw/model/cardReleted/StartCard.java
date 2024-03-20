package it.polimi.ingsw.model.cardReleted;


import java.util.Map;
import java.util.Set;

public class StartCard extends CardWithCorners{
    final private Map<CardCorner,Collectable> backCorners;
    final private Set<Resource> permanentResources;

    public StartCard(){
        super();
        this.backCorners = null;
        this.permanentResources = null;
    }
    public Set<Resource> getPermanentResources(CardFace cardFace){
        return null;
    }
}
