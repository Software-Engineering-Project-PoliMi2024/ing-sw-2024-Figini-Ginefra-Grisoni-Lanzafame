package it.polimi.ingsw.lightModel.diffs.game.joinDiffs;

import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffCurrentPlayer;
import it.polimi.ingsw.lightModel.diffs.game.gamePartyDiffs.GameDiffFirstPlayer;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

/**
 * An update that initialize the lightGame for the player who receives the diff.
 * It sets the game name, the player's name, the name of the other players, the current player and the first player
 */
public class GameDiffInitialization extends GameDiff {
    /**The name of the game*/
    private final String gameName;
    /**The name of the player receiving the diff*/
    private final String yourName;
    /**The list of players that are expected to play the game (including those disconnected)*/
    private final List<String> players;
    /**The name of the player who is the current player*/
    private final GameDiffCurrentPlayer currentPlayer;
    /**The name of the player who is the first player*/
    private final GameDiffFirstPlayer firstPlayer;

    /**
     * Creates a diff to initialize the lightGame with the number of players,
     * the game name, the current player, the others players name and the user's name
     * @param players the list of players that are expected to play the game (including those disconnected)
     * @param gameName the name of the game
     * @param yourName the name of the player receiving the diff
     * @param currentPlayer the name of the player who is currently playing
     */
    public GameDiffInitialization(List<String> players, String gameName, String yourName, GameDiffCurrentPlayer currentPlayer, GameDiffFirstPlayer firstPlayer) {
        this.players = players;
        this.gameName = gameName;
        this.yourName = yourName;
        this.currentPlayer = currentPlayer;
        this.firstPlayer = firstPlayer;
    }

    /**
     * Apply the diff to the lightGame by setting the game name, the player's name, the name of the other players, the current player and the first player
     * @param game the game to which the diff must be applied
     */
    @Override
    public void apply(LightGame game) {
        game.setGameName(gameName);
        game.setYourName(yourName);
        game.gameInitialization(players);
        currentPlayer.apply(game);
        firstPlayer.apply(game);
    }
}
