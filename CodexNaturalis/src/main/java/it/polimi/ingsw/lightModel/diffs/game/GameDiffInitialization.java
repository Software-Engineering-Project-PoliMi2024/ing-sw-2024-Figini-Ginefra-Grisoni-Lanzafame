package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;
import java.util.SimpleTimeZone;

public class GameDiffInitialization extends GameDiff{
    private final String gameName;
    private final String yourName;
    private final List<String> players;
    private final GameDiffCurrentPlayer currentPlayer;

    /**
     * Creates a diff to initialize the lightGame with the number of players,
     * the game name, the current player and the player's name
     * @param players the list of players that are expected to play the game (including those disconnected)
     * @param gameName the name of the game
     * @param yourName the name of the player receiving the diff
     * @param currentPlayer the name of the player who is currently playing
     */
    public GameDiffInitialization(List<String> players, String gameName, String yourName, GameDiffCurrentPlayer currentPlayer) {
        this.players = players;
        this.gameName = gameName;
        this.yourName = yourName;
        this.currentPlayer = currentPlayer;
    }
    @Override
    public void apply(LightGame game) {
        game.setGameName(gameName);
        game.setYourName(yourName);
        game.gameInitialization(players);
        currentPlayer.apply(game);
    }
}
