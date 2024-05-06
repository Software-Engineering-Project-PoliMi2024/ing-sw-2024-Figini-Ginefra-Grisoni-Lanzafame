package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

import java.util.List;

public class GameDiffInitialization extends GameDiff{
    private final GameDiffGameName gameName;
    private final GameDiffYourName yourName;
    private final List<String> players;
    public GameDiffInitialization(List<String> players, GameDiffGameName gameName, GameDiffYourName yourName) {
        this.players = players;
        this.gameName = gameName;
        this.yourName = yourName;
    }
    @Override
    public void apply(LightGame game) {
        gameName.apply(game);
        yourName.apply(game);
        game.gameInitialization(players);
    }
}
