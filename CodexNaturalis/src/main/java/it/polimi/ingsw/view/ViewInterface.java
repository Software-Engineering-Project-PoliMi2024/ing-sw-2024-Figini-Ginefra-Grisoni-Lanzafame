package it.polimi.ingsw.view;

import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ViewInterface extends Serializable, Remote {
    void transitionTo(ViewState state) throws Exception;
    /** Logs a message to the view. */
    void log(String logMsg) throws Exception;

    /** Logs an error message to the view. And renders the active commands*/
    void logErr(String logMsg) throws Exception;

    /** Logs a message to the view relative to somebody else's action. */
    void logOthers(String logMsg) throws Exception  ;

    /** Logs a message to the view relative to the game. */
    void logGame(String logMsg) throws Exception;
    void updateLobbyList(ModelDiffs<LightLobbyList> diff) throws Exception;
    void updateLobby(ModelDiffs<LightLobby> diff) throws Exception;
    void updateGame(ModelDiffs<LightGame> diff) throws Exception;
    void setFinalRanking(String[] nicks, int[] points) throws Exception;
}
