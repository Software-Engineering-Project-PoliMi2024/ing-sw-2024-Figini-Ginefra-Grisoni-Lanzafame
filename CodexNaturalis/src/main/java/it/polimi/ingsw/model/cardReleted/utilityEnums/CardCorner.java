package it.polimi.ingsw.model.cardReleted.utilityEnums;

import it.polimi.ingsw.model.playerReleted.Position;

import java.io.Serializable;

public enum CardCorner implements Serializable {

    BR(1, -1),
    TR(1, 1),
    TL(-1, 1),
    BL(-1, -1);
    final private Position offset;

    CardCorner(int x, int y){
        this.offset = new Position(x, y);
    }

    public Position getOffset(){
        return new Position(offset);
    }

    /*public String toString(){
        return "CardCorner[" + this.name() + "]";
    }*/

}

