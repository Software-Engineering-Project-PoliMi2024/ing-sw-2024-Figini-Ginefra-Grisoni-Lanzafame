package it.polimi.ingsw.controller3.mediators;

import it.polimi.ingsw.controller3.mediatorSubscriber.GameMediatorSubscriber;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public class GameMediator extends Mediator<GameMediatorSubscriber, LightGame>{
    @Override
    void notify(GameMediatorSubscriber subscriber, ModelDiffs<LightGame> diff) {

    }
}
