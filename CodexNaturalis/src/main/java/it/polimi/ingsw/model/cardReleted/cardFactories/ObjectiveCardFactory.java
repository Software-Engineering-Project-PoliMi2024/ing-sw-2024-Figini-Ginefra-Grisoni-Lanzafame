package it.polimi.ingsw.model.cardReleted.cardFactories;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.CollectableCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.DiagonalCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.LCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.ObjectiveCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import it.polimi.ingsw.model.utilities.Pair;

import java.io.*;
import java.util.*;

/**
 * The concreteFactory for the Objective type of Card
 */
public class ObjectiveCardFactory extends AbstractCardFactory<ObjectiveCard>{

    /**
     * The constructor of the class
     * @param inFile the path of the card.json file
     * @param outDirPath the path of the directory where the .bin file will be saved
     */
    public ObjectiveCardFactory(String inFile, String outDirPath) {
        super(inFile, outDirPath);
    }


    /**
     Retrieves a Queue of ObjectiveCard objects.
     If a binary file containing serialized Objective objects exists, it deserializes and returns the Queue from it.
     If the binary file does not exist, create a new one by call getCardsFromJson()
     @return A Queue of ObjectiveCards objects obtained either from existing binary file or serialized from JSON.
     @throws RuntimeException If an error occurs during file operations or deserialization.
     */
    @Override
    public Queue<ObjectiveCard> getCards() {
        String filePath = outDirPath + "objectiveCards.bin";
        File fileSerialized = new File(filePath);
        if(fileSerialized.exists()){
            return deserializeQueue(fileSerialized);
        }else{
            serializeQueue(filePath, getCardsFromJson()); //create the .bin file
            return deserializeQueue(fileSerialized);
        }
    }

    /**
     * Deserialize the ObjectiveCard from the .bin file. If the file is corrupted or not up-to-date, it will be deleted
     * and the getCards() method will be called to create a new one
     * @param binFile the file that contains the serialized ObjectiveCard
     * @return the Queue of ObjectiveCard
     */
    @SuppressWarnings("unchecked")
    public Queue<ObjectiveCard> deserializeQueue(File binFile){
        FileInputStream fileInputStream;
        try{
            fileInputStream = new FileInputStream(binFile);
        }catch (FileNotFoundException e){
            //This should never happen. If the file is not found, it should be created by the getCards() method before calling this one
            throw new RuntimeException("Error in creating the file");
        }
        ObjectInputStream objectInputStream;
        Queue<ObjectiveCard> queue;
        try {
            objectInputStream = new ObjectInputStream(fileInputStream);
            queue = (Queue<ObjectiveCard>) objectInputStream.readObject();
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
                queue = this.getCards();
            }
        }
        return queue;
    }

    /**
     * Retrieves a queue of Objective cards from JSON file
     * It calls StartCard() for building the actual card
     * @return A queue containing StartCard objects parsed from the JSON data.
     */
    @Override
    public Queue<ObjectiveCard> getCardsFromJson() {
        Queue<ObjectiveCard> deckBuilder = new LinkedList<>();
        JsonArray ResourceCards = getCardArray("ObjectiveCard");
        for(int i=0; i<ResourceCards.size(); i++) {
            JsonObject card = ResourceCards.get(i).getAsJsonObject();
            deckBuilder.add(new ObjectiveCard(getIdFront(card), getIdBack(card), card.get("points").getAsInt(), getPointMultiplier(card)));
        }
        return deckBuilder;
    }

    /**
     * Retrieves a queue of Objective cards from JSON file
     * @return A queue of pairs containing ObjectiveCard and its multiplier
     */
    public Queue<Pair<ObjectiveCard, DiagonalCardPointMultiplier>> getCardsWithDiagonalMultiplier(){
        Queue<Pair<ObjectiveCard, DiagonalCardPointMultiplier>> deckBuilder = new LinkedList<>();
        JsonArray ResourceCards = getCardArray("ObjectiveCard");
        for(int i=0; i<ResourceCards.size(); i++) {
            JsonObject card = ResourceCards.get(i).getAsJsonObject();
            if(getDiagonalMultiplier(card) != null){
                deckBuilder.add(new Pair(new ObjectiveCard(getIdFront(card), getIdBack(card), card.get("points").getAsInt(), getPointMultiplier(card)), getDiagonalMultiplier(card)));
            }
        }
        return deckBuilder;

    }

    /**
     * Retrieves a queue of Objective cards from JSON file
     * @return A queue of pairs containing ObjectiveCard and its multiplier
     */
    public Queue<Pair<ObjectiveCard, LCardPointMultiplier>> getCardsWithLMultiplier(){
        Queue<Pair<ObjectiveCard, LCardPointMultiplier>> deckBuilder = new LinkedList<>();
        JsonArray ResourceCards = getCardArray("ObjectiveCard");
        for(int i=0; i<ResourceCards.size(); i++) {
            JsonObject card = ResourceCards.get(i).getAsJsonObject();
            if(getLMultiplier(card) != null){
                deckBuilder.add(new Pair(new ObjectiveCard(getIdFront(card), getIdBack(card), card.get("points").getAsInt(), getPointMultiplier(card)), getLMultiplier(card)));
            }
        }
        return deckBuilder;
    }

    /**
     * Retrieves a queue of Objective cards from JSON file
     * @return A queue of pairs containing ObjectiveCard and its multiplier
     */
    public Queue<Pair<ObjectiveCard, CollectableCardPointMultiplier>> getCardsWithCollectableMultiplier(){
        Queue<Pair<ObjectiveCard, CollectableCardPointMultiplier>> deckBuilder = new LinkedList<>();
        JsonArray ResourceCards = getCardArray("ObjectiveCard");
        for(int i=0; i<ResourceCards.size(); i++) {
            JsonObject card = ResourceCards.get(i).getAsJsonObject();
            if(getCollectableMultiplier(card) != null){
                deckBuilder.add(new Pair(new ObjectiveCard(getIdFront(card), getIdBack(card), card.get("points").getAsInt(), getPointMultiplier(card)), getCollectableMultiplier(card)));
            }
        }
        return deckBuilder;
    }

    private CollectableCardPointMultiplier getCollectableMultiplier(JsonObject card){
        if(!card.has("multiplierCollectable")){
            return null;
        }

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

    /**
     * Retrieves the DiagonalCardPointMultiplier from the card
     * @param card that is being build
     * @return the DiagonalCardPointMultiplier
     */
    private DiagonalCardPointMultiplier getDiagonalMultiplier(JsonObject card){
        if(!card.has("multiplierD")){
            return null;
        }

        JsonObject multiplierD = card.getAsJsonObject("multiplierD");
        return new DiagonalCardPointMultiplier(multiplierD.get("upwards").getAsString().equals("1"),
                Resource.valueOf(multiplierD.get("resource").getAsString().toUpperCase()));
    }

    /**
     * Retrieves the LCardPointMultiplier from the card
     * @param card that is being build
     * @return the LCardPointMultiplier
     */
    private LCardPointMultiplier getLMultiplier(JsonObject card){
        if(!card.has("multiplierL")){
            return null;
        }

        JsonObject multiplierL = card.getAsJsonObject("multiplierL");
        return new LCardPointMultiplier(CardCorner.valueOf(multiplierL.get("corner").getAsString().toUpperCase()),
                Resource.valueOf(multiplierL.get("singleResource").getAsString().toUpperCase()),
                Resource.valueOf(multiplierL.get("multiResource").getAsString().toUpperCase()));
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
            return this.getCollectableMultiplier(card);
        }
    }
}
