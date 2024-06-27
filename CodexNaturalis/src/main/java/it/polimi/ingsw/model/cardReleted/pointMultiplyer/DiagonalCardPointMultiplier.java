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
 * This class is a ObjectiveCardPointMultiplier given by an ObjectiveCard
 * that multiplies the points of a ObjectiveCard based on the number of cards of a certain color
 * disposed in a diagonal line
 */
public class DiagonalCardPointMultiplier implements ObjectiveCardPointMultiplier {
    /** define the orientation of the diagonal */
    final private boolean upwards;
    /** define the color of the cards to count */
    final private Resource color;

    /**@param upwards if is upward == True,
     * if is downwards upward == False*/
    public DiagonalCardPointMultiplier(boolean upwards, Resource color){
        this.upwards = upwards;
        this.color = color;
    }

    /**@param other the multiplier to copy*/
    public DiagonalCardPointMultiplier(DiagonalCardPointMultiplier other){
        this(other.upwards, other.color);
    }

    /**@return the multiplier of the points
     * @param codex the codex from which to calc the multiplier factor*/
    public int getMultiplier(Codex codex){
        int multiplier = 0;

        Set<Position> alreadySeen = new HashSet<>();

        Position offset = upwards ? CardCorner.TR.getOffset() : CardCorner.TL.getOffset();

        for(Placement p : codex.getPlacementHistory()){
            if(alreadySeen.contains(p.position()) || !codex.getPlacementAt(p.position()).card().getPermanentResources(CardFace.BACK).contains(color))
                continue;

            int counter = 0;
            Position current = p.position();

            while(codex.getPlacementAt(current) != null &&
                    codex.getPlacementAt(current).card().getPermanentResources(CardFace.BACK).contains(color)){
                counter++;
                current = current.add(offset);
                alreadySeen.add(current);
            }

            current = p.position();
            while(codex.getPlacementAt(current) != null &&
                    codex.getPlacementAt(current).card().getPermanentResources(CardFace.BACK).contains(color)){
                counter++;
                current = current.add(offset.multiply(-1));
                alreadySeen.add(current);
            }

            multiplier += Math.floorDiv(counter, 3);
        }
        return multiplier;
    }

    /** @return the resource of the cards the diagonal is made of*/
    public Resource getColor() {
        return color;
    }
    /** @return true if the diagonal is upwards, false if it is downwards*/
    public boolean isUpwards() {
        return upwards;
    }

    /** @return a copy of the object*/
    public ObjectiveCardPointMultiplier getCopy(){
        return new DiagonalCardPointMultiplier(this);
    }
}
