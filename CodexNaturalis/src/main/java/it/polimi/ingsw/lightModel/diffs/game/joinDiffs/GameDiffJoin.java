package it.polimi.ingsw.lightModel.diffs.game.joinDiffs;

import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for all the GameDiff that are sent to a player when he joins a game
 */
public class GameDiffJoin extends GameDiff {
    /**The diff that set the initial information of the Game (i.e. game's name, user's name ecc)*/
    private final GameDiffInitialization initialization;
    /**The list of all GameDiff that are sent to the player who his joining the game*/
    private final List<GameDiff> gameDiffs = new ArrayList<>();

    /**
     * Constructor
     * @param initialization the GameDiffInitialization that sets the initial information of the Game
     */
    public GameDiffJoin(GameDiffInitialization initialization){
        this.initialization = initialization;
    }

    /**
     * Add a GameDiff to the list of GameDiff that are sent to the player who his joining the game
     * @param gameDiff the GameDiff to add
     */
    public void put(GameDiff gameDiff){
        gameDiffs.add(gameDiff);
    }

    /**
     * Add a list of GameDiff to the list of GameDiff that are sent to the player who his joining the game
     * @param gameDiffs the list of GameDiff to add
     */
    public void put(List<GameDiff> gameDiffs){
        this.gameDiffs.addAll(gameDiffs);
    }

    /**
     * For each GameDiff in the list of GameDiff that are sent to the player, apply them to the LightGame
     * @param lightGame the LightGame to which the GameDiffJoin must be applied
     */
    @Override
    public void apply(LightGame lightGame) {
        initialization.apply(lightGame);
        for (GameDiff gameDiff : gameDiffs) {
            gameDiff.apply(lightGame);
        }
    }
}
