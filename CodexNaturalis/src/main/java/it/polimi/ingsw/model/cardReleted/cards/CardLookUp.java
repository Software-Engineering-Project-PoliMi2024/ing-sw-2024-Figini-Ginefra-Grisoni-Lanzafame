package it.polimi.ingsw.model.cardReleted.cards;

import java.util.*;

/**
 * This class is used to look up cards by their id.
 * Mapping is done by the id of the card.
 * @param <T> The type of the card to look up.
 */
public class CardLookUp<T extends Card>{
    /** The map that contains the cards of the specified type T. */
    private final Map<Integer, T> cardMap;

    /**
     * Constructor.
     * @param queue The queue of cards from which retrieve the cards given their id.
     */
    public CardLookUp(Queue<T> queue){
        this.cardMap = new HashMap<>();
        for(T card : queue){
            cardMap.put(card.getIdFront(), card);
        }
    }

    /**
     * Look up a card by its id.
     * @param id The id of the card to look up.
     * @return The card with the specified id.
     */
    public T lookUp(int id){
        return cardMap.get(id);
    }

    /**
     * @return The queue of cards from which the cards were retrieved.
     */
    public Queue<T> getQueue(){
        Queue<T> returnQueue = new LinkedList<>();
        for(Map.Entry<Integer, T> entry : this.cardMap.entrySet()){
            returnQueue.add(entry.getValue());
        }
        return returnQueue;
    }

    /**
     * Check if a card with the specified id is present in the map.
     * @param id The id of the card to check.
     * @return True if the card is present, false otherwise.
     */
    public boolean isPresent(int id){
        return cardMap.containsKey(id);
    }

}
