package it.polimi.ingsw.lightModel.lightPlayerRelated;

import it.polimi.ingsw.utils.designPatterns.Observed;
import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.Differentiable;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is a light version of the Hand class in the model of the user who does not own the client.
 * It contains only the info that the client should know about the hand of the other players.
 * In particular, only the lightBack of the cards that the player has in his hand is known by the client.
 */
public class LightHandOthers implements Differentiable, Observed {
    /** The list of observers of the class */
    private final List<Observer> observers = new LinkedList<>();
    /** An array of lightBack that contains the lightBack of the cards that the, other, player has in his hand */
    private final LightBack[] cards;

    /**
     * The constructor of the class
     * Initializes the backIdCard array with 3 null lightBack
     */
    public LightHandOthers(){
        cards = new LightBack[3];
    }

    /**
     * @return the pointer to the backIdCard array
     */
    public LightBack[] getCards() {
        return cards;
    }

    /**
     * Return the number of not null elements in the backIdCard
     * @param arr an array of lightBack
     * @return the number of not null elements in the given array
     */
    private int length(LightBack[] arr){
        int i=0;
        for(LightBack back: arr){
            if(back!=null){
                i++;
            }
        }
        return i;
    }

    /**
     * Add a LightBack to the backIdCard array
     * At the end of the update the observers are notified
     * @param card the lightBack to be added
     * @throws IllegalCallerException if the array already has three elements
     */
    public void addCard(LightBack card){
        if(length(cards) > 3){
            throw new IllegalCallerException();
        }else{
            for(int i=0; i<cards.length; i++){
                if(cards[i]==null){
                    cards[i]=card;
                    break;
                }
            }
        }
        notifyObservers();
    }

    /**
     * Remove a LightBack from the backIdCard array
     * At the end of the update the observers are notified
     * @param card the lightBack to be removed
     * @throws IllegalArgumentException if the backId is not in the array
     */
    public void removeCard(LightBack card){
        if(card == null){
            throw new IllegalArgumentException("card can not be null");
        }
        boolean found = false;
        for(int i=0; i<cards.length && !found; i++){
            if(cards[i].equals(card)){
                cards[i] = null;
                found=true;
            }
        }
        if(!found){
            throw new IllegalArgumentException();
        }
        notifyObservers();
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
        for(Observer o: observers){
            o.update();
        }
    }
}
