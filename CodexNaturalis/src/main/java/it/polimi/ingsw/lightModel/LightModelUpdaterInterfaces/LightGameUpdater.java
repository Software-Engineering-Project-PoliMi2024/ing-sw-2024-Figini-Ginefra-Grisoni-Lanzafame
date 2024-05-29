package it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces;

import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface LightGameUpdater extends Updater, Serializable, Remote {
    /**
     * Update the game with a diff
     * @param diff the diff to apply
     * @throws RemoteException if the connection between the server and the client fails
     */
    void updateGame(ModelDiffs<LightGame> diff) throws Exception;

    /**
     * Set the final ranking of the game
     * @param ranking the ordered list of the final raking in decreasing order
     *                depending on player points
     * @throws RemoteException if the connection between the server and the client fails
     */
    void setFinalRanking(List<String> ranking) throws Exception;
}
