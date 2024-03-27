package it.polimi.ingsw.model.cardReleted;

import java.util.Queue;

/**
 * The interface implemented by all concrete factory
 * @param <Element> is the "type" of Card that will be produced
 */
public interface CardFactoryInterface<Element> {
    Queue<Element> getCards();
}
