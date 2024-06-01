package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**The class constructor*/
public class Hand implements Serializable {
    private ObjectiveCard secretObjective;
    private StartCard startCard;
    private List<ObjectiveCard> secretObjectiveChoices;
    final private Set<CardInHand> playableHand;
    final private ReentrantLock startCardLock = new ReentrantLock();
    final private ReentrantLock secretObjectiveChoicesLock = new ReentrantLock();
    final private ReentrantLock secretObjectiveLock = new ReentrantLock();

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
    /** sets the secret objective chosen and sets the secretObjectiveOptions to Null
     * @param objective is set to being the player secret objective*/
    public void setSecretObjective(ObjectiveCard objective){
        secretObjectiveLock.lock();
        try{
            this.secretObjective = objective == null ? null : new ObjectiveCard(objective);
            this.secretObjectiveChoices = null;
        } finally {
            secretObjectiveLock.unlock();
        }
    }
    /** @return the user nickname*/
    public Set<CardInHand> getHand(){
        return new LinkedHashSet<>(playableHand);
    }
    /** @return the secretObjective nickname*/
    public ObjectiveCard getSecretObjective(){
        secretObjectiveLock.lock();
        try{
            return secretObjective == null ? null : new ObjectiveCard(secretObjective);
        } finally {
            secretObjectiveLock.unlock();
        }
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
        startCardLock.lock();
        try{
            this.startCard = startCard == null ? null : new StartCard(startCard);
        } finally {
            startCardLock.unlock();
        }
    }

    public StartCard getStartCard() {
        startCardLock.lock();
        try{
            return startCard == null ? null : new StartCard(startCard);
        } finally {
            startCardLock.unlock();
        }
    }

    public void setSecretObjectiveChoice(List<ObjectiveCard> secretObjectiveChoices) {
        secretObjectiveChoicesLock.lock();
        try{
            this.secretObjectiveChoices = secretObjectiveChoices == null ? null : new LinkedList<>(secretObjectiveChoices);
        } finally {
            secretObjectiveChoicesLock.unlock();
        }
    }

    public List<ObjectiveCard> getSecretObjectiveChoices() {
        secretObjectiveChoicesLock.lock();
        try{
            return secretObjectiveChoices == null ? null : new LinkedList<>(secretObjectiveChoices);
        } finally {
            secretObjectiveChoicesLock.unlock();
        }
    }

    public void addSecretObjectiveChoice(ObjectiveCard objectiveCard){
        secretObjectiveChoicesLock.lock();
        try{
            if (secretObjectiveChoices == null) {
                secretObjectiveChoices = new LinkedList<>();
            }
            secretObjectiveChoices.add(objectiveCard);
        } finally {
            secretObjectiveChoicesLock.unlock();
        }
    }

    public int getHandSize(){
        return playableHand.size();
    }
}
