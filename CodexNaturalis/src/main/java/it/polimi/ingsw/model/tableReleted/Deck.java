package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.model.cardReleted.cards.CardInHand;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

/**
 * This class represents a deck of elements (cards)
 * @param <Element> the type of the elements in the deck
 */
public class Deck<Element> implements Serializable {
    /** the buffer size */
    private final int bufferSize;
    /** the buffer */
    private final List<Element> buffer;
    /** the actual deck */
    private final List<Element> actualDeck;

    /** the next Element to be drawn */
    private Element nextDraw;

    /**
     * Constructor for the deck
     * @param bufferSize the size of the buffer
     * @param elements the elements to put in the deck
     */
    public Deck(int bufferSize, Queue<Element> elements) {
        this.bufferSize = bufferSize;
        this.actualDeck = new LinkedList<>(elements);
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
        this.nextDraw = other.nextDraw;
    }

    /**
     * Shuffle the deck
     */
    private void shuffle(){
        if(nextDraw != null)
            actualDeck.add(nextDraw);
        List<Element> deck = new ArrayList<>(actualDeck);
        Collections.shuffle(deck);
        actualDeck.clear();
        actualDeck.addAll(deck);
        nextDraw = actualDeckPoll();
    }

    /**
     * Gets the first element of the actual deck and removes it
     * @return the first element of the actual deck
     */
    private Element actualDeckPoll(){
        Element next = actualDeck.getFirst();
        actualDeck.removeFirst();
        return next;
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
            buffer.set(indexElement, newElement);
            return element;
        }
        return null;
    }

    /**
     * Draw an element from the deck
     * @return the element drawn
     */
    public Element drawFromDeck(){
        Element drawn = nextDraw;
        nextDraw = actualDeckPoll();
        return drawn;
    }

    /**
     * @return the top Card of the Deck don't remove it
     */
    public Element showTopCardOfDeck(){
        return nextDraw;
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

    /** @return true if both the actual deck and the buffer are empty */
    public boolean isEmpty(){
        return buffer.stream().allMatch(Objects::isNull) && nextDraw == null;
    }

    /**
     * Peek the element in the deck or in the buffer
     * @param cardID the id of the element to peek
     * @return the element peeked
     */
    public Element peekCardInDecks(int cardID){
        Element card;
        if (cardID == Configs.actualDeckPos) {
            card = showTopCardOfDeck();
        } else {
            card = showCardFromBuffer(cardID);
        }
        return card;
    }

    /**
     * @return the number of cards in the buffer of the deck
     */
    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * @return the number of cards left in the deck
     */
    public int numberOfCardsLeftInDeck(){
        return isEmpty() ? 0 : actualDeck.size() + 1;
    }

    /**
     * @return the number of cards left in the buffer
     */
    public int numberOfCardsLeftInBuffer() {
        return (int) buffer.stream().filter(Objects::nonNull).count();
    }

    /**
     * @return the number of cards left in the deck and in the buffer
     */
    public int numberOfDrawsLeft(){
        return numberOfCardsLeftInDeck() + numberOfCardsLeftInBuffer();
    }
}
