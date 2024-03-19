package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.CardFace;
import it.polimi.ingsw.model.cardReleted.CardWithCorners;
import it.polimi.ingsw.model.cardReleted.Collectable;

import java.util.Map;

public class Placement {
    final CardWithCorners card;
    final Position position;
    final CardFace finalFace;

    final Map<Collectable, Integer> consequences;

    public Placement(Position position, CardWithCorners card, CardFace finalFace){
        this.finalFace = finalFace;
        this.position = position;
        this.card = card;
        this.consequences = null;
    }

    public CardWithCorners getCard(){
        return this.card;
    }
    public Position getPosition(){
        return this.position;
    }
    public CardFace getFace(){
        return this.finalFace;
    }
    public Map<Collectable, Integer> getConsequences(){
        return null;
    }
}
