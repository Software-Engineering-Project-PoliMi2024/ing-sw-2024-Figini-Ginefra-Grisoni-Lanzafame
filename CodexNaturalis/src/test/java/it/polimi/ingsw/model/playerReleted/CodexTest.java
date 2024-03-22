package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.SpecialCollectable;
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
}