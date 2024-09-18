package it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated;

import java.util.*;

/**
 * This class represents a belief about a deck of elements (cards). It is used to keep track of the cards that could be left in the deck and the cards that are in the buffer
 * @param <Element>
 */
public class DeckBelief<Element> {
    private final HashSet<Element> cardsLeft;
    private final List<Element> buffer;
    private int drawsLeft;

    public DeckBelief(Queue<Element> cardsLeft, Set<Element> Buffer, int drawsLeft) {
        this.cardsLeft = new HashSet<>(cardsLeft);
        this.buffer = new LinkedList<>(Buffer);
        this.drawsLeft = drawsLeft;
    }

    public DeckBelief(DeckBelief<Element> other){
        this.cardsLeft = new HashSet<>(other.cardsLeft);
        this.buffer = new LinkedList<>(other.buffer);
        this.drawsLeft = other.drawsLeft;
    }

    /**
     * Gets a random element from the distribution
     * @return the element
     */
    public Element sample(){
        return cardsLeft.isEmpty() ? null : cardsLeft.stream().skip((int) (Math.random() * cardsLeft.size())).findFirst().get();
    }

    /**
     * Draws a card from the deck and removes it
     * @return the card
     */
    public Element drawFromDeck(){
        Element drawn = sample();
        if(drawn != null){
            cardsLeft.remove(drawn);
            drawsLeft--;
        }
        return drawn;
    }

    /**
     * Draws a card from the buffer at the given index and replaces it with a new card from the deck
     * @param index the index of the card to draw
     * @return the drawn card
     */
    public Element drawFromBufferAt(int index){
        if(index < 0 || index >= buffer.size())
            throw new IllegalArgumentException("Index out of bounds");

        Element drawn = buffer.get(index);
        Element newBufferElement = drawFromDeck();
        buffer.remove(index);
        buffer.add(index, newBufferElement);
        return drawn;
    }

    /**
     * Draws a card from the buffer at the given index and does not replace it
     * @param index the index of the buffer position to draw from
     * @return the drawn card
     */
    public Element drawFromBufferNoReplace(int index){
        if(index < 0 || index >= buffer.size())
            throw new IllegalArgumentException("Index out of bounds");

        Element drawn = buffer.get(index);
        buffer.remove(index);
        return drawn;
    }

    /**
     * @return whether there are still draws left
     */
    public boolean canDraw(){
        return drawsLeft > 0;
    }

    public int bufferSize(){
        //Count non null elements
        return (int) buffer.stream().filter(Objects::nonNull).count();
    }

    public void addToBufferInEmptySlot(Element element){
        for(int i = 0; i < buffer.size(); i++){
            if(buffer.get(i) == null){
                buffer.set(i, element);
                return;
            }
        }
        throw new IllegalStateException("Buffer is full");
    }

    public int numCardsLeft(){
        return cardsLeft.size();
    }

    public Element getCardLeftAt(int index){
        if(index < 0 || index >= cardsLeft.size())
            throw new IllegalArgumentException("Index out of bounds");

        return cardsLeft.stream().skip(index).findFirst().orElse(null);
    }
}
