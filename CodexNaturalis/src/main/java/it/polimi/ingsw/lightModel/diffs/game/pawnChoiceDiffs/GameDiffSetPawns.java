package it.polimi.ingsw.lightModel.diffs.game.pawnChoiceDiffs;

import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.PawnColors;

import java.util.List;

/**
 * This class contains an update to the lightGame that sets all the possible pawn colors options
 */
public class GameDiffSetPawns extends GameDiff {
    /**The list of pawn colors that can be chosen*/
    private final List<PawnColors> pawnColors;

    /**
     * Creates a new GameDiffSetPawns
     * @param pawnColors the list of pawn colors that can be chosen
     */
    public GameDiffSetPawns(List<PawnColors> pawnColors){
        this.pawnColors = pawnColors;
    }

    /**
     * set the pawn choices in the lightGame
     * @param lightGame the lightGame to which the diff must be applied
     */
    @Override
    public void apply(LightGame lightGame) {
        lightGame.setPawnChoices(pawnColors);
    }
}
