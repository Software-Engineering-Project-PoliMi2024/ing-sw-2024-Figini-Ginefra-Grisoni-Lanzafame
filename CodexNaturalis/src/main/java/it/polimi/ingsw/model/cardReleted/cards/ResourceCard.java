package it.polimi.ingsw.model.cardReleted.cards;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

import java.util.Map;

/** This class represents a resource card. */
public class ResourceCard extends CardInHand {
    /**
     * Constructor for the class.
     * @param idFront the id of the front face of the card
     * @param idBack the id of the back face of the card
     * @param permanentResource the permanent resource of the card
     * @param points the points given by the card
     * @param frontCorners what each front corner of the card contains
     */
    public ResourceCard(int idFront, int idBack, Resource permanentResource, int points, Map<CardCorner, Collectable> frontCorners){
        super(idFront, idBack, permanentResource, points, frontCorners);
    }

    /**
     * Copy constructor.
     * @param other the card to copy
     */
    public ResourceCard(ResourceCard other){
        this(other.getIdFront(), other.getIdBack(), other.getPermanentResources(CardFace.BACK).stream().findFirst().orElse(null), other.getPoints(), other.getFrontCorners());
    }

    /**
     * Copy method.
     * @return a copy of the card
     */
    public CardInHand copy(){
        return new ResourceCard(this);
    }
}
