package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.utils.designPatterns.Observed;
import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the decks in the game seen by the view.
 * In the buffer the cards are fully visible, but for the top card of the deck only the back is visible.
 */
public class LightDeck implements Differentiable, Observed {
    /** The buffer of the deck */
    private final LightCard[] cardBuffer;
    /** The list of observers */
    private final List<Observer> observerList;
    /** The back of the top card on the deck */
    private LightBack deckBack;

    /**
     * Constructor of the class
     * Initialize the buffer and the list of observers
     */
    public LightDeck() {
        this.cardBuffer = new LightCard[2];
        observerList = new ArrayList<>();
    }

    /**
     * @return the pointer to the card buffer array
     */
    public LightCard[] getCardBuffer() {
        return cardBuffer;
    }

    /**
     * @return the lightBack of the top card of the deck
     */
    public LightBack getDeckBack() {
        return deckBack;
    }
    /**
     * Set a new top card of the deck
     * At the end of the update the observers are notified
     * @param cardDeck the lightBack of the new top card of the deck
     */
    public void setTopDeckCard(LightBack cardDeck) {
        this.deckBack = cardDeck;
        this.notifyObservers();
    }
    /**
     * Substitute a card in the buffer with a new one
     * @param card the new card in the buffer to add
     * @param position the position where to add the new card and remove the old one (0 or 1)
     */
    public void substituteBufferCard(LightCard card, Integer position) {
        this.cardBuffer[position] = card;
        this.notifyObservers();
    }

    @Override
    public void attach(Observer observer) {
        observerList.add(observer);
        this.notifyObservers();
    }

    @Override
    public void detach(Observer observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observerList){
            observer.update();
        }
    }
}
