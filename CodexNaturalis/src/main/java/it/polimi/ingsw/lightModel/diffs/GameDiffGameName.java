package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public class GameDiffGameName extends GameDiff{
    String gameName;
    public GameDiffGameName(String gameName) {
        this.gameName = gameName;
    }
    @Override
    public void apply(LightGame lightGame) {
        lightGame.setGameName(gameName);
    }
}
