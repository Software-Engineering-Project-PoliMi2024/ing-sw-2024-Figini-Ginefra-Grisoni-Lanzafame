package it.polimi.ingsw.view.TUI.cardDrawing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextCardTest {

    @Test
    void getWithNullShouldThrowException() {
        TextCard textCard = new TextCard(null, null);
        assertThrows(IllegalArgumentException.class, () -> textCard.get(null));
    }
}