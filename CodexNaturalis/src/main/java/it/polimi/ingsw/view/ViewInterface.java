package it.polimi.ingsw.view;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.lightModel.diffObserverInterface.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ViewInterface extends DiffSubscriber, Serializable, Remote {
    void setState(ViewState state) throws RemoteException;
    void transitionTo(ViewState state) throws RemoteException;
    void postConnectionInitialization(ControllerInterface controller) throws RemoteException;
    void log(String logMsg) throws RemoteException;
    void updateLobbyList(ModelDiffs<LightLobbyList> diff) throws RemoteException;
    void updateLobby(ModelDiffs<LightLobby> diff) throws RemoteException;
    void updateGame(ModelDiffs<LightGame> diff) throws RemoteException;
    void setFinalRanking(String[] nicks, int[] points) throws RemoteException;
}