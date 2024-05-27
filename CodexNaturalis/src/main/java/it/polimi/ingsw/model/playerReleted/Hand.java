package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;

import java.io.Serializable;
import java.util.*;

/**The class constructor*/
public class Hand implements Serializable {
    private ObjectiveCard secretObjective;
    private StartCard startCard;
    private List<ObjectiveCard> secretObjectiveChoices;
    final private Set<CardInHand> playableHand;

    public Hand(){
        this.playableHand = new HashSet<>();
    }


    /**
     * Add a card to the player hand
     * @param card the card that need to be added
     * @throws IllegalCallerException is throw if the player already have 3 card in hand
     */
    public void addCard(CardInHand card) throws IllegalCallerException{
        if(playableHand.size()==3){
            throw new IllegalCallerException("Each player can have a max of 3 card in hand");
        }else{
            playableHand.add(card.copy());
        }
    }

    /** Remove a card from the player hand
      * @param card the card that need to be removed
     * @throws IllegalArgumentException is throw if the card is not found
     */
    public void removeCard(CardInHand card) throws IllegalArgumentException{
        if(!playableHand.remove(card)){
            throw new IllegalArgumentException("The card is not in this hand");
        }
    }
    /** @param objective is set to being the player secret objective*/
    public void setSecretObjective(ObjectiveCard objective){
        this.secretObjective=objective;
    }
    /** @return the user nickname*/
    public Set<CardInHand> getHand(){
        return new LinkedHashSet<>(playableHand);
    }
    /** @return the secretObjective nickname*/
    public ObjectiveCard getSecretObjective(){
        return secretObjective == null ? null : new ObjectiveCard(secretObjective);
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

    public void setStartCard(StartCard startCard) {
        this.startCard = startCard == null ? null : new StartCard(startCard);
    }

    public StartCard getStartCard() {
        return startCard == null ? null : new StartCard(startCard);
    }

    public void setSecretObjectiveChoice(List<ObjectiveCard> secretObjectiveChoices) {
        this.secretObjectiveChoices = secretObjectiveChoices == null ? null : new LinkedList<>(secretObjectiveChoices);
    }

    public List<ObjectiveCard> getSecretObjectiveChoices() {
        return secretObjectiveChoices == null ? null : new ArrayList<>(secretObjectiveChoices);
    }

    public void addSecretObjectiveChoice(ObjectiveCard objectiveCard){
        if(secretObjectiveChoices == null){
            secretObjectiveChoices = new LinkedList<>();
        }
        secretObjectiveChoices.add(objectiveCard);
    }

    public int getHandSize(){
        return playableHand.size();
    }
}
