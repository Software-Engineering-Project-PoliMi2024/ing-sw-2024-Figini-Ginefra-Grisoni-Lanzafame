package it.polimi.ingsw.model.cardReleted.cardFactories;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * The concreteFactory for the StartCard type of Card
 */
public class StartCardFactory extends AbstractCardFactory<StartCard>{
    /**
     * The constructor of the class
     * @param inFile the path of the card.json file
     * @param outDirPath the path of the directory where the .bin file will be saved
     */
    public StartCardFactory(String inFile, String outDirPath) {
        super(inFile, outDirPath);
    }

    private final Queue<StartCard> deckBuilder = new LinkedList<>();
    /**
     Retrieves a Queue of StarCard objects.
     If a binary file containing serialized GoldCard objects exists, it deserializes and returns the Queue from it.
     If the binary file does not exist, call getCardsFromJson()
     @return A Queue of GoldCard objects obtained either from existing binary file or serialized from JSON.
     @throws RuntimeException If an error occurs during file operations or deserialization.
     */
    @Override
    public Queue<StartCard> getCards() {
        String fileSerializedName = outDirPath + "startCards.bin";
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

    /**
     * Retrieves a queue of Start cards from JSON file
     * It calls StartCard() for building the actual card
     * @return A queue containing StartCard objects parsed from the JSON data.
     */
    @Override
    public Queue<StartCard> getCardsFromJson() {
        JsonArray ResourceCards = getCardArray("StartingCard");
        for(int i=0; i<ResourceCards.size(); i++){
            JsonObject card = ResourceCards.get(i).getAsJsonObject();
            deckBuilder.add(new StartCard(getIdFront(card), getIdBack(card), getFrontCornerMap(card),getBackCornerMap(card),
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
