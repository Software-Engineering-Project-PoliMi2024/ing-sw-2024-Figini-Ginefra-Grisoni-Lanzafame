package it.polimi.ingsw.model.cardReleted.cardFactories;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Configs;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.model.cardReleted.cards.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.cards.StartCard;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CardFactoryTest{
    String fileJsonPath = Configs.CardResourcesFolderPath+"/"+ Configs.CardJSONFileName;
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileJsonPath);
    Gson gson = new Gson();
    @Test
    public void getStartCardsFromJson() {
        JsonArray ja = gson.fromJson(new InputStreamReader(inputStream), JsonObject.class).getAsJsonArray("StartingCard");

        StartCardFactory startCardFactory = new StartCardFactory(fileJsonPath, Configs.CardResourcesFolderPath);
        List<StartCard> startCards= new ArrayList<>(startCardFactory.getCardsFromJson());
        assert startCards.size() == ja.size(); // the number of cards in the json file is the same as the number of cards in the list


        for(int i = 0; i < startCards.size(); i++){
            // the id of the card in the list is the same as the id of the card in the json file
            assert startCards.get(i).getIdFront() == ja.get(i).getAsJsonObject().get("frontID").getAsInt();
            // the back id of the card in the list is the same as the back id of the card in the json file
            assert startCards.get(i).getIdBack() == ja.get(i).getAsJsonObject().get("backID").getAsInt();
        }
    }

    @Test
    public void getGoldCardsFromJson(){
        JsonArray ja = gson.fromJson(new InputStreamReader(inputStream), JsonObject.class).getAsJsonArray("GoldCards");

        GoldCardFactory goldCardFactory = new GoldCardFactory(fileJsonPath, Configs.CardResourcesFolderPath);
        List<GoldCard> goldCards = new ArrayList<>(goldCardFactory.getCardsFromJson());
        assert goldCards.size() == ja.size(); // the number of cards in the json file is the same as the number of cards in the list

        for(int i = 0; i < goldCards.size(); i++) {
            // the id of the card in the list is the same as the id of the card in the json file
            assert goldCards.get(i).getIdFront() == ja.get(i).getAsJsonObject().get("frontID").getAsInt();
            // the back id of the card in the list is the same as the back id of the card in the json file
            assert goldCards.get(i).getIdBack() == ja.get(i).getAsJsonObject().get("backID").getAsInt();
        }
    }

    @Test
    public void getObjectiveCardsFromJson(){
        JsonArray ja = gson.fromJson(new InputStreamReader(inputStream), JsonObject.class).getAsJsonArray("ObjectiveCard");

        ObjectiveCardFactory objectiveCardFactory = new ObjectiveCardFactory(fileJsonPath, Configs.CardResourcesFolderPath);
        List<ObjectiveCard> objectiveCards = new ArrayList<>(objectiveCardFactory.getCardsFromJson());
        assert objectiveCards.size() == ja.size(); // the number of cards in the json file is the same as the number of cards in the list

        for(int i = 0; i < objectiveCards.size(); i++) {
            // the id of the card in the list is the same as the id of the card in the json file
            assert objectiveCards.get(i).getIdFront() == ja.get(i).getAsJsonObject().get("frontID").getAsInt();
            // the back id of the card in the list is the same as the back id of the card in the json file
            assert objectiveCards.get(i).getIdBack() == ja.get(i).getAsJsonObject().get("backID").getAsInt();
        }
    }

    @Test
    public void getResourcesCardsFromJson(){
        JsonArray ja = gson.fromJson(new InputStreamReader(inputStream), JsonObject.class).getAsJsonArray("ResourcesCards");

        ResourceCardFactory resourcesCardFactory = new ResourceCardFactory(fileJsonPath, Configs.CardResourcesFolderPath);
        List<ResourceCard> resourcesCards = new ArrayList<>(resourcesCardFactory.getCardsFromJson());
        assert resourcesCards.size() == ja.size(); // the number of cards in the json file is the same as the number of cards in the list

        for(int i = 0; i < resourcesCards.size(); i++) {
            // the id of the card in the list is the same as the id of the card in the json file
            assert resourcesCards.get(i).getIdFront() == ja.get(i).getAsJsonObject().get("frontID").getAsInt();
            // the back id of the card in the list is the same as the back id of the card in the json file
            assert resourcesCards.get(i).getIdBack() == ja.get(i).getAsJsonObject().get("backID").getAsInt();
        }
    }
}