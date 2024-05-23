package it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces;

import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public interface LightGameUpdater extends Updater{
    void updateGame(ModelDiffs<LightGame> diff) throws Exception;
    void setFinalRanking(String[] nicks, int[] points) throws Exception;
}
