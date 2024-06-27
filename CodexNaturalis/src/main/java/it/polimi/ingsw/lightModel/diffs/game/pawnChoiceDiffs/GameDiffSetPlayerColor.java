package it.polimi.ingsw.lightModel.diffs.game.pawnChoiceDiffs;

import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.model.playerReleted.PawnColors;

/**
 * This class contains an update to the lightGame that sets the color of the pawn chosen by a player
 */
public class GameDiffSetPlayerColor extends GameDiff {
    /**The color of the pawn chosen by the player*/
    private final PawnColors color;
    /**The nickname of the player that chose the color*/
    private final String nickname;

    /**
     * Constructor
     * Creates a new GameDiffSetPlayerColor
     * @param nickname the nickname of the player that chose the color
     * @param color the color of the pawn chosen by the player
     */
    public GameDiffSetPlayerColor(String nickname, PawnColors color){
        this.color = color;
        this.nickname = nickname;
    }

    /**
     * Update the lightGame with the color chosen by the player
     * @param lightGame the lightGame to which the diff must be applied
     */
    @Override
    public void apply(LightGame lightGame) {
        lightGame.addPlayerColor(nickname, color);
    }
}
