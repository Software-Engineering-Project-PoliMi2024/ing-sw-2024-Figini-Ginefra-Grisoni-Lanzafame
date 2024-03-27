package it.polimi.ingsw.model.cardReleted;

import java.util.Queue;

/**
 * The concreteFactory for the Resource type of Card
 */
public class ObjectiveCardFactory implements CardFactoryInterface<ObjectiveCard>{
    @Override
    public Queue<ObjectiveCard> getCards() {
        //cardCreationLogic
        return null; //placeHolder
    }
}
