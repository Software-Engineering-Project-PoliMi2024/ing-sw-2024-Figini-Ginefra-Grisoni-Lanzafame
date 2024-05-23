package it.polimi.ingsw.controller3.mediatorSubscriber;

import it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces.LightGameUpdater;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public interface GameMediatorSubscriber extends MediatorSubscriber, LightGameUpdater {
    @Override
    void updateGame(ModelDiffs<LightGame> diff);
    @Override
    void setFinalRanking(String[] nicks, int[] points);
}
