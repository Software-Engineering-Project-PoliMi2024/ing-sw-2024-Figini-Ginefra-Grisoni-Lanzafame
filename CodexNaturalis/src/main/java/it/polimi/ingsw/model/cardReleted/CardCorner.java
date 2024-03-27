package it.polimi.ingsw.model.cardReleted;

import it.polimi.ingsw.model.playerReleted.Position;

public enum CardCorner {

    TL(1, -1),
    TR(1, 1),
    BR(-1, 1),
    BL(-1, -1);
    final private Position offset;

    CardCorner(int x, int y){
        this.offset = new Position(x, y);
    }

    public Position getOffset(){
        return offset;
    }

    /*public String toString(){
        return "CardCorner[" + this.name() + "]";
    }*/

}

