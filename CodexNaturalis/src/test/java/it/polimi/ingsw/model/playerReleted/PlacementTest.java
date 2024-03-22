package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlacementTest {

    @Test
    void equals_inputSamePlacement_shouldReturnTrue() {
        Map<Collectable, Integer> consequences1 = new HashMap<>();
        consequences1.put(Resource.ANIMAL, 2);
        consequences1.put(Resource.INSECT, 5);

        Map<Collectable, Integer> consequences2 = new HashMap<>();
        consequences2.put(Resource.ANIMAL, 2);
        consequences2.put(Resource.INSECT, 5);


        Placement placement = new Placement(new Position(1, 1), new ResourceCard(), CardFace.FRONT);
        Placement placement2 = new Placement(new Position(1, 1), new ResourceCard(), CardFace.FRONT);
        assertEquals(placement, placement2);
    }

    @Test
    void equals_inputDifferentConsequences_shouldReturnFalse() {
        Map<Collectable, Integer> consequences1 = new HashMap<>();
        consequences1.put(Resource.ANIMAL, 2);
        consequences1.put(Resource.INSECT, 5);

        Map<Collectable, Integer> consequences2 = new HashMap<>();
        consequences2.put(Resource.ANIMAL, 2);
        consequences2.put(Resource.INSECT, 7);

        Placement placement = new Placement(new Position(1, 1), new ResourceCard(), CardFace.FRONT);
        Placement placement2 = new Placement(new Position(1, 1), new ResourceCard(), CardFace.FRONT);
        assertNotEquals(placement, placement2);
    }
}