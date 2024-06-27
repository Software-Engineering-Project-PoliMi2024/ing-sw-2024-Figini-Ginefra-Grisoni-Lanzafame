package it.polimi.ingsw.lightModel.diffs.game.handDiffs;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

/**
 * This class represents an update for the lightGame that adds a card to the lightHand of the user
 */
public class HandDiffAdd extends HandDiff {
    /**The playability of the card. True if the card is playable, false otherwise*/
    private final boolean playability;
    /**
     * Constructor
     * @param card the LightCard to which the diff applies
     * @param playbility the playability of the card
     */
    public HandDiffAdd(LightCard card, boolean playbility) {
        super(card);
        this.playability = playbility;
    }

    /**
     * Add the card to the lightHand of the user
     * @param game to which the diff must be applied
     */
    public void apply(LightGame game) {
        game.addCard(super.card, this.playability);
    }
}
