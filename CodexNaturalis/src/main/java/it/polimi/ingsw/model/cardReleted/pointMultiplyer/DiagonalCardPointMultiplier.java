package it.polimi.ingsw.model.cardReleted.pointMultiplyer;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.playerReleted.Codex;
import it.polimi.ingsw.model.playerReleted.Placement;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.ArrayList;

public class DiagonalCardPointMultiplier implements ObjectiveCardPointMultiplier {
    final private boolean upwards;
    final private Resource color;

    /**@param upwards if is upward == True,
     * if is downwards upward == False*/
    public DiagonalCardPointMultiplier(boolean upwards, Resource color){
        this.upwards = upwards;
        this.color = color;
    }

    /**@return the multiplier of the points
     * @param codex the codex from which to calc the multiplier factor*/
    public int getMultiplier(Codex codex){
        int multiplier = 0;
        ArrayList <Position> positions = new ArrayList<>();
        if(upwards) {
            positions.add(CardCorner.TR.getOffset().add(CardCorner.TR.getOffset()));
            positions.add(CardCorner.BL.getOffset().add(CardCorner.BL.getOffset()));
        }else{
            positions.add(CardCorner.TL.getOffset().add(CardCorner.TL.getOffset()));
            positions.add(CardCorner.BR.getOffset().add(CardCorner.BR.getOffset()));
        }

        for(Placement placement : codex.getPlacementHistory()){
            if(placement.card().getPermanentResources(CardFace.BACK).contains(color) &&
                    codex.getPlacementAt(positions.getFirst())!= null &&
                    codex.getPlacementAt(positions.getFirst()).card().getPermanentResources(CardFace.BACK).contains(color) &&
                    codex.getPlacementAt(positions.getLast())!= null &&
                    codex.getPlacementAt(positions.getLast()).card().getPermanentResources(CardFace.BACK).contains(color))

                multiplier++;
        }
        return multiplier;
    }
}
