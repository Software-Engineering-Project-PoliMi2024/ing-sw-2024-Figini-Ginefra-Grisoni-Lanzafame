package it.polimi.ingsw.lightModel.diffs.game.handDiffs;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

/**
 * This class represents an update for the lightGame that updates the playability of a card in the hand of the user
 */
public class HandDiffUpdatePlayability extends HandDiff{
    /**The NEW playability of the card. True if the card is playable, false otherwise*/
    private final Boolean playability;
    /**
     * Constructor
     * @param card the LightCard to which the diff applies
     * @param playability the new playability of the card
     */
    public HandDiffUpdatePlayability(LightCard card, Boolean playability) {
        super(card);
        this.playability = playability;
    }

    /**
     * Update the playability of the card in the hand of the user
     * @param game to which the diff must be applied
     */
    @Override
    public void apply(LightGame game) {
        game.updateHandPlayability(card, playability);
    }
}
