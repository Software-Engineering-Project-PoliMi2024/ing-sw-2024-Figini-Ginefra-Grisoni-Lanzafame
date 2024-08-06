package it.polimi.ingsw.model.playerReleted.AgentRelated.StateRelated;

import it.polimi.ingsw.model.tableReleted.Game;

import java.util.*;

/**
 * This class represents a belief about a deck of elements (cards). It is used to keep track of the cards that could be left in the deck and the cards that are in the buffer
 * @param <Element>
 */
public class DeckBelief<Element> {
    private final HashSet<Element> cardsLeft;
    private final List<Element> Buffer;
    private int drawsLeft;

    public DeckBelief(Queue<Element> cardsLeft, Set<Element> Buffer, int drawsLeft) {
        this.cardsLeft = new HashSet<>(cardsLeft);
        this.Buffer = new LinkedList<>(Buffer);
        this.drawsLeft = drawsLeft;
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
        if(index < 0 || index >= Buffer.size())
            throw new IllegalArgumentException("Index out of bounds");

        Element drawn = Buffer.get(index);
        Element newBufferElement = drawFromDeck();
        Buffer.remove(index);
        Buffer.add(index, newBufferElement);
        return drawn;
    }

    /**
     * @return whether there are still draws left
     */
    public boolean canDraw(){
        return drawsLeft > 0;
    }
}
