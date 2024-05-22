package it.polimi.ingsw.model.cardReleted.cards;

import com.sun.source.tree.AssertTree;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.playerReleted.Codex;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {

    @Test
    void canBePlaced_ItShouldBePlaceable() {
        GoldCard card = new GoldCard(1, 1, 1, Map.of(
                Resource.ANIMAL, 2,
                Resource.INSECT, 2
        ), null, null, Resource.PLANT);

        Codex codex = new Codex();
        codex.setEarnedCollectables(Resource.ANIMAL, 3);
        codex.setEarnedCollectables(Resource.INSECT, 2);
        codex.setEarnedCollectables(Resource.PLANT, 1);

        assertTrue(card.canBePlaced(codex));
    }

    @Test
    void canBePlaced_ItShouldNotBePlaceable() {
        GoldCard card = new GoldCard(1, 1, 1, Map.of(
                Resource.ANIMAL, 2,
                Resource.INSECT, 2
        ), null, null, Resource.PLANT);

        Codex codex = new Codex();
        codex.setEarnedCollectables(Resource.INSECT, 1);
        codex.setEarnedCollectables(Resource.PLANT, 1);

        assertFalse(card.canBePlaced(codex));
    }
}