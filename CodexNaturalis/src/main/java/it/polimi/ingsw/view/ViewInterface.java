package it.polimi.ingsw.view;

import it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces.LightModelUpdater;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ViewInterface extends LightModelUpdater, LoggerInterface, Serializable, Remote {
    void transitionTo(ViewState state) throws Exception;
}
