package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.lightModel.diffPublishers.GameDiffPublisher;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public class GameDiffPublicObj extends GameDiff {
    private final LightCard[] publicObjective = new LightCard[2];

    public GameDiffPublicObj(){}
    public GameDiffPublicObj(LightCard[] cards){
        publicObjective[0] = cards[0];
        publicObjective[1] = cards[1];
    }
    @Override
    public void apply(LightGame differentiableType) {
        differentiableType.addObjective(publicObjective[0]);
        differentiableType.addObjective(publicObjective[1]);
    }
}
