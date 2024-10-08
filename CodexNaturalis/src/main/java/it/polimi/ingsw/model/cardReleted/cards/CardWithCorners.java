package it.polimi.ingsw.model.cardReleted.cards;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.GoldCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class CardWithCorners extends Card {
    protected final Map<CardCorner, Collectable> frontCorners;

    /**
     * Constructor of the class
     * @param idFront the id of the front of the card
     * @param idBack the id of the back of the card
     * @param points the points given by the card
     * @param frontCorners what contains the front corners of the card
     */
    public CardWithCorners(int idFront, int idBack, int points, Map<CardCorner, Collectable> frontCorners){
        super(idFront, idBack, points);
        this.frontCorners = frontCorners;
    }

    /**@param corner the corner to investigate
     * @param face the face where there's the corner
     * @return Collectable at the given corner*/
    public Collectable getCollectableAt(CardCorner corner, CardFace face){
        return null;
    }

    /**@param corner the corner to investigate
     * @param face the face where there's the corner
     * @return true if there's a corner, false otherwise*/
    public boolean isCorner(CardCorner corner, CardFace face){
        return true;
    }

    /** @param face the face from which retrive the resources
     * @return the Set containing the permanent Resources */
    public Set <Resource> getPermanentResources(CardFace face){
        return null;
    }

    /**
     * @return the GoldCardPointMultiplier of the card, if it has one, null otherwise
     * */
    public GoldCardPointMultiplier getGoldCardPointMultiplier(){
        return null;
    }

    /** @return a copy of the frontCorners what contains the corners of the card*/
    public Map<CardCorner, Collectable> getFrontCorners(){
        return new HashMap<>(this.frontCorners);
    }
}
