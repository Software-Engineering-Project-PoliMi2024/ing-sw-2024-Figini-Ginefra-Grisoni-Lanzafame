package it.polimi.ingsw.utils.cardFactories;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Configs;
import it.polimi.ingsw.model.cardReleted.cards.GoldCard;
import it.polimi.ingsw.utils.cardFactories.AbstractCardFactory;
import it.polimi.ingsw.utils.cardFactories.GoldCardFactory;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class AbstractCardFactoryTest {
    @Test
    public void getCardArrayShouldNotBeNullOrEmptyForAllCardTypes() {
        //getCardArray is a common method between all concrete factory,
        //the concrete factory that is chosen does not matter
        AbstractCardFactory<GoldCard> abstractCardFactory = new GoldCardFactory(Configs.CardResourcesFolderPath +Configs.CardJSONFileName, Configs.CardResourcesFolderPath);
        String[] CardTypes = {"GoldCards", "StartingCard", "ObjectiveCard", "ResourcesCards"};
        for(String s : CardTypes){
            JsonArray jsonArrayTest = abstractCardFactory.getCardArray(s);
            assertNotNull("JSON Array is not null", jsonArrayTest);
            assertFalse("JSON Array is not empty", jsonArrayTest.isEmpty());
        }
    }

    @Test
    public void getFrontCornerMapNotNull(){
        //getCardArray is a common method between all concrete factory,
        //the concrete factory that is chosen does not matter
        AbstractCardFactory<GoldCard> abstractCardFactory = new GoldCardFactory(Configs.CardResourcesFolderPath +Configs.CardJSONFileName, Configs.CardResourcesFolderPath);
        //ObjectiveCard have no FrontCorner
        String[] CardTypes = {"GoldCards", "StartingCard", "ResourcesCards"};
        for(String s : CardTypes){
            JsonArray cardArray = abstractCardFactory.getCardArray(s);
            for(int i =0; i<cardArray.size(); i++){
                JsonObject card = cardArray.get(i).getAsJsonObject();
                assertNotNull("Front map should not be null", abstractCardFactory.getFrontCornerMap(card));
            }
        }
    }
}