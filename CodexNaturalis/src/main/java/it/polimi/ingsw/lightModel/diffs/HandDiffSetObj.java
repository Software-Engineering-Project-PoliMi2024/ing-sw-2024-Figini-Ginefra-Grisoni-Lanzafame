package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public class HandDiffSetObj extends HandDiff{
    public HandDiffSetObj(LightCard secretObjective) {
        super(secretObjective);
    }
    @Override
    public void apply(LightGame game) {
        game.setSecretObjective(super.card);
    }
}
