package it.polimi.ingsw.view;

import java.io.Serializable;
import java.rmi.Remote;

public interface LoggerInterface extends Serializable, Remote {
    /** Logs a message to the view. */
    void log(String logMsg) throws Exception;

    /** Logs an error message to the view. And renders the active commands*/
    void logErr(String logMsg) throws Exception;

    /** Logs a message to the view relative to somebody else's action. */
    void logOthers(String logMsg) throws Exception;

    /** Logs a message to the view relative to the game. */
    void logGame(String logMsg) throws Exception;
    void logChat(String logMsg) throws Exception;

}
