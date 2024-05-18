package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;

public interface DiffSubscriberLobby {
    public void updateLobby(ModelDiffs<LightLobby> diff);

}
