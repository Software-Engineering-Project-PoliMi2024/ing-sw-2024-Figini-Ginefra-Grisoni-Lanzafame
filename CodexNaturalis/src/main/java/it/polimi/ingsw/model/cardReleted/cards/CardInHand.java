package it.polimi.ingsw.model.cardReleted.cards;

import it.polimi.ingsw.model.cardReleted.utilityEnums.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class CardInHand extends CardWithCorners {
    private final Resource permanentResource;

    /**
     * this is the constructor of the class
     * @param permanentResource the permanent resource of the card
     * @param points the points of the card
     * @param frontCorners the corners of the front of the card
     */
    public CardInHand(int id, Resource permanentResource, int points,  Map<CardCorner, Collectable> frontCorners){
        super(id, points, frontCorners);
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
        if (!this.isCorner(corner, face))
            return null;

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
     * @param face For the card In hand this parameter is irrelevant
     * @return A set with the permanent resource of the card
     */
    @Override
    public Set<Resource> getPermanentResources(CardFace face){
        if ( face == null)
            throw new IllegalArgumentException("face can't be null");
        return new HashSet<>(Set.of(permanentResource));
    }

    /**
     * checks if the card can be placed
     * @return true if the card can be placed, false otherwise
     */
    public boolean canBePlaced(){
        return true;
    }
}
