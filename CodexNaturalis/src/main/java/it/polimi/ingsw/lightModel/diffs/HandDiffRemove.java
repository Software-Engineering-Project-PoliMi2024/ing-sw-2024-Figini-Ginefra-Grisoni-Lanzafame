package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightHand;

public class HandDiffRemove extends HandDiff{
    /**
     * @param card the LightCard to which the diff applies
     */
    public HandDiffRemove(LightCard card){
        super(card);
    }
    /**
     * @param hand the LightHand to which the diff applies
     */
    @Override
    public void apply(LightHand hand) {
        hand.removeCard(card);
    }
}
