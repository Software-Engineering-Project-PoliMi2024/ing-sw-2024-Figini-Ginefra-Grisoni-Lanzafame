package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.cards.ResourceCard;
import it.polimi.ingsw.model.cardReleted.utilityEnums.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CodexTest {

    @Test
    void setEarnedCollectables() {
        Codex codex = new Codex();
        assertThrows(IllegalArgumentException.class, () -> {codex.setEarnedCollectables(SpecialCollectable.EMPTY, -1);});

    }
    @Test
    void getPlacementAt(){
        Codex codex = new Codex();
        Position pos = new Position(0, 0);
        assertNull(codex.getPlacementAt(pos));
    }

    @Test
    void calculateConsequences() {
        Codex codex = new Codex();
        ResourceCard card = new ResourceCard(1, 1, Resource.FUNGI, 0, Map.of(
                CardCorner.TR, Resource.ANIMAL,
                CardCorner.TL, Resource.FUNGI,
                CardCorner.BR, Resource.PLANT,
                CardCorner.BL, Resource.INSECT
        ));

        ResourceCard card1 = new ResourceCard(1, 1, Resource.PLANT, 0, Map.of(
                CardCorner.TR, Resource.ANIMAL,
                CardCorner.TL, Resource.FUNGI,
                CardCorner.BR, Resource.PLANT,
                CardCorner.BL, Resource.INSECT
        ));

        codex.playCard(new Placement(new Position(0, 0), card, CardFace.FRONT));
        codex.playCard(new Placement(new Position(1, 1), card1, CardFace.FRONT));
        Map<Collectable, Integer> expected = Map.of(
                Resource.ANIMAL, 1,
                Resource.FUNGI, 3,
                Resource.PLANT, 3,
                Resource.INSECT, 2,
                WritingMaterial.INKWELL, 0,
                WritingMaterial.MANUSCRIPT, 0,
                WritingMaterial.QUILL, 0
        );

        expected.forEach((collectable, integer) -> {
            System.out.println(collectable + " " + integer);
            assertEquals(integer, codex.getEarnedCollectables().get(collectable));
        });

        assertEquals(expected.size(), codex.getEarnedCollectables().size());
    }


    /*void savePlacementHistory() {
        Codex codex = new Codex();
        System.out.println(codex.toString());
        try {
            codex.savePlacementHistory("./codexserialized.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}