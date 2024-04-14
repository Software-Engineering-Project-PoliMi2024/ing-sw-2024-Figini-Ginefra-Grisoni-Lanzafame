package it.polimi.ingsw.model.cardReleted.cards;

import it.polimi.ingsw.model.playerReleted.Codex;

import java.io.Serializable;


public abstract class Card implements Serializable {
    final private int points;

    /** @param  points given by the card*/
    public Card(int points){
        this.points = points;
    }

    /** @return the points of the card */
    public int getPoints(){
        return points;
    }

    /** @return the points of the card multiplied by the multiplier
     * @param codex the codex from which calc the points */
    public int getPoints(Codex codex){
        return 0;
    }
}
