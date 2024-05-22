package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

public class GameDiffInitialization extends GameDiff{
    private final GameDiffGameName gameName;
    private final GameDiffYourName yourName;
    private final List<String> players;
    private final GameDiffCurrentPlayer currentPlayer;
    public GameDiffInitialization(List<String> players, GameDiffGameName gameName, GameDiffYourName yourName, GameDiffCurrentPlayer currentPlayer) {
        this.players = players;
        this.gameName = gameName;
        this.yourName = yourName;
        this.currentPlayer = currentPlayer;
    }
    @Override
    public void apply(LightGame game) {
        gameName.apply(game);
        yourName.apply(game);
        game.gameInitialization(players);
        currentPlayer.apply(game);
    }
}
