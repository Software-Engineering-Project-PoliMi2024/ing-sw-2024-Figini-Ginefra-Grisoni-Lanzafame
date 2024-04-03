package it.polimi.ingsw.model.cardReleted;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.*;

/**
 * The concreteFactory for the Resource type of Card
 */
public class ResourceCardFactory extends AbstractCardFactory<ResourceCard>{

    public ResourceCardFactory(String filePath) {
        super(filePath);
    }


    private final Queue<ResourceCard> deckBuilder = new LinkedList<>();
    /**
     * @return the QueueOfCards
     */
    @Override
    public Queue<ResourceCard> getCards() {
        JsonArray ResourceCards = getCardArray("ResourcesCards");
        for(int i=0; i<ResourceCards.size(); i++){
            JsonObject card = ResourceCards.get(i).getAsJsonObject();
            deckBuilder.add(new ResourceCard(getPermanentResource(card),
                    card.get("points").getAsInt(), getFrontCornerMap(card)));
        }
        return deckBuilder;
    }
}
