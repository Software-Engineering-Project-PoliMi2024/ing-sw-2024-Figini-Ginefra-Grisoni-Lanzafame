package it.polimi.ingsw.model.playerReleted;

import it.polimi.ingsw.model.cardReleted.utilityEnums.CardCorner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the frontier of a codex
 * The frontier is the set of positions that are free and adjacent to a placement
 */
public class Frontier implements Serializable {
    /** The frontier */
    List<Position> frontier;

    /**
     * Default constructor
     */
    public Frontier(){
        frontier = new ArrayList<>();
        frontier.add(new Position(0, 0));
    }

    /**
     * Copy constructor
     * @param other the frontier to copy
     */
    public Frontier(Frontier other){
        this.frontier = new ArrayList<>(other.frontier);
    }

    /**returns the map describing for each
     * Update the frontier after a placement
     * @throws IllegalArgumentException if codex or position are null
     * @param codex the codex
     * @param position the position of the placement
     */
    public void updateFrontier(Codex codex, Position position){
        if(codex == null || position == null)
            throw new IllegalArgumentException("Codex and position cannot be null");

        this.removePosition(position);

        Placement placement = codex.getPlacementAt(position);
        System.out.println(placement);
        System.out.println(placement.card());
        for (CardCorner corner : CardCorner.values()) {
            if(!placement.card().isCorner(corner, placement.face()))
                continue;

            Position possiblePosition = position.add(corner.getOffset());
            if (codex.getPlacementAt(possiblePosition) == null) {
                this.addPosition(possiblePosition);
            }
        }

    }

    /**
     * Returns the frontier
     * @return the frontier
     */
    public List<Position> getFrontier(){
        return new ArrayList<>(this.frontier);
    }

    /**
    * Checks if a position is in the frontier
    * @param position position to check
    * @return true if the position is in the frontier, false otherwise
    */
    public boolean isInFrontier(Position position){
        return frontier.contains(position);
    }

    /**
    * Adds a position to the frontier if it is not already present
     * @throws IllegalArgumentException if position is null
    * @param position position to add
    */
    public void addPosition(Position position){
        if(position == null)
            throw new IllegalArgumentException("Position cannot be null");

        if(!frontier.contains(position))
            frontier.add(new Position(position));
    }

    /**
     * Removes a position from the frontier
     * @throws IllegalArgumentException if position is null
     * @param position position to remove
     */

    public void removePosition(Position position){
        if(position == null)
            throw new IllegalArgumentException("Position cannot be null");

        frontier.remove(position);
    }

    /**
     * Returns the size of the frontier
     * @return the size of the frontier
     */
    public int size(){
        return frontier.size();
    }

    @Override
    public String toString(){
        return "Frontier[" + this.frontier + "]";
    }
}
