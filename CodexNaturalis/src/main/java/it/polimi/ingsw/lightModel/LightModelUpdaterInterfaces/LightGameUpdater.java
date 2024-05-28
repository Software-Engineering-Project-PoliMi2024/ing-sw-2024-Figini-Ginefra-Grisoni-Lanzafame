package it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces;

import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LightGameUpdater extends Updater, Serializable, Remote {
    /**
     * Update the game with a diff
     * @param diff the diff to apply
     * @throws RemoteException if the connection between the server and the client fails
     */
    void updateGame(ModelDiffs<LightGame> diff) throws Exception;

    /**
     * Set the final ranking of the game
     * @param nicks the nicknames of the players
     * @param points the points of the players
     * @throws RemoteException if the connection between the server and the client fails
     */
    void setFinalRanking(String[] nicks, int[] points) throws Exception;
}
