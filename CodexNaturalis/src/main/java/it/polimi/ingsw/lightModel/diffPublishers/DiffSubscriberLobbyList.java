package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

public interface DiffSubscriberLobbyList {
    void updateLobbyList(ModelDiffs<LightLobbyList> diff);

}
