package it.polimi.ingsw.model.playerReleted;

/**
 * Class representing a position in a 2D space
 * @author Paolo
 */
public record Position(int x, int y) {
    /**
     * Class constructor
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public Position {
    }


    /**
     * Returns a new position with the sum of the coordinates
     *
     * @param position position to add
     * @return a new position with the sum of the coordinates
     */
    public Position add(Position position) {
        return new Position(this.x + position.x, this.y + position.y);
    }

    /**
     * Returns a new position with the sum of the coordinates
     *
     * @param x x coordinate to add
     * @param y y coordinate to add
     * @return a new position with the sum of the coordinates
     */
    public Position add(int x, int y) {
        return new Position(this.x + x, this.y + y);
    }
}
