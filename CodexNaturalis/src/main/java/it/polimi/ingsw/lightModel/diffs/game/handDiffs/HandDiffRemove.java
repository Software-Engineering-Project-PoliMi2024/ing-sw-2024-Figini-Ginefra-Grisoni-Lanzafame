package it.polimi.ingsw.lightModel.diffs.game.handDiffs;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

/**
 * This class represents an update for the lightGame that removes a card from the lightHand of the user
 */
public class HandDiffRemove extends HandDiff{
    /**
     * Constructor
     * @param card the LightCard to which the diff applies
     */
    public HandDiffRemove(LightCard card){
        super(card);
    }

    /**
     * Remove the card from the lightHand of the user
     * @param game the LightHand to which the diff applies
     */
    @Override
    public void apply(LightGame game) {
        game.removeCard(card);
    }
}
