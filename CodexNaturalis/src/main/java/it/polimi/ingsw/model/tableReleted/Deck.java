package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

public class Deck<Element> implements Serializable {
    private final int bufferSize;
    private final List<Element> buffer;
    private final Queue<Element> actualDeck;

    /**
     * Constructor for the deck
     * @param bufferSize the size of the buffer
     * @param elements the elements to put in the deck
     */
    public Deck(int bufferSize, Queue<Element> elements) {
        this.bufferSize = bufferSize;
        this.actualDeck = elements;
        this.buffer = new ArrayList<>();
        this.shuffle();
        this.populateBuffer();
    }

    /**
     * Copy constructor
     * @param other the deck to copy
     */
    public Deck(Deck<Element> other){
        this.bufferSize = other.bufferSize;
        this.actualDeck = new LinkedList<>(other.actualDeck);
        this.buffer = other.buffer.stream().toList();
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
         return new LinkedHashSet<>(buffer);
    }

    /**
     * Get the actual deck
     * @return the actual deck
     */
    public Queue<Element> getActualDeck() {
        return new LinkedList<>(actualDeck);
    }

    /**
     * Draw an element from the buffer given is index then it repopulates the Deck with another card
     * @param indexElement the index of the element to draw
     * @return the element drawn
     */
    public Element drawFromBuffer(int indexElement){
        Element element = buffer.get(indexElement);
        if(element!=null){
            Element newElement = this.drawFromDeck();
            if(newElement != null){
                buffer.set(indexElement, newElement);
            }else{
                buffer.remove(indexElement);
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
        if(bufferId < buffer.stream().toList().size())
            return buffer.stream().toList().get(bufferId);
        else
            return null;
    }

    public boolean isEmpty(){
        return actualDeck.isEmpty() && buffer.isEmpty();
    }
    public Element peekCardInDecks(int cardID){
        Element card;
        if (cardID == Configs.actualDeckPos) {
            card = showTopCardOfDeck();
        } else {
            card = showCardFromBuffer(cardID);
        }
        return card;
    }
}
