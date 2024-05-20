package it.polimi.ingsw.lightModel.lightPlayerRelated;

import it.polimi.ingsw.designPatterns.Observed;
import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LightHand implements Differentiable, Observed {
    private final List<Observer> observers = new LinkedList<>();
    private LightCard secretObjective;
    private final Map<LightCard, Boolean> cardPlayability;
    private final LightCard[] cards;
    private final LightCard[] secretObjectiveOptions = new LightCard[2];

    /**
     * The constructor of the class
     * */
    public LightHand(){
        this.cardPlayability = new HashMap<>();
        this.cards = new LightCard[3];
    }

    /**
     * @param obj the secret objective of the player
     * @param cards the cards of the player and their playability
     */
    public LightHand(LightCard obj, Map<LightCard, Boolean> cards){
        this.secretObjective = obj;
        this.cardPlayability = cards;
        this.cards = cards.keySet().toArray(new LightCard[2]);

    }
    public LightHand(Map<LightCard, Boolean> cards){
        this.cardPlayability = cards;
        this.cards = cards.keySet().toArray(new LightCard[2]);
    }
    /**
     * Set the secret objective of the player
     * @param secretObjective the secret objective of the player
     */
    public void setSecretObjective(LightCard secretObjective) {
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
     * @return the cards of the player
     */
    public Map<LightCard, Boolean> getCardPlayability() {
        return cardPlayability;
    }
    /**
     * @return the cards of the player
     */
    public LightCard[] getCards() {
        return cards;
    }
    /**
     * @param card to check
     * @return if the card is playable
     */
    public Boolean isPlayble(LightCard card){
        return cardPlayability.get(card);
    }
    /**
     * Add a card to the cards array and the cardPlayability Map
     * @param card that need to be added
     * @param playability is a boolean that specified if the card is playable
     * @throws IllegalCallerException if the player has already enough card
     */
    public void addCard(LightCard card, Boolean playability){
        if(length(cards) == 3){
            throw new IllegalCallerException("hand is full");
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
     * Remove a lightCard from the cards array and the cardPlayability map
     * @param card that need to be removed
     */
    public void removeCard(LightCard card){
        boolean found = false;
        for(int i=0; i<cards.length && !found; i++){
            if(cards[i].equals(card)){
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
     * Add a card to the secret objective options
     * @param card that need to be added
     * @throws IllegalCallerException if the secret objective options are already full
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
