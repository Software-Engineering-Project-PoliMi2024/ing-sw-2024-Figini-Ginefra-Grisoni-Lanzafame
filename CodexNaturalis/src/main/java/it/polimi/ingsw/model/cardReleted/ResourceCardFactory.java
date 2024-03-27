package it.polimi.ingsw.model.cardReleted;

import java.util.Queue;

/**
 * The concreteFactory for the Resource type of Card
 */
public class ResourceCardFactory implements CardFactoryInterface<ResourceCard>{
    @Override
    public Queue<ResourceCard> getCards() {
        //cardCreationLogic
        return null; //placeHolder
    }
}
