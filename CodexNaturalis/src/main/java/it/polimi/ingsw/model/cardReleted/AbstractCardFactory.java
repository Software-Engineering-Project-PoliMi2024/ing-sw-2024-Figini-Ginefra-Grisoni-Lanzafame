package it.polimi.ingsw.model.cardReleted;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * The interface implemented by all concrete factory
 * @param <Element> is the "type" of Card that will be produced
 */
public abstract class AbstractCardFactory<Element> {
    public abstract Queue<Element> getCards();

    /**
     * @param cardType the name of the card type (the array name) in the JSON (
     * @return the array made of Resources Cards
     * @throws RuntimeException if cards.json is not present
     */
    protected JsonArray getCardArray(String cardType){
        final Gson gson = new Gson();
        FileReader fileReader;
        try {
            fileReader = new FileReader("C:\\Polimi\\3rd_Year\\2ndSem\\ProgettoSoftEng\\cards.json");
            JsonObject jsonObject = gson.fromJson(fileReader, JsonObject.class);
            return  jsonObject.getAsJsonArray(cardType);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method use the toString define in the Resource/Special/WritingMaterial enum
     * @param card that is being build
     * @return a Map with the Collectable in the frontCorner
     */
    protected Map<CardCorner, Collectable> getFrontCornerMap(JsonObject card){
        Map<CardCorner, Collectable> frontCornerMap = new HashMap<>();
        JsonObject frontCorner = card.getAsJsonObject("corners");
        //For each "element" in the frontCorner object
        for (Map.Entry<String, JsonElement> element : frontCorner.entrySet()) {
            String cornerKey = element.getKey().toUpperCase();
            String cornerValue = element.getValue().getAsString().toUpperCase();
            // Convert key and collectable, which are String, to their respective enum and put them in the map
            if (cornerValue.equals("EMPTY")){
                frontCornerMap.put(CardCorner.valueOf(cornerKey), SpecialCollectable.valueOf(cornerValue));
            }else if (cornerValue.equals("QUILL") || cornerValue.equals("INKWELL") || cornerValue.equals("MANUSCRIPT")){
                frontCornerMap.put(CardCorner.valueOf(cornerKey), WritingMaterial.valueOf(cornerValue));
            }else{
                frontCornerMap.put(CardCorner.valueOf(cornerKey), Resource.valueOf(cornerValue));
            }
        }
        return frontCornerMap;
    }

    /**
     * @param card that is being build
     * @return Resource as the PermanentResource stored in the json file
     */
    protected Resource getPermanentResource(JsonObject card){
        return  Resource.valueOf(card.get("permanentResource").getAsString());
    }
}
