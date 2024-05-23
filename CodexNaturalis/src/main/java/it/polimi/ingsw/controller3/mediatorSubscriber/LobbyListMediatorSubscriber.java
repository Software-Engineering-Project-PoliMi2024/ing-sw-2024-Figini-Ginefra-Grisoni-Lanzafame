package it.polimi.ingsw.controller3.mediatorSubscriber;

import it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces.LightLobbyListUpdater;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

public interface LobbyListMediatorSubscriber extends MediatorSubscriber, LightLobbyListUpdater {
    @Override
    void updateLobbyList(ModelDiffs<LightLobbyList> diff);
}
