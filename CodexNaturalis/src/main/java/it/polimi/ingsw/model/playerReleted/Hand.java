package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.cards.CardInHand;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**The class constructor*/
public class Hand implements Serializable {
    /** the secret objective chosen by the player*/
    private ObjectiveCard secretObjective;
    /** the start card before placing it in the codex*/
    private StartCard startCard;
    /** the secret objective options that the player can choose from*/
    private List<ObjectiveCard> secretObjectiveChoices;
    /** actual hand of the player, the cards that the player can place in the codex*/
    final private Set<CardInHand> playableHand;
    /** the lock for the start card*/
    final private ReentrantLock startCardLock = new ReentrantLock();
    /** the lock for the secret objective choices*/
    final private ReentrantLock secretObjectiveChoicesLock = new ReentrantLock();
    /** the lock for the secret objective*/
    final private ReentrantLock secretObjectiveLock = new ReentrantLock();

    /**
     * The constructor of the class, initialize the hand of the player to an empty set
     */
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

    /**
     * Set the start card of the player
     * @param startCard the start card that need to be set
     */
    public void setStartCard(StartCard startCard) {
        startCardLock.lock();
        try{
            this.startCard = startCard == null ? null : new StartCard(startCard);
        } finally {
            startCardLock.unlock();
        }
    }

    /**
     * @return the start card of the player
     */
    public StartCard getStartCard() {
        startCardLock.lock();
        try{
            return startCard == null ? null : new StartCard(startCard);
        } finally {
            startCardLock.unlock();
        }
    }

    /**
     * Set the secret objective choices
     * @param secretObjectiveChoices the secret objective choices that need to be set
     */
    public void setSecretObjectiveChoice(List<ObjectiveCard> secretObjectiveChoices) {
        secretObjectiveChoicesLock.lock();
        try{
            this.secretObjectiveChoices = secretObjectiveChoices == null ? null : new LinkedList<>(secretObjectiveChoices);
        } finally {
            secretObjectiveChoicesLock.unlock();
        }
    }

    /**
     * @return the secret objective choices
     */
    public List<ObjectiveCard> getSecretObjectiveChoices() {
        secretObjectiveChoicesLock.lock();
        try{
            return secretObjectiveChoices == null ? null : new LinkedList<>(secretObjectiveChoices);
        } finally {
            secretObjectiveChoicesLock.unlock();
        }
    }

    /**
     * @return the number of cards in the hand
     */
    public int getHandSize(){
        return playableHand.size();
    }
}
