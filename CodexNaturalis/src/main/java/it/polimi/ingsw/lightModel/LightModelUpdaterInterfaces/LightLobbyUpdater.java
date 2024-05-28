package it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces;

import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LightLobbyUpdater extends Updater, Serializable, Remote {
    void updateLobby(ModelDiffs<LightLobby> diff) throws Exception;
}
