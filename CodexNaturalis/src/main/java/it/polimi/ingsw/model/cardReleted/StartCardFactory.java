package it.polimi.ingsw.model.cardReleted;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.*;

/**
 * The concreteFactory for the StartCard type of Card
 */
public class StartCardFactory extends AbstractCardFactory<StartCard>{

    public StartCardFactory(String filePath) {
        super(filePath);
    }

    private final Queue<StartCard> deckBuilder = new LinkedList<>();
    /**
     * @return the QueueOfCards
     */
    @Override
    public Queue<StartCard> getCards() {
        JsonArray ResourceCards = getCardArray("StartingCard");
        for(int i=0; i<ResourceCards.size(); i++){
            JsonObject card = ResourceCards.get(i).getAsJsonObject();
            deckBuilder.add(new StartCard(getFrontCornerMap(card),getBackCornerMap(card),
                    getSetOfPermanentResource(card)));
        }
        return deckBuilder;
    }

    /**
     * This method use the toString define in the Resource/Special/WritingMaterial enum
     * @param card that is being build
     * @return a Map with the Collectable in the backCorner
     */
    private Map<CardCorner, Collectable> getBackCornerMap(JsonObject card){
        Map<CardCorner, Collectable> backCornerMap = new HashMap<>();
        JsonObject backCorners = card.getAsJsonObject("backCorners");
        //For each "element" in the backCornerMap object
        for (Map.Entry<String, JsonElement> element : backCorners.entrySet()) {
            String cornerKey = element.getKey().toUpperCase();
            String cornerValue = element.getValue().getAsString().toUpperCase();
            // Convert key and collectable, which are String, to their respective enum and put them in the map
            if (cornerValue.equals("EMPTY")){
                backCornerMap.put(CardCorner.valueOf(cornerKey), SpecialCollectable.valueOf(cornerValue));
            }else if (cornerValue.equals("QUILL") || cornerValue.equals("INKWELL") || cornerValue.equals("MANUSCRIPT")){
                backCornerMap.put(CardCorner.valueOf(cornerKey), WritingMaterial.valueOf(cornerValue));
            }else{
                backCornerMap.put(CardCorner.valueOf(cornerKey), Resource.valueOf(cornerValue));
            }
        }
        return backCornerMap;
    }

    /**
     * @param card that is being build
     * @return a SET of resources as the PermanentResource stored in the json file
     */
    private HashSet<Resource> getSetOfPermanentResource(JsonObject card){
        HashSet<Resource> setPermanentResource = new HashSet<>();
        JsonArray permanentResourceArray = card.getAsJsonArray("permanentResource");
        for(int i=0;i<permanentResourceArray.size();i++){
            setPermanentResource.add(Resource.valueOf(permanentResourceArray.get(i).getAsString()));
        }
        return  setPermanentResource;
    }
}
