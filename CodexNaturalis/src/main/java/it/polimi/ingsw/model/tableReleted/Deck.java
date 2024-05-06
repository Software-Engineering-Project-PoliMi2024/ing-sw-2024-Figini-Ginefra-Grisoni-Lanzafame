package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.model.cardReleted.cards.Card;

import java.io.Serializable;
import java.util.*;

public class Deck<Element> implements Serializable {
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
        this.buffer = new LinkedHashSet<>();
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
     * Draw an element from the buffer given is index then it repopulate the Deck with another card
     * @param indexElement the index of the element to draw
     * @return the element drawn
     */
    public Element drawFromBuffer(int indexElement){
        List<Element> tmpList = new LinkedList<>(buffer);
        Element element = tmpList.get(indexElement);
        if(element!=null){
            buffer.remove(element);
            Element newElement = this.drawFromDeck();
            if(newElement != null){
                buffer.add(newElement);
            }
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

    /**
     * @return the top Card of the Deck don't remove it
     */
    public Element showTopCardOfDeck(){
        return actualDeck.peek();
    }
    /**
     * @return the bufferID card from the buffer but do not remove it
     */
    public Element showCardFromBuffer(int bufferId){
        List<Element> tmpList = new LinkedList<>(buffer);
        Element element = tmpList.get(bufferId);
        return element;
    }

    public boolean isEmpty(){
        return actualDeck.isEmpty() && buffer.isEmpty();
    }
}
