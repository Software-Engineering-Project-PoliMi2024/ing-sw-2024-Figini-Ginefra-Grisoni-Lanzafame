package it.polimi.ingsw.model.cardReleted;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class CardInHand extends CardWithCorners{
    private final Resource permanentResource;

    /**
     * this is the constructor of the class
     * @param permanentResource the permanent resource of the card
     */
    public CardInHand(Resource permanentResource, int points,  Map<CardCorner, Collectable> frontCorners){
        super(points, frontCorners);
        this.permanentResource = permanentResource;
    }

    /**
     * gets the collectable from card the specified corner and face
     * @param corner is the one of the 4 corners of the card
     * @param face is one of side of the card either front or back
     * @return the collectable from card the specified corner and face
     */
    @Override
    public Collectable getCollectableAt(CardCorner corner, CardFace face){
        if (corner == null || face == null)
            throw new IllegalArgumentException("corner and face can't be null");
        if ( face == CardFace.BACK )
            return SpecialCollectable.EMPTY;
        if ( ! this.isCorner(corner, face))
            throw new IllegalArgumentException("the selected corner is not a corner");
        assert this.frontCorners != null;
        return this.frontCorners.get(corner);
    }

    /**
     * checks if the corner is usable for other card placement
     * @param corner is the one of the 4 corners of the card
     * @param face is one of side of the card either front or back
     * @return true or false if corner is usable ora not for card placements
     */
    @Override
    public boolean isCorner(CardCorner corner, CardFace face){
        if (corner == null || face == null)
            throw new IllegalArgumentException("corner and face can't be null");
        if (face == CardFace.BACK)
            return true;
        assert frontCorners != null;
        return frontCorners.containsKey(corner);
    }

    /**
     * gets the permanent resource from the selected face of the chosen card
     * @param face is one of side of the card either front or back
     * @return a set of permanent resources from the selected face of the chosen card if the selected face is front there aren't any permanent resources
     */
    @Override
    public Set<Resource> getPermanentResources(CardFace face){
        if ( face == null)
            throw new IllegalArgumentException("face can't be null");
        Set<Resource> temp= new HashSet<Resource>();
        if ( face == CardFace.FRONT)
           return temp;
        temp.add(this.permanentResource);
        return temp;
    }

    public boolean canBePlaced(){
        return true;
    }
}
