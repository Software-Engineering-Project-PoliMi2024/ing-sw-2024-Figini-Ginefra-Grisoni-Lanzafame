package it.polimi.ingsw.model.cardReleted;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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
            deckBuilder.add(new ObjectiveCard(card.get("points").getAsInt(), getPointMultiplier(card)));
        }
        return deckBuilder;
    }

    /**
     * This method use the toString method of both WritingMaterial and Resource enum
     * @param card that is being build
     * @return a pointer to the correct Multiplier
     */
    private ObjectiveCardPointMultiplier getPointMultiplier(JsonObject card){
        if(card.has("multiplierD")){
            JsonObject multiplierD = card.getAsJsonObject("multiplierD");
            return new DiagonalCardPointMultiplier(multiplierD.get("upwards").getAsBoolean(),
                    Resource.valueOf(multiplierD.get("resource").getAsString().toUpperCase()));
        }else if(card.has("multiplierL")){
            JsonObject multiplierL = card.getAsJsonObject("multiplierL");
            return new LCardPointMultiplier(CardCorner.valueOf(multiplierL.get("corner").getAsString().toUpperCase()),
                    Resource.valueOf(multiplierL.get("singleResource").getAsString().toUpperCase()),
                    Resource.valueOf(multiplierL.get("multiResource").getAsString().toUpperCase()));
        }else{ //multiplierCollectable case
            JsonObject multiplierL = card.getAsJsonObject("multiplierCollectable");
            HashMap<Collectable, Integer> collectableMap = new HashMap<>();
            //For each "element" get the key in UpperCase and use the enum toString to convert it
            for (Map.Entry<String, JsonElement> element : multiplierL.entrySet()){
                String key = element.getKey().toUpperCase();
                if (key.equals("QUILL") || key.equals("INKWELL") || key.equals("MANUSCRIPT")){
                    collectableMap.put(WritingMaterial.valueOf(key),
                            element.getValue().getAsInt());
                }else{
                    collectableMap.put(Resource.valueOf(key),
                            element.getValue().getAsInt());
                }
            }
            return new CollectableCardPointMultiplier(collectableMap);
        }
    }
}
