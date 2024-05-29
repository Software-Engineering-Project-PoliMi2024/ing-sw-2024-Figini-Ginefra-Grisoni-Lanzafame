package it.polimi.ingsw.lightModel.diffs.game.handDiffOther;

import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;

public abstract class HandOtherDiff extends GameDiff {
    protected final LightBack card;
    protected final String owner;

    /**
     * Creates a diff to update the lightGame with the card of another player
     * @param card the card of the other player
     * @param owner the owner of the card
     */
    protected HandOtherDiff(LightBack card, String owner){
        this.card = card;
        this.owner = owner;
    }
}
