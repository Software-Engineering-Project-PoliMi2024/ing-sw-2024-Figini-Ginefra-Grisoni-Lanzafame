package it.polimi.ingsw.lightModel.lightPlayerRelated;

import it.polimi.ingsw.utils.designPatterns.Observed;
import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.Differentiable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is a light version of the Hand class in the model of the user who own the view.
 * It contains all the data the user can see about his hand such as the LightCard he has, the playability of each card ù
 * and his own secret objective.
 */
public class LightHand implements Differentiable, Observed {
    /** The list of observers of the class */
    private final List<Observer> observers = new LinkedList<>();
    /**The secretObjective chosen by the player. If a secretObjective is not chosen yet the attribute is null */
    private LightCard secretObjective;
    /**A map that contains the playability of each card that the player has in his hand */
    private final Map<LightCard, Boolean> cardPlayability;
    /**The cards that the player has in his hand */
    private final LightCard[] cards;
    /**The secretObjective options that the player has received from the server.
     * If the player has not received any secretObjective options or has already chosen one, each element of the array is null */
    private final LightCard[] secretObjectiveOptions = new LightCard[2];

    /**
     * The constructor of the class
     * It initializes an empty cardPlayability map and an empty cards array
     */
    public LightHand(){
        this.cardPlayability = new HashMap<>();
        this.cards = new LightCard[3];
    }

    /**
     * The constructor of the class
     * It initializes and populate the cardPlayability map and the card array
     * with the given cards passed as parameter
     * @param cards a map that contains the playability of each lightCard that the player has in his hand
     */
    public LightHand(Map<LightCard, Boolean> cards){
        this.cardPlayability = cards;
        this.cards = cards.keySet().toArray(new LightCard[2]);
    }
    /**
     * Set the secret objective of the player and set each element of the secretObjectiveOptions array to null
     * At the end of the update the observers are notified
     * @param secretObjective the secret objective of the player
     */
    public void setSecretObjective(LightCard secretObjective) {
        this.secretObjectiveOptions[0] = null;
        this.secretObjectiveOptions[1] = null;
        this.secretObjective = secretObjective;
        this.notifyObservers();
    }
    /**
     * @return the secret objective of the player
     */
    public LightCard getSecretObjective() {
        return secretObjective;
    }
    /**
     * @return the lightCard in the hand of the player has a map where each card is associated with his playability
     */
    public Map<LightCard, Boolean> getCardPlayability() {
        return cardPlayability;
    }
    /**
     * @return an array of LightCard that contains the cards that the player has in his hand
     */
    public LightCard[] getCards() {
        return cards;
    }
    /**
     * @param card the lightCard that need to be checked if it is playable
     * @return true if the card is playable, false otherwise
     */
    public Boolean isPlayable(LightCard card){
        return cardPlayability.get(card);
    }

    /**
     * Update the playability of a given LightCard
     * At the end of the update the observers are notified
     * @param card the lightCard that need to have his playability updated
     * @param playability the new playability of the card
     */
    public void updatePlayability(LightCard card, Boolean playability){
        cardPlayability.put(card, playability);
        this.notifyObservers();
    }
    /**
     * Add a lightCard to the card array and the cardPlayability Map
     * At the end of the update the observers are notified
     * @param card that need to be added
     * @param playability is a boolean that specified if the card is playable (true) or not (false)
     * @throws IllegalCallerException if the player already has three lightCards in hand
     */
    public void addCard(LightCard card, Boolean playability){
        if(length(cards) == 3){
            throw new IllegalCallerException("The player has already 3 cards");
        }else{
            for(int i=0; i<cards.length; i++){
                if(cards[i]==null){
                    cards[i]=card;
                    cardPlayability.put(card, playability);
                    break;
                }
            }
        }
        this.notifyObservers();

    }
    /**
     * Return how many not null lightCard are present the given array
     * @param arr an array of LightCard
     * @return the number of not null elements in the card array
     */
    private int length(LightCard[] arr){
        int i=0;
        for(LightCard r: arr){
            if(r!=null){
                i++;
            }
        }
        return i;
    }
    /**
     * Remove a lightCard from the card array and the cardPlayability map
     * At the end of the update the observers are notified
     * @param card that need to be removed
     * @throws IllegalArgumentException if the card is not present in the card array
     */
    public void removeCard(LightCard card){
        boolean found = false;
        for(int i=0; i<cards.length && !found; i++){
            if(cards[i] != null && cards[i].equals(card)){
                cards[i] = null;
                cardPlayability.remove(card);
                found=true;
            }
        }
        if(!found){
            throw new IllegalArgumentException();
        }
        this.notifyObservers();
    }

    /**
     * Add a lightCard as a secret objective options
     * At the end of the update the observers are notified
     * @param card that need to be added
     * @throws IllegalCallerException if the secret objective options are already two
     */
    public void addSecretObjectiveOption(LightCard card){
        if(secretObjectiveOptions[0]==null){
            secretObjectiveOptions[0] = card;
        }else if(secretObjectiveOptions[1]==null){
            secretObjectiveOptions[1] = card;
        }else{
            throw new IllegalCallerException("The secret objective options are already full");
        }
        this.notifyObservers();
    }

    /**
     * @return a copy of the secret objective options
     */
    public LightCard[] getSecretObjectiveOptions() {
        return new LightCard[]{secretObjectiveOptions[0], secretObjectiveOptions[1]};
    }

    @Override
    public void attach(Observer observer) {
        observer.update();
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }
}
