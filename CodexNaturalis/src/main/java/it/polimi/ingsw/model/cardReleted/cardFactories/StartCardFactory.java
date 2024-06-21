package it.polimi.ingsw.model.cardReleted.cardFactories;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Configs;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.*;

import java.io.*;
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

    /**
     Retrieves a Queue of StarCard objects.
     If a binary file containing serialized GoldCard objects exists, it deserializes and returns the Queue from it.
     If the binary file does not exist, call getCardsFromJson()
     @return A Queue of GoldCard objects obtained either from existing binary file or serialized from JSON.
     @throws RuntimeException If an error occurs during file operations or deserialization.
     */
    @Override
    public Queue<StartCard> getCards(String binFileName) {
        String filePath = outDirPath + binFileName;
        File fileSerialized = new File(filePath);
        if (!fileSerialized.exists()) {
            serializeQueue(filePath, getCardsFromJson()); //create the .bin file
        }
        return deserializeQueue(fileSerialized);
    }

    @SuppressWarnings("unchecked")
    public Queue<StartCard> deserializeQueue(File binFile){
        FileInputStream fileInputStream;
        try{
            fileInputStream = new FileInputStream(binFile);
        }catch (FileNotFoundException e){
            //This should never happen. If the file is not found, it should be created by the getCards() method before calling this one
            throw new RuntimeException("Error in creating the file");
        }
        ObjectInputStream objectInputStream;
        Queue<StartCard> queue;
        try {
            objectInputStream = new ObjectInputStream(fileInputStream);
            queue = (Queue<StartCard>) objectInputStream.readObject();
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
                queue = this.getCards(Configs.startCardBinFileName);
            }
        }
        return queue;
    }


    /**
     * Retrieves a queue of Start cards from JSON file
     * It calls StartCard() for building the actual card
     * @return A queue containing StartCard objects parsed from the JSON data.
     */
    @Override
    public Queue<StartCard> getCardsFromJson() {
        Queue<StartCard> deckBuilder = new LinkedList<>();
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
