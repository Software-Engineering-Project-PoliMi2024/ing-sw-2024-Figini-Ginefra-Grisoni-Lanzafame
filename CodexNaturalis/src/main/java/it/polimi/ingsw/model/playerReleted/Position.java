package it.polimi.ingsw.model.playerReleted;

import java.io.Serializable;

/**
 * Class representing a position in a 2D space
 * @param x x coordinate of the position
 * @param y y coordinate of the position
 */
public record Position(int x, int y) implements Serializable {
    /**
     * Copy Constructor
     * @param other position to copy
     */
    public Position(Position other) {
        this(other.x, other.y);
    }


    /**
     * Returns a new position with the sum of the coordinates
     * @throws IllegalArgumentException if position is null
     * @param position position to add
     * @return a new position with the sum of the coordinates
     */
    public Position add(Position position) {
        if(position == null)
            throw new IllegalArgumentException("Position cannot be null");

        return new Position(this.x + position.x, this.y + position.y);
    }

    /** @return the x coordinate */
    public Integer getX() {
        return x;
    }

    /** @return the y coordinate*/
    public Integer getY() {
        return y;
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

    /**
     * Returns a scaled version of the position
     *
     * @param scalar scalar to multiply the coordinates by
     * @return a scaled version of the position
     */
    public Position multiply(int scalar){
        return new Position(this.x * scalar, this.y * scalar);
    }

    /** @param x the x coordinate to set
     *  @return a new position with the x coordinate set to x
     *          and the y coordinate unchanged
     */

    public Position setX(int x) {
        return new Position(x, this.y);
    }

    /**
     * Set the y coordinate of the position
     * @param y the y coordinate to set
     * @return a new position with the y coordinate set to y
     *         and the x coordinate unchanged
     */
    public Position setY(int y) {
        return new Position(this.x, y);
    }
}
