package it.polimi.ingsw.model.cardReleted;
import java.util.Map;
import java.util.Set;

public abstract class CardWithCorners extends Card{
    protected final Map<CardCorner, Collectable> frontCorners;

    public CardWithCorners(){
        super(0);
        frontCorners = null;
    }

    public Collectable getCollectableAt(CardCorner corner, CardFace face){
        return null;
    }

    public boolean isCorner(CardCorner corner, CardFace face){
        return true;
    }

    public Set <Resource> getPermanentResources(CardFace face){
        return null;
    }
}
