package it.polimi.ingsw.model.cardReleted;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.CoveredCornersCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.GoldCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.WritingMaterialsCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * The concreteFactory for the Gold type of Card
 */
public class GoldCardFactory extends AbstractCardFactory<GoldCard>{

    /**
     * The constructor of the class
     * @param filePath the path of the card.json file
     */
    public GoldCardFactory(String filePath) {
        super(filePath);
    }

    /**
     * Retrieves a Queue of GoldCard objects.
     * If a binary file containing serialized GoldCard objects exists, it deserializes and returns the Queue from it.
     * If the binary file does not exist, call getCardsFromJson()
     *
     * @return A Queue of GoldCard objects obtained either from existing binary file or serialized from JSON.
     * @throws RuntimeException If an error occurs during file operations or deserialization.
     */
    @Override
    public Queue<GoldCard> getCards() {
        String fileSerializedName = "CodexNaturalis/cards/goldCards.bin";
        FileInputStream file;
        try { //check if the .bin file exist
            file = new FileInputStream(fileSerializedName);
            return deserializeQueue(file);
        } catch (FileNotFoundException e) { //the .bin file doesn't exist
            serializeQueue(fileSerializedName, getCardsFromJson()); //create the .bin file
            try {
                file = new FileInputStream(fileSerializedName);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            return deserializeQueue(file);
        }
    }

    private final Queue<GoldCard> deckBuilder = new LinkedList<>();

    /**
     * Retrieves a queue of Gold cards from JSON file
     * It calls StartCard() for building the actual card
     * @return A queue containing StartCard objects parsed from the JSON data.
     */
    public Queue<GoldCard> getCardsFromJson() {
        JsonArray GoldCards = getCardArray("GoldCards");
        for(int i =0; i<GoldCards.size(); i++){
            JsonObject card = GoldCards.get(i).getAsJsonObject();
            getPointMultiplier(card);
            deckBuilder.add(new GoldCard(card.get("points").getAsInt(), getRequirmentMap(card),
                    getPointMultiplier(card),getFrontCornerMap(card),getPermanentResource(card)));
        }
        return deckBuilder;
    }
    /**
     * This method use the toString define in the Resource enum
     * @param card that is being build
     * @return a Map with the requirement needed to place the card
     */
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

    /**
     * @param card that is being build
     * @return a pointer to the correct Multiplier
     */
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
}

