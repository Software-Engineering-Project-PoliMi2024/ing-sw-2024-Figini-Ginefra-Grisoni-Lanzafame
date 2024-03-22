package it.polimi.ingsw.model.cardReleted;

public abstract class CardInHand extends CardWithCorners{
    private final Resource permanentResource;

    public CardInHand(){
        super();
        this.permanentResource = null;
    }

    public boolean canBePlaced(){
        return true;
    }
}
