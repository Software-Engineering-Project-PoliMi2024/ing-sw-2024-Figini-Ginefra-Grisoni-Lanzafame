package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

public class HandOtherDiffRemove extends HandOtherDiff{
    public HandOtherDiffRemove(LightBack card, String owner) {
        super(card, owner);
    }

    @Override
    public void apply(LightGame lightGame) {
        lightGame.getHandOthers().get(owner).removeCard(card);
    }
}
