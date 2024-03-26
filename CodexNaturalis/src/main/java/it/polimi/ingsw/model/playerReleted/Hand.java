package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.CardInHand;
import it.polimi.ingsw.model.cardReleted.ObjectiveCard;
import java.util.HashSet;
import java.util.Set;

/**The class constructor*/
public class Hand {
    private ObjectiveCard secretObjective;
    final private Set<CardInHand> playableHand;
    public Hand(){
        this.playableHand = new HashSet<>();
    }

    /**
     * Add a card to the player hand
     * @param card the card that need to be added
     * @throws toManyCardException is throw if the player already have 3 card in hand
     */
    public void addCard(CardInHand card) throws toManyCardException{
        if(playableHand.size()==3){
            throw new toManyCardException("Each player can have a max of 3 card in hand");
        }else{
            playableHand.add(card);
        }
    }

    /** Remove a card from the player hand
     *
      * @param card the card that need to be removed
     * @throws cardNotFoundException is throw if the card is not found
     */
    public void removeCard(CardInHand card) throws cardNotFoundException{
        if(!playableHand.remove(card)){
            throw new cardNotFoundException("The card is not in this hand");
        }
    }
    /** @param objective is set to being the player secret objective*/
    public void setSecretObjective(ObjectiveCard objective){
        this.secretObjective=objective;
    }
    /** @return the user nickname*/
    public Set<CardInHand> getHand(){
        return this.playableHand;
    }
    /** @return the secretObjective nickname*/
    public ObjectiveCard getSecretObjective(){
        return this.secretObjective;
    }

    @Override
    public String toString() {
        StringBuilder cardSetName = new StringBuilder();
        for (CardInHand card : playableHand) {
            cardSetName.append(card.toString()).append(", ");
        }
        return "Hand{" +
                "secretObjective is: " + secretObjective +
                ", playableHand is made of: " + cardSetName +
                '}';
    }
}
