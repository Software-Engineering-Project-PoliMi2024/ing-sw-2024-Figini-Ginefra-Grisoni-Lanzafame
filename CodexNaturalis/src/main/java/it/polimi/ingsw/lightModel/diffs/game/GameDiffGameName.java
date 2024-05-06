package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public class GameDiffGameName extends GameDiff{
    private final String gameName;
    public GameDiffGameName(String gameName, String yourName) {
        this.gameName = gameName;
    }
    @Override
    public void apply(LightGame lightGame) {
        lightGame.setGameName(gameName);
    }
}
