package it.polimi.ingsw.model.cardReleted.cards;

import it.polimi.ingsw.model.playerReleted.Codex;

import java.io.Serializable;


public abstract class Card implements Serializable {
    final private int points;
    private final int id;

    /** @param  points given by the card*/
    public Card(int id, int points){
        this.id = id;
        this.points = points;
    }

    /** @return the id of the card */
    public int getId(){
        return id;
    }

    /** @return the points of the card */
    public int getPoints(){
        return points;
    }

    /** @return the points of the card multiplied by the multiplier
     * @param codex the codex from which calc the points */
    public int getPoints(Codex codex){
        return codex.getPoints();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != this.getClass()) return false;
        Card card = (Card) obj;
        return card.getId() == this.getId();
    }
}
