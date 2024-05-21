package it.polimi.ingsw.model.cardReleted.cards;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

import java.util.Map;

public class ResourceCard extends CardInHand {
    public ResourceCard(int idFront, int idBack, Resource permanentResource, int points, Map<CardCorner, Collectable> frontCorners){
        super(idFront, idBack, permanentResource, points, frontCorners);
    }

    public ResourceCard(ResourceCard other){
        this(other.getIdFront(), other.getIdBack(), other.getPermanentResources(CardFace.BACK).stream().findFirst().orElse(null), other.getPoints(), other.getFrontCorners());
    }

    public CardInHand copy(){
        return new ResourceCard(this);
    }
}
