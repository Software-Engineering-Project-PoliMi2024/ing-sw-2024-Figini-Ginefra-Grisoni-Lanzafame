package it.polimi.ingsw.lightModel.diffs.game.handDiffOther;

import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;

/**
 * This abstract class represents an update for the lightGame that updates the lightHand of another player
 */
public abstract class HandOtherDiff extends GameDiff {
    /**The LightBack of the card to which the update is about*/
    protected final LightBack card;
    /**The owner of the card that is being updated*/
    protected final String owner;

    /**
     * Creates a diff to update the lightGame with the card of another player
     * @param card the backCard of the other player
     * @param owner the owner of the card
     */
    protected HandOtherDiff(LightBack card, String owner){
        this.card = card;
        this.owner = owner;
    }
}
