package it.polimi.ingsw.model.cardReleted.pointMultiplyer;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is a multiplier for the points of the cards that are placed in an L shape.
 */
public class LCardPointMultiplier implements ObjectiveCardPointMultiplier {
    /** the orientation of the L determined by the position of the corner*/
    final private CardCorner corner;
    /** the color of the single card*/
    final private Resource singleResource;
    /*+ the color of the two cards*/
    final private Resource doubleResource;

    /**@param corner the corner of the L
     * @param singleResource the color of the single card
     * @param doubleResource the color of the two cards*/
    public LCardPointMultiplier(CardCorner corner, Resource singleResource,
                                Resource doubleResource){
        this.corner = corner;
        this.singleResource = singleResource;
        this.doubleResource = doubleResource;
    }

    /**@param other the multiplier to copy*/
    public LCardPointMultiplier(LCardPointMultiplier other){
        this(other.corner, other.singleResource, other.doubleResource);
    }

    /**@param codex the codex to calc the multiplier
     * @return the multiplier of the points*/
    @Override
    public int getMultiplier(Codex codex) {
        int multiplier = 0;
        Set<Position> alreadySeen = new HashSet<>();
        for(Placement p : codex.getPlacementHistory()){
            if(p.card().getPermanentResources(CardFace.BACK).contains(singleResource) &&
                    codex.getPlacementAt(p.position()
                            .add(corner.getOffset()))!= null &&
                    !alreadySeen.contains(p.position().add(corner.getOffset())) &&
                    codex.getPlacementAt(p.position()
                            .add(corner.getOffset())).card()
                            .getPermanentResources(CardFace.BACK).contains(doubleResource) &&
                    codex.getPlacementAt(p.position()
                            .add(corner.getOffset())
                            .add(0, 2*corner.getOffset().getY()))!= null &&
                    !alreadySeen.contains(p.position().add(corner.getOffset()).add(0, 2*corner.getOffset().getY())) &&
                    codex.getPlacementAt(p.position()
                            .add(corner.getOffset())
                            .add(0, 2*corner.getOffset().getY())).card()
                            .getPermanentResources(CardFace.BACK).contains(doubleResource)
            ){
                multiplier++;
                alreadySeen.add(p.position().add(corner.getOffset()));
                alreadySeen.add(p.position().add(corner.getOffset().add(0, 2*corner.getOffset().getY())));
            }
        }
        return multiplier;
    }

    /** @return the position of corner of the L*/
    public CardCorner corner() {
        return corner;
    }

    /** @return the color of the single card*/
    public Resource singleResource() {
        return singleResource;
    }

    /** @return the color of the two cards*/
    public  Resource doubleResource(){
        return doubleResource;
    }

    /** @return a copy of the multiplier*/
    public ObjectiveCardPointMultiplier getCopy(){
        return new LCardPointMultiplier(this);
    }
}
