package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.PawnColors;

public class GameDiffSetPlayerColor extends GameDiff{
    private final PawnColors color;
    private final String nickname;

    public GameDiffSetPlayerColor(String nickname, PawnColors color){
        this.color = color;
        this.nickname = nickname;
    }


    @Override
    public void apply(LightGame lightGame) {
        lightGame.addPlayerColor(nickname, color);
    }
}
