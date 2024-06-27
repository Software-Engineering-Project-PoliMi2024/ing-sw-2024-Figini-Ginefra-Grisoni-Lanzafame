package it.polimi.ingsw.view;

import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This interface represents the view of the game.
 * This can be implemented by both a real View and a Virtual View.
 * It has methods to update the view with the game state and to log messages.
 */
public interface ViewInterface extends Serializable, Remote {
    /** Transitions the view to a new state. */
    void transitionTo(ViewState state) throws Exception;

    /** Logs a message to the view. */
    void log(String logMsg) throws Exception;

    /** Logs an error message to the view. And renders the active commands*/
    void logErr(String logMsg) throws Exception;

    /** Logs a message to the view relative to somebody else's action. */
    void logOthers(String logMsg) throws Exception;

    /** Logs a message to the view relative to the game. */
    void logGame(String logMsg) throws Exception;

    /** Logs a message to the view relative to the chat. */
    void logChat(String logMsg) throws Exception;

    /** Applies the given diff to the light lobby list.
     * @param diff the diff to apply
     * @throws RemoteException if the connection between the server and the client fails
     */
    void updateLobbyList(ModelDiffs<LightLobbyList> diff) throws Exception;

    /** Applies the given diff to the light lobby.
     * @param diff the diff to apply
     * @throws RemoteException if the connection between the server and the client fails
     */
    void updateLobby(ModelDiffs<LightLobby> diff) throws Exception;

    /**
     * Update the game with a diff
     * @param diff the diff to apply
     * @throws RemoteException if the connection between the server and the client fails
     */
    void updateGame(ModelDiffs<LightGame> diff) throws Exception;
}
