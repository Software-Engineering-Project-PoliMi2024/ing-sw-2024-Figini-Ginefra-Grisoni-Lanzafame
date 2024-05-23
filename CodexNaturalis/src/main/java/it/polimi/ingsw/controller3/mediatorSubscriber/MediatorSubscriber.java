package it.polimi.ingsw.controller3.mediatorSubscriber;

import it.polimi.ingsw.view.LoggerInterface;

public interface MediatorSubscriber extends LoggerInterface {
    /** Logs a message to the view. */
    @Override
    void log(String logMsg);

    /** Logs an error message to the view. And renders the active commands*/
    @Override
    void logErr(String logMsg);

    /** Logs a message to the view relative to somebody else's action. */
    @Override
    void logOthers(String logMsg);

    /** Logs a message to the view relative to the game. */
    @Override
    void logGame(String logMsg);
}
