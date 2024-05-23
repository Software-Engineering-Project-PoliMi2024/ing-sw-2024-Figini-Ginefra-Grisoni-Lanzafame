package it.polimi.ingsw.view;

import it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces.LightModelUpdater;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ViewInterface extends LightModelUpdater, Serializable, Remote {
    void transitionTo(ViewState state) throws Exception;
    /** Logs a message to the view. */
    void log(String logMsg) throws Exception;

    /** Logs an error message to the view. And renders the active commands*/
    void logErr(String logMsg) throws Exception;

    /** Logs a message to the view relative to somebody else's action. */
    void logOthers(String logMsg) throws Exception  ;

    /** Logs a message to the view relative to the game. */
    void logGame(String logMsg) throws Exception;

}
