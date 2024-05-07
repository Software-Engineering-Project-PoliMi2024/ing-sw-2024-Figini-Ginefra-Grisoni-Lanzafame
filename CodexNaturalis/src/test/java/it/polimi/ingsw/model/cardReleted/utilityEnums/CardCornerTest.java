package it.polimi.ingsw.model.cardReleted.utilityEnums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardCornerTest {

    @Test
    void getOpposite_TL() {
        assertEquals(CardCorner.BR, CardCorner.TL.getOpposite());
    }

    @Test
    void getOpposite_TR() {
        assertEquals(CardCorner.BL, CardCorner.TR.getOpposite());
    }

    @Test
    void getOpposite_BL() {
        assertEquals(CardCorner.TR, CardCorner.BL.getOpposite());
    }

    @Test
    void getOpposite_BR() {
        assertEquals(CardCorner.TL, CardCorner.BR.getOpposite());
    }
}