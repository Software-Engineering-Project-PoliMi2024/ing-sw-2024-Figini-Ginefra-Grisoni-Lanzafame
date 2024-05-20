package it.polimi.ingsw.view.GUI;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateGUITest {

    @Test
    void testEquals_shouldBeEqualWhenIdentical() {
        StateGUI stateGUI = StateGUI.SERVER_CONNECTION;
        StateGUI stateGUI2 = StateGUI.SERVER_CONNECTION;
        assertTrue(stateGUI.equals(stateGUI2));
    }

    @Test
    void testEquals_shouldBeEqualWhenEqual() {
        StateGUI stateGUI = StateGUI.CHOOSE_START_CARD;
        StateGUI stateGUI2 = StateGUI.SELECT_OBJECTIVE;
        assertTrue(stateGUI.equals(stateGUI2));
    }
}