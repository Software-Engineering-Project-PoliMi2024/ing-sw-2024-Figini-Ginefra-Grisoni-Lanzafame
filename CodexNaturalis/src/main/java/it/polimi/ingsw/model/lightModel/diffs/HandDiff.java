package it.polimi.ingsw.model.lightModel.diffs;
import it.polimi.ingsw.model.lightModel.LightCard;
import it.polimi.ingsw.model.lightModel.LightHand;

public abstract class HandDiff implements ModelDiffs<LightHand>{
    protected final LightCard card;
    protected HandDiff(LightCard card) {
        this.card = card;
    }

    abstract public void apply(LightHand hand);
}


