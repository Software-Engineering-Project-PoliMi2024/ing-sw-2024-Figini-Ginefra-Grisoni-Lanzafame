package it.polimi.ingsw.model.cardReleted;

import java.util.Queue;

/**
 * The concreteFactory for the Resource type of Card
 */
public class GoldCardFactory implements CardFactoryInterface<GoldCard>{
    @Override
    public Queue<GoldCard> getCards() {
        //cardCreationLogic
        return null; //placeHolder
    }
}
