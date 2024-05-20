package it.polimi.ingsw.view;

import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ViewInterface extends Serializable, Remote {
    void transitionTo(ViewState state) throws RemoteException;
    /** Logs a message to the view. */
    void log(String logMsg) throws RemoteException;

    /** Logs an error message to the view. And renders the active commands*/
    void logErr(String logMsg) throws RemoteException;

    /** Logs a message to the view relative to somebody else's action. */
    void logOthers(String logMsg) throws RemoteException;

    /** Logs a message to the view relative to the game. */
    void logGame(String logMsg) throws RemoteException;
    void updateLobbyList(ModelDiffs<LightLobbyList> diff) throws RemoteException;
    void updateLobby(ModelDiffs<LightLobby> diff) throws RemoteException;
    void updateGame(ModelDiffs<LightGame> diff) throws RemoteException;
    void setFinalRanking(String[] nicks, int[] points) throws RemoteException;
}
