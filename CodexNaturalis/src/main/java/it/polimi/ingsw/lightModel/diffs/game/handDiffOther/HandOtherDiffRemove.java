package it.polimi.ingsw.lightModel.diffs.game.handDiffOther;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

/**
 * This class represents  an update for the lightGame that removes a card from the lightHand of another player
 */
public class HandOtherDiffRemove extends HandOtherDiff{
    /**
     * Creates a diff to remove a card from the lightGame of another player
     * @param card the backCard of the other player
     * @param owner the owner of the card that is being removed
     */
    public HandOtherDiffRemove(LightBack card, String owner) {
        super(card, owner);
    }

    /**
     * Remove the card from the lightHand of the other player
     * @param lightGame the lightGame to which the diff must be applied
     */
    @Override
    public void apply(LightGame lightGame) {
        lightGame.getHandOthers().get(owner).removeCard(card);
    }
}
