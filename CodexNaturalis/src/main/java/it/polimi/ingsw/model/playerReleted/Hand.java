package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.CardInHand;
import it.polimi.ingsw.model.cardReleted.ObjectiveCard;

import java.util.Set;

public class Hand {
    ObjectiveCard secretObjective;
    Set<CardInHand> playableHand;
    public Hand(){
        secretObjective = null;
        playableHand = null;
    }
    public void addCard(CardInHand card){

    }
    public void removeCard(CardInHand card){

    }
    public Set<CardInHand> getHand(){
        return null;
    }
    public ObjectiveCard getSecretObjective(){
        return null;
    }
    public void setSecretObjective(ObjectiveCard objective){

    }
}
