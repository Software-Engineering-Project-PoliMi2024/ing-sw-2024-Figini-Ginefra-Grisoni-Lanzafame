package it.polimi.ingsw.model.tableReleted;

import java.util.*;

public class Deck<Element> {
    private final int bufferSize;
    private final Set<Element> buffer;
    private final Queue<Element> actualDeck;

    /**
     * Constructor for the deck
     * @param bufferSize the size of the buffer
     * @param elements the elements to put in the deck
     */
    public Deck(int bufferSize, Queue<Element> elements) {
        this.bufferSize = bufferSize;
        this.actualDeck = elements;
        this.buffer = new HashSet<>();
        this.shuffle();
        this.populateBuffer();
    }

    /**
     * Shuffle the deck
     */
    private void shuffle(){
        List<Element> deck = new ArrayList<>(actualDeck);
        Collections.shuffle(deck);
        actualDeck.clear();
        actualDeck.addAll(deck);
    }

    /**
     * Populate the buffer
     */
    private void populateBuffer(){
        for(int i = 0; i < bufferSize; i++){
            buffer.add(this.drawFromDeck());
        }
    }

    /**
     * Get the buffer
     * @return the buffer
     */
    public Set<Element> getBuffer(){
        return buffer;
    }

    /**
     * Get the actual deck
     * @return the actual deck
     */
    public Queue<Element> getActualDeck() {
        return actualDeck;
    }

    /**
     * Draw an element from the buffer
     * @param element the element to draw
     * @return the element drawn
     */
    public Element drawFromBuffer(Element element){
        if(buffer.contains(element)){
            buffer.remove(element);
            buffer.add(this.drawFromDeck());
            return element;
        }
        throw new IllegalArgumentException("Element not in buffer");
    }

    /**
     * Draw an element from the deck
     * @return the element drawn
     */
    public Element drawFromDeck(){
        return actualDeck.poll();
    }
}
