package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.PawnColors;

import java.util.List;

public class GameDiffSetPawns extends GameDiff{
    private final List<PawnColors> pawnColors;

    public GameDiffSetPawns(List<PawnColors> pawnColors){
        this.pawnColors = pawnColors;
    }

    @Override
    public void apply(LightGame lightGame) {
        lightGame.setPawnChoices(pawnColors);
    }
}
