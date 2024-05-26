package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

public abstract class HandOtherDiff extends GameDiff{
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
