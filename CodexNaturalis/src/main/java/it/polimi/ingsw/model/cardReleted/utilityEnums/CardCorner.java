package it.polimi.ingsw.model.cardReleted.utilityEnums;

import it.polimi.ingsw.model.playerReleted.Position;

import java.io.Serializable;
import java.util.Arrays;

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

    public CardCorner getOpposite(){
        Position offset = this.getOffset().multiply(-1);
        return Arrays.stream(CardCorner.values()).filter(corner -> corner.getOffset().equals(offset)).findFirst().orElse(null);
    }

    /*public String toString(){
        return "CardCorner[" + this.name() + "]";
    }*/

}

