package it.polimi.ingsw.lightModel.diffs;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightHand;

public abstract class HandDiff implements ModelDiffs<LightHand>{
    protected final LightCard card;
    /**
     * @param card the LightCard to which the diff applies
     */
    protected HandDiff(LightCard card) {
        this.card = card;
    }
    /**
     * @param hand the LightHand to which the diff applies
     */
    abstract public void apply(LightHand hand);
}


