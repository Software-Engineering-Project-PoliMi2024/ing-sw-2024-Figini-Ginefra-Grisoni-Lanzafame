package it.polimi.ingsw.model.lightModel.diffs;

import it.polimi.ingsw.model.lightModel.LightCard;
import it.polimi.ingsw.model.lightModel.LightHand;

public class HandDiffAdd extends HandDiff {
    private final boolean playbility;
    public HandDiffAdd(LightCard card, boolean playbility) {
        super(card);
        this.playbility = playbility;
    }

    public boolean isPlaybility() {
        return playbility;
    }
    public void apply(LightHand hand) {
        hand.addCard(super.card, this.playbility);
    }
}
