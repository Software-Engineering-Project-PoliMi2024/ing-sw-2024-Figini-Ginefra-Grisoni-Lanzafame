package it.polimi.ingsw.model.cardReleted.cardFactories;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * The concreteFactory for the Resource type of Card
 */
public class ResourceCardFactory extends AbstractCardFactory<ResourceCard>{
    /**
     * The constructor of the class
     * @param inFile the path of the card.json file
     * @param outDirPath the path of the directory where the .bin file will be saved
     */
    public ResourceCardFactory(String inFile, String outDirPath) {
        super(inFile, outDirPath);
    }


    private final Queue<ResourceCard> deckBuilder = new LinkedList<>();
    /**
     * Retrieves a queue of Resource cards from JSON file
     * It calls StartCard() for building the actual card
     * @return A queue containing StartCard objects parsed from the JSON data.
     */
    @Override
    public Queue<ResourceCard> getCardsFromJson() {
        JsonArray ResourceCards = getCardArray("ResourcesCards");
        for(int i=0; i<ResourceCards.size(); i++){
            JsonObject card = ResourceCards.get(i).getAsJsonObject();
            deckBuilder.add(new ResourceCard(getId(card), getPermanentResource(card),
                    card.get("points").getAsInt(), getFrontCornerMap(card)));
        }
        return deckBuilder;
    }

    /**
     Retrieves a Queue of ResourceCard objects.
     If a binary file containing serialized GoldCard objects exists, it deserializes and returns the Queue from it.
     If the binary file does not exist, call getCardsFromJson()
     @return A Queue of GoldCard objects obtained either from existing binary file or serialized from JSON.
     @throws RuntimeException If an error occurs during file operations or deserialization.
     */
    @Override
    public Queue<ResourceCard> getCards() {
        String fileSerializedName = outDirPath + "resourceCards.bin";
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
}
