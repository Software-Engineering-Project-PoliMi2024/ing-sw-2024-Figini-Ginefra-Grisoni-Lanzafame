package it.polimi.ingsw.model.cardReleted;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * The concreteFactory for the Resource type of Card
 */
public class GoldCardFactory extends AbstractCardFactory<GoldCard>{
    private final Queue<GoldCard> deckBuilder = new LinkedList<>();
    @Override
    public Queue<GoldCard> getCards() {
        JsonArray GoldCards = getCardArray("GoldCards");
        for(int i =0; i<GoldCards.size(); i++){
            JsonObject card = GoldCards.get(i).getAsJsonObject();
            getPointMultiplier(card);
            deckBuilder.add(new GoldCard(card.get("points").getAsInt(), getRequirmentMap(card),
                    getPointMultiplier(card),getFrontCornerMap(card),getPermanentResource(card)));
        }
        return deckBuilder;
    }

    private Map<Resource, Integer> getRequirmentMap(JsonObject card){
        Map<Resource, Integer> requirmentMap = new HashMap<>();
        JsonObject requirementsList = card.getAsJsonObject("requirements");
        //For each "element" get the key in UpperCase and use the enum toString to convert it
        for (Map.Entry<String, JsonElement> element : requirementsList.entrySet()){
            requirmentMap.put(Resource.valueOf(element.getKey().toUpperCase()),
                    element.getValue().getAsInt());
        }
        return requirmentMap;
    }

    private GoldCardPointMultiplier getPointMultiplier(JsonObject card){
        String multiplierType = card.get("multiplier").getAsString().toUpperCase();
        if(multiplierType.isEmpty()){
            return null;
        }else if(multiplierType.equals("CORNER")){
            return new CoveredCornersCardPointMultiplier();
        }else{
            return new WritingMaterialsCardPointMultiplier(WritingMaterial.valueOf(multiplierType));
        }
    }
    public static void main(String[] arg){
        GoldCardFactory factory = new GoldCardFactory();
        factory.getCards();
    }
}


