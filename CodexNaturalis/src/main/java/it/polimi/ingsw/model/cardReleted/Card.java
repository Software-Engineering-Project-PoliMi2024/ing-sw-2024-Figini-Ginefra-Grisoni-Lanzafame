package it.polimi.ingsw.model.cardReleted;

import it.polimi.ingsw.model.playerReleted.Codex;

public abstract class Card {
    final private int points;

    public Card(int points){
        this.points = points;
    }

    public int getPoints(){
        return points;
    }

    public int getPoints(Codex codex){
        return 0;
    }
}
