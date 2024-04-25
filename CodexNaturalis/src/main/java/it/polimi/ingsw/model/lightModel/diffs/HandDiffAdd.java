package it.polimi.ingsw.model.lightModel.diffs;

import it.polimi.ingsw.model.lightModel.LightCard;
import it.polimi.ingsw.model.lightModel.LightHand;

public class HandDiffAdd extends HandDiff {
    private final boolean playability;
    public HandDiffAdd(LightCard card, boolean playbility) {
        super(card);
        this.playability = playbility;
    }

    public boolean isPlayable() {
        return playability;
    }
    public void apply(LightHand hand) {
        hand.addCard(super.card, this.playability);
    }
}
