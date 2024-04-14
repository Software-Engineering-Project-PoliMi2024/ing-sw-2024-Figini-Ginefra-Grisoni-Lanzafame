package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.utilityEnums.SpecialCollectable;
import org.junit.jupiter.api.Test;

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
    void playCard() {
        Codex codex = new Codex();

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