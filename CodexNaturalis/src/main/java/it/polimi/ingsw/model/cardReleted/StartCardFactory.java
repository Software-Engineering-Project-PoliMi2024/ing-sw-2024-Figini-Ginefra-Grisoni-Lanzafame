package it.polimi.ingsw.model.cardReleted;

import java.util.Queue;

/**
 * The concreteFactory for the Resource type of Card
 */
public class StartCardFactory implements CardFactoryInterface<StartCard>{
    @Override
    public Queue<StartCard> getCards() {
        //cardCreationLogic
        return null; //placeHolder
    }
}
