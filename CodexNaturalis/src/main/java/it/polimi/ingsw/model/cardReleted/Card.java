package it.polimi.ingsw.model.cardReleted;

public abstract class Card {
    final private int points;

    public Card(int points){
        this.points = points;
    }

    public int getPoints(){
        return points;
    }
}
