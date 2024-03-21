package it.polimi.ingsw.model.playerReleted;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void ToString() {
        Position position = new Position(1, 1);
        assertEquals("Position[x=1, y=1]", position.toString());
    }

    @Test
    void add_inputPosition_shouldAdd() {
        Position position = new Position(1, 1);
        Position position2 = new Position(1, 1);
        Position result = position.add(position2);
        assertEquals(new Position(2, 2), result);
    }

    @Test
    void add_inputInt_shouldAdd() {
        Position position = new Position(1, 1);
        Position result = position.add(1, 1);
        assertEquals(new Position(2, 2), result);
    }

    @Test
    void equals_inputSamePosition_shouldReturnTrue() {
        Position position = new Position(1, 1);
        Position position2 = new Position(1, 1);
        assertEquals(position, position2);
    }

    @Test
    void equals_inputDifferentPosition_shouldReturnFalse() {
        Position position = new Position(1, 1);
        Position position2 = new Position(1, 2);
        assertNotEquals(position, position2);
    }

    @Test
    void hashCode_differentObjectsSameParameters_shouldEqual() {
        Position position = new Position(1, 1);
        Position position2 = new Position(1, 1);
        assertEquals(position.hashCode(), position2.hashCode());
    }
}