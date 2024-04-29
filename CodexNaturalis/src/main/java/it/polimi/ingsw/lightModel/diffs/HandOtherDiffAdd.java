package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

public class HandOtherDiffAdd extends HandOtherDiff{

    public HandOtherDiffAdd(Resource card, String owner) {
        super(card, owner);
    }

    @Override
    public void apply(LightGame lightGame) {
        lightGame.addOtherHand(owner, card);
    }
}
