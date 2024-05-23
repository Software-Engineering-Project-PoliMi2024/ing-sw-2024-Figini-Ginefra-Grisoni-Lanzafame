package it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces;

import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;

public interface LightLobbyUpdater extends Updater{
    void updateLobby(ModelDiffs<LightLobby> diff) throws Exception;
}
