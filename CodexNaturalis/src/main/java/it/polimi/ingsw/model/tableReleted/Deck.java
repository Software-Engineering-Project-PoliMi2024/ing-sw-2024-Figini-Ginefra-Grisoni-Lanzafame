package it.polimi.ingsw.model.tableReleted;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Deck<Element> {
    private final int bufferSize;
    private Set<Element> buffer = new HashSet<Element>();
    private Queue<Element> actualDeck = new LinkedList<Element>();

    public Deck(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    private void shuffle(){    }
    public Set<Element> getBuffer(){
        return buffer;
    }

    public Queue<Element> getActualDeck() {
        return actualDeck;
    }

    public Element drawFromBuffer(){
        return null;
    }
    public Element drawFromDeck(){
        return null;
    }
}
