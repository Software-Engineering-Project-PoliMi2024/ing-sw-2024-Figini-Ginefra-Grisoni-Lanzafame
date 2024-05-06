package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public class GameDiffGameNames extends GameDiff{
    private final String yourName;
    private final String gameName;
    public GameDiffGameNames(String gameName, String yourName) {
        this.gameName = gameName;
        this.yourName = yourName;
    }
    @Override
    public void apply(LightGame lightGame) {
        lightGame.setGameName(gameName);
        lightGame.setYourName(yourName);
    }
}
