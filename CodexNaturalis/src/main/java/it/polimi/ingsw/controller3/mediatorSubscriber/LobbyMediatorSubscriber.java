package it.polimi.ingsw.controller3.mediatorSubscriber;

import it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces.LightLobbyUpdater;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;

public interface LobbyMediatorSubscriber extends MediatorSubscriber, LightLobbyUpdater {
    @Override
    void updateLobby(ModelDiffs<LightLobby> diff);
}
