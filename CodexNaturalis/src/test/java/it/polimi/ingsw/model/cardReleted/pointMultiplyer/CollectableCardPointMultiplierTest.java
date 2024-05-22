package it.polimi.ingsw.model.cardReleted.pointMultiplyer;

import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import it.polimi.ingsw.model.playerReleted.Codex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CollectableCardPointMultiplierTest {

    private Codex codex;
    private CollectableCardPointMultiplier multiplier;

    @BeforeEach
    void setUp() {
        codex = new Codex();
        multiplier = new CollectableCardPointMultiplier(Map.of(
                Resource.PLANT, 2,
                Resource.ANIMAL, 3,
                WritingMaterial.INKWELL, 1
        ));
    }

    @Test
    void getMultiplier_correctPointsX2() {
        codex.setEarnedCollectables(Resource.PLANT, 4);
        codex.setEarnedCollectables(Resource.ANIMAL, 6);
        codex.setEarnedCollectables(WritingMaterial.INKWELL, 2);

        assertEquals(2, multiplier.getMultiplier(codex));
    }

    @Test
    void getMultiplier_correctPointsX0() {
        codex.setEarnedCollectables(Resource.PLANT, 4);
        codex.setEarnedCollectables(Resource.ANIMAL, 6);
        codex.setEarnedCollectables(WritingMaterial.INKWELL, 0);

        assertEquals(0, multiplier.getMultiplier(codex));
    }
}