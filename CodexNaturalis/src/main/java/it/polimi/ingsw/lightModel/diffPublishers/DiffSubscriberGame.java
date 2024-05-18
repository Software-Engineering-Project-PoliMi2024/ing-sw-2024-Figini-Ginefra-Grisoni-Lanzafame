package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public interface DiffSubscriberGame {
    void updateGame(ModelDiffs<LightGame> diff);

}
