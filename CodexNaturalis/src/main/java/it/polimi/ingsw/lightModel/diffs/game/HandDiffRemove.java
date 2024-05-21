package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public class HandDiffRemove extends HandDiff{
    /**
     * @param card the LightCard to which the diff applies
     */
    public HandDiffRemove(LightCard card){
        super(card);
    }
    /**
     * @param game the LightHand to which the diff applies
     */
    @Override
    public void apply(LightGame game) {
        game.removeCard(card);
    }
}
