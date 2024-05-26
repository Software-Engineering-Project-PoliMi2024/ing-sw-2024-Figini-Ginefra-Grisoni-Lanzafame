package it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces;

import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public interface LightGameUpdater extends Updater{
    /**
     * Update the game with a diff
     * @param diff the diff to apply
     * @throws Exception if the connection between the server and the client fails
     */
    void updateGame(ModelDiffs<LightGame> diff) throws Exception;

    /**
     * Set the final ranking of the game
     * @param nicks the nicknames of the players
     * @param points the points of the players
     * @throws Exception if the connection between the server and the client fails
     */
    void setFinalRanking(String[] nicks, int[] points) throws Exception;
}
