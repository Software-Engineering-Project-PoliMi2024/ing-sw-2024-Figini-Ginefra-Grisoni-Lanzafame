package it.polimi.ingsw.model.cardReleted.cardFactories;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Configs;
import it.polimi.ingsw.OSRelated;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.CoveredCornersCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.GoldCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.pointMultiplyer.WritingMaterialsCardPointMultiplier;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;

import java.io.*;
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
     * @param inFile the path of the card.json file
     * @param outDirPath the path of the directory where the .bin file will be saved
     */
    public GoldCardFactory(String inFile, String outDirPath) {
        super(inFile, outDirPath);
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
    public Queue<GoldCard> getCards(String binFileName) {
        String filePath = outDirPath + binFileName;
        File fileSerialized = new File(filePath);
        if (!fileSerialized.exists()) {
            serializeQueue(filePath, getCardsFromJson()); //create the .bin file
        }
        return deserializeQueue(fileSerialized);
    }

    @SuppressWarnings("unchecked")
    public Queue<GoldCard> deserializeQueue(File binFile){
        FileInputStream fileInputStream;
        try{
            fileInputStream = new FileInputStream(binFile);
        }catch (FileNotFoundException e){
            //This should never happen. If the file is not found, it should be created by the getCards() method before calling this one
            throw new RuntimeException("Error in creating the file");
        }
        ObjectInputStream objectInputStream;
        Queue<GoldCard> queue;
        try {
            objectInputStream = new ObjectInputStream(fileInputStream);
            queue = (Queue<GoldCard>) objectInputStream.readObject();
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
                queue = this.getCards(Configs.goldCardBinFileName);
            }
        }
        return queue;
    }

    /**
     * Retrieves a queue of Gold cards from JSON file
     * It calls StartCard() for building the actual card
     * @return A queue containing StartCard objects parsed from the JSON data.
     */
    public Queue<GoldCard> getCardsFromJson() {
        Queue<GoldCard> deckBuilder = new LinkedList<>();
        JsonArray GoldCards = getCardArray("GoldCards");
        for(int i =0; i<GoldCards.size(); i++){
            JsonObject card = GoldCards.get(i).getAsJsonObject();
            getPointMultiplier(card);
            deckBuilder.add(new GoldCard(getIdFront(card), getIdBack(card), card.get("points").getAsInt(), getRequirmentMap(card),
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

