package it.polimi.ingsw.model.cardReleted;

import com.google.gson.JsonArray;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The concreteFactory for the Resource type of Card
 */
public class GoldCardFactory extends AbstractCardFactory<GoldCard>{
    private final Queue<GoldCard> deckBuilder = new LinkedList<>();
    @Override
    public Queue<GoldCard> getCards() {
        JsonArray ResourceCards = getCardArray("GoldCards");
        //cardCreationLogic
        return null; //placeHolder
    }
}
