package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.LightGame;
import it.polimi.ingsw.lightModel.diffs.GameDiff;

public class GameDiffPublicObj extends GameDiff {
    private final LightCard[] publicObjective = new LightCard[2];

    public GameDiffPublicObj(LightCard publicObjective1, LightCard publicObjective2){
        publicObjective[0] = publicObjective1;
        publicObjective[1] = publicObjective2;
    }
    @Override
    public void apply(LightGame differentiableType) {
        differentiableType.addObjective(publicObjective[0]);
        differentiableType.addObjective(publicObjective[1]);
    }
}
