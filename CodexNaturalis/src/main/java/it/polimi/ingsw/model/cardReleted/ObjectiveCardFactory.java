package it.polimi.ingsw.model.cardReleted;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The concreteFactory for the Objective type of Card
 */
public class ObjectiveCardFactory extends AbstractCardFactory<ObjectiveCard>{
    private final Queue<ObjectiveCard> deckBuilder = new LinkedList<>();
    /**
     * @return the QueueOfCards
     */
    @Override
    public Queue<ObjectiveCard> getCards() {
        JsonArray ResourceCards = getCardArray("ObjectiveCard");
        for(int i=0; i<ResourceCards.size(); i++) {
            JsonObject card = ResourceCards.get(i).getAsJsonObject();
            deckBuilder.add(new ObjectiveCard(card.get("points").getAsInt(),
                    getPointMultiplier(card)));
        }
        return deckBuilder;
    }

    /**
     * @param card that is being build
     * @return a pointer to the correct Multiplier
     */
    private void getPointMultiplier(JsonObject card){
        //TODO return type: ObjectiveCardPointMultiplier, add method functionality
    }
}
