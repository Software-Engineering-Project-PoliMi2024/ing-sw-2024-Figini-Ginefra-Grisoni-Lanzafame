package it.polimi.ingsw.view.TUI.cardDrawing;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardFace;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardMuseumFactoryTest {
    @Test
    void testCardMuseumFactory_Generation() {
        CardMuseum museum = new CardMuseumFactory("./cards/", true).getCardMuseum();
        assertNotNull(museum);

        for(int key = 1; key <= 102; key++){
            System.out.println("Card ID: " + key);
            System.out.println(museum.get(key).get(CardFace.FRONT).toString());

            assertNotNull(museum.get(key).get(CardFace.FRONT));
            assertNotNull(museum.get(key).get(CardFace.BACK));
        }
    }
}