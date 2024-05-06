package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public class GameDiffYourName extends GameDiff{
    private final String yourName;

    public GameDiffYourName(String yourName){
        this.yourName = yourName;
    }

    @Override
    public void apply(LightGame lightGame) {
        lightGame.setYourName(yourName);
    }
}
