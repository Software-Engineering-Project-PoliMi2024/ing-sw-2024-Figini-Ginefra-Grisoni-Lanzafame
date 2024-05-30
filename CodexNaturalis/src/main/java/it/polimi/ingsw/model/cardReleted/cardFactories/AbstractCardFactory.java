package it.polimi.ingsw.model.cardReleted.cardFactories;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Configs;
import it.polimi.ingsw.Server;
import it.polimi.ingsw.model.cardReleted.utilityEnums.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * The interface implemented by all concrete factory
 * @param <Element> is the "type" of Card that will be produced
 */
public abstract class AbstractCardFactory<Element> {
    public abstract Queue<Element> getCards(String binFileName);

    public abstract Queue<Element> getCardsFromJson();
    private final String inFileFromResources;
    protected final String outDirPath;

    /**
     * The constructor of the class
     * @param inFileFromResources the path of the card.json file in the resources folder
     * @param outDirPath the path of the directory where the .bin file will be saved
     */
    public AbstractCardFactory(String inFileFromResources, String outDirPath) {
        this.inFileFromResources = inFileFromResources;
        this.outDirPath = outDirPath;
    }

    /**
     * Read from the Json File the Card Array
     * @param cardType the name of the card type (the array name) in the JSON (
     * @return the array made of Resources Cards
     * @throws RuntimeException if cards.json is not present
     */
    protected JsonArray getCardArray(String cardType){
        //TODO change file path
        final Gson gson = new Gson();
        InputStream inputStream = Server.class.getClassLoader().getResourceAsStream(inFileFromResources);
        if(inputStream == null){
            throw new RuntimeException("File " + Configs.CardFile + " not found");
        }else{
            return gson.fromJson(new InputStreamReader(inputStream), JsonObject.class).getAsJsonArray(cardType);
        }
    }

    /**
     * Read the JsonObject corners from the Json File
     * This method use the toString define in the Resource/Special/WritingMaterial Enum
     * @param card that is being build
     * @return a Map with the Collectable in frontCorner
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

    protected int getIdFront(JsonObject card){
        return card.get("frontID").getAsInt();
    }

    protected int getIdBack(JsonObject card){
        return card.get("backID").getAsInt();
    }

    /**
     * @param card that is being build
     * @return Resource as the PermanentResource stored in the json file
     */
    protected Resource getPermanentResource(JsonObject card){
        return  Resource.valueOf(card.get("permanentResource").getAsString());
    }

    /**
     * serialize a Queue of card and create the appropriate serialized file
     * @param filePath the path including the name of the final file
     * @param queue the queue of card that needs to be serialized
     * @throws RuntimeException if an error occurs during the opening/writing
     */
    public void serializeQueue(String filePath, Queue<Element> queue){
        FileOutputStream fileOut;
        ObjectOutputStream objOut;
        try {
            fileOut = new FileOutputStream(filePath);
            objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(queue);
            objOut.close();
            fileOut.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract Queue<Element> deserializeQueue(File filename);
}


