package it.polimi.ingsw.model.cardReleted;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StartCard extends CardWithCorners{
    final private Map<CardCorner,Collectable> backCorners;
    final private HashSet<Resource> permanentResources;

    /** @param backCorners the map containing which collectable is in which corner
     * @param permanentResources the permanent resources given by the start card
     * @param frontCorners the front corners map*/
    public StartCard(Map<CardCorner, Collectable> frontCorners, Map<CardCorner,Collectable> backCorners, HashSet<Resource> permanentResources){
        super(0, frontCorners);
        this.backCorners = backCorners;
        this.permanentResources = permanentResources;
    }

    /**@param corner the corner to check
     * @param face the face from which  retrieve the corner
     * @return the Collectable at given position
     * @throws IllegalArgumentException if there is no corner*/
    @Override
    public Collectable getCollectableAt(CardCorner corner, CardFace face){
        if (face == CardFace.BACK){
            if(backCorners.containsKey(corner))
                return backCorners.get(corner);
            else
                throw new IllegalArgumentException("No corner");
        } else
            if(frontCorners.containsKey(corner))
                return frontCorners.get(corner);
            else
                throw new IllegalArgumentException("No corner");
    }

    /**@param corner the corner to check
     * @param face the face from which retrieve the corner
     * @return true if the corner is present, false otherwise*/
    @Override
    public boolean isCorner(CardCorner corner, CardFace face){
        if (face == CardFace.BACK)
            return backCorners.containsKey(corner);
        else
            return frontCorners.containsKey(corner);
    }

    /** @param face the face from which retrieve the resources
     * @return the HashSet containing the permanent Resources */
    @Override
    public HashSet <Resource> getPermanentResources(CardFace face){
        if (face == CardFace.FRONT)
            return permanentResources;
        else
            return new HashSet<Resource>();
    }

}
