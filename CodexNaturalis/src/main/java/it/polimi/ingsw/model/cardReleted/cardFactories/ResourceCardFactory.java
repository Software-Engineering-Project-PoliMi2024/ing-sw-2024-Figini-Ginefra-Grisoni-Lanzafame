package it.polimi.ingsw.model.cardReleted.cardFactories;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Configs;
import it.polimi.ingsw.OSRelated;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;

import java.io.*;
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
    /**
     * Retrieves a queue of Resource cards from JSON file
     * It calls StartCard() for building the actual card
     * @return A queue containing StartCard objects parsed from the JSON data.
     */
    @Override
    public Queue<ResourceCard> getCardsFromJson() {
        Queue<ResourceCard> deckBuilder = new LinkedList<>();
        JsonArray ResourceCards = getCardArray("ResourcesCards");
        for(int i=0; i<ResourceCards.size(); i++){
            JsonObject card = ResourceCards.get(i).getAsJsonObject();
            deckBuilder.add(new ResourceCard(getIdFront(card), getIdBack(card), getPermanentResource(card),
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
    public Queue<ResourceCard> getCards(String binFileName) {
        String filePath = OSRelated.cardFolderDataPath + binFileName;
        File fileSerialized = new File(filePath);
        if (!fileSerialized.exists()) {
            serializeQueue(filePath, getCardsFromJson()); //create the .bin file
        }
        return deserializeQueue(fileSerialized);
    }

    @SuppressWarnings("unchecked")
    public Queue<ResourceCard> deserializeQueue(File binFile){
        FileInputStream fileInputStream;
        try{
            fileInputStream = new FileInputStream(binFile);
        }catch (FileNotFoundException e){
            //This should never happen. If the file is not found, it should be created by the getCards() method before calling this one
            throw new RuntimeException("Error in creating the file");
        }
        ObjectInputStream objectInputStream;
        Queue<ResourceCard> queue;
        try {
            objectInputStream = new ObjectInputStream(fileInputStream);
            queue = (Queue<ResourceCard>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            try {
                fileInputStream.close();
            } catch (IOException ex) {
                throw new RuntimeException("An error occurred while closing the fileStream");
            }
            if(!binFile.delete()){
                throw new RuntimeException("An error occurred while deleting file");
            }else{
                System.out.println("Corrupted or not up-to-date " + binFile.getPath() + " file deleted");
                queue = this.getCards(Configs.resourceCardBinFileName);
            }
        }
        return queue;
    }
}
