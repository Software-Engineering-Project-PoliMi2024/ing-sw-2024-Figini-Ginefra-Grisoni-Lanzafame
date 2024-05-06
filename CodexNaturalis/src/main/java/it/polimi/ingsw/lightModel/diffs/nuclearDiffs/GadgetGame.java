package it.polimi.ingsw.lightModel.diffs.nuclearDiffs;

import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;

public class GadgetGame extends GameDiff {
    @Override
    public void apply(LightGame lightGame) {
        lightGame.reset();
    }
}
