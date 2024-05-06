package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

public abstract class HandOtherDiff extends GameDiff{
    protected final Resource card;
    protected final String owner;
    protected HandOtherDiff(Resource card, String owner){
        this.card = card;
        this.owner = owner;
    }
}
