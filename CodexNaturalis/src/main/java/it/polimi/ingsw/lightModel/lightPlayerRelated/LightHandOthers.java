package it.polimi.ingsw.lightModel.lightPlayerRelated;

import it.polimi.ingsw.utils.designPatterns.Observed;
import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.Differentiable;

import java.util.LinkedList;
import java.util.List;

public class LightHandOthers implements Differentiable, Observed {
    private final List<Observer> observers = new LinkedList<>();
    private final LightBack[] cards;
    public LightHandOthers(){
        cards = new LightBack[3];
    }
    public LightHandOthers(LightBack[] cards){
        if(cards.length > 3)
            throw new IllegalArgumentException();
        else
            this.cards = cards;
    }
    public LightBack[] getCards() {
        return cards;
    }
    private int length(LightBack[] arr){
        int i=0;
        for(LightBack back: arr){
            if(back!=null){
                i++;
            }
        }
        return i;
    }
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
