package it.polimi.ingsw.lightModel.diffs.game.handDiffOther;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

/**
 * This class represents an update for the lightGame that adds a card to the lightHand of another player
 */

public class HandOtherDiffAdd extends HandOtherDiff{

    /**
     * Creates a diff to add a card to the lightGame of another player
     * @param card the backCard of the other player
     * @param owner the owner of the card that is being added
     */
    public HandOtherDiffAdd(LightBack card, String owner) {
        super(card, owner);
    }

    /**
     * Add the card to the lightHand of the other player
     * @param lightGame the lightGame to which the diff must be applied
     */
    @Override
    public void apply(LightGame lightGame) {
        lightGame.addOtherHand(owner, card);
    }
}
