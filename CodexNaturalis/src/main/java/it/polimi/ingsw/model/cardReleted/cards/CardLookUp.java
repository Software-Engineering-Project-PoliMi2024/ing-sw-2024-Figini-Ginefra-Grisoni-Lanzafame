package it.polimi.ingsw.model.cardReleted.cards;
import it.polimi.ingsw.model.cardReleted.cards.Card;
import it.polimi.ingsw.model.tableReleted.Deck;

import java.util.*;

public class CardLookUp<T extends Card>{
    private Map<Integer, T> cardMap;
    
    public CardLookUp(){
        this.cardMap = new HashMap<>();
    }

    public CardLookUp(Queue<T> queue){
        this.cardMap = new HashMap<>();
        for(T card : queue){
            cardMap.put(card.getIdFront(), card);
        }
    }
    
    public T lookUp(int id){
        return cardMap.get(id);
    }
    
    public void putDown(T card){
        cardMap.put(card.getIdFront(), card);
    }

    public void deckPutDown(Deck<T> deck){
        Map<Integer, T> newMap = new HashMap<>();
        for(T card : deck.getActualDeck()){
            cardMap.put(card.getIdFront(), card);
        }
        this.cardMap = newMap;
    }

    public Queue<T> getQueue(){
        Queue<T> returnQueue = new LinkedList<>();
        for(Map.Entry<Integer, T> entry : this.cardMap.entrySet()){
            returnQueue.add(entry.getValue());
        }
        return returnQueue;
    }

    public boolean isPresent(int id){
        return cardMap.containsKey(id);
    }

}
