package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.designPatterns.Observed;
import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightBack;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;

import java.util.ArrayList;
import java.util.List;

public class LightDeck implements Differentiable, Observed {
    private final LightCard[] cardBuffer;
    private final List<Observer> observerList;
    private LightBack deckBack;

    /**
     * Constructor of the class
     */
    public LightDeck() {
        this.cardBuffer = new LightCard[2];
        observerList = new ArrayList<>();
    }
    public LightCard[] getCardBuffer() {
        return cardBuffer;
    }
    public LightBack getDeckBack() {
        return deckBack;
    }
    /**
     * @param cardDeck the resource of the first gold card on top of the deck
     */
    public void setTopDeckCard(LightBack cardDeck) {
        this.deckBack = cardDeck;
        this.notifyObservers();
    }
    /**
     * @param card the card to add
     * @param position the position where to add the new card and remove the old one
     */
    public void substituteBufferCard(LightCard card, Integer position) {
        this.cardBuffer[position] = card;
        this.notifyObservers();
    }

    @Override
    public void attach(Observer observer) {
        observerList.add(observer);
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
