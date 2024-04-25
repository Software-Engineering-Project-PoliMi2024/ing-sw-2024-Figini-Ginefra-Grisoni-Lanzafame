package it.polimi.ingsw.model.lightModel.diffs;

import it.polimi.ingsw.model.lightModel.LightCard;
import it.polimi.ingsw.model.lightModel.LightHand;

public class HandDiffRemove extends HandDiff{

    public HandDiffRemove(LightCard card){
        super(card);
    }
    @Override
    public void apply(LightHand hand) {
        hand.removeCard(card);
    }
}
