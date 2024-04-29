package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightGame;
import it.polimi.ingsw.lightModel.LightHand;

public class HandDiffAdd extends HandDiff {
    private final boolean playability;
    /**
     * @param card the LightCard to which the diff applies
     * @param playbility the playability of the card
     */
    public HandDiffAdd(LightCard card, boolean playbility) {
        super(card);
        this.playability = playbility;
    }
    /**
     * @return the playability of the card
     */
    public boolean isPlayable() {
        return playability;
    }
    /**
     * @param game from which get the LightHand to which the diff applies
     */
    public void apply(LightGame game) {
        game.addCard(super.card, this.playability);
    }
}
