package it.polimi.ingsw.lightModel.lightPlayerRelated;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.List;

/**
 * This class represents the frontier in the light model.
 * The lightFrontier is a list of positions in which the player can place a card.
 * @param frontier
 */
public record LightFrontier(List<Position> frontier) implements Differentiable {
    /**
     * Add and remove positions from the light frontier
     * @param add a list of positions to add
     * @param rmv a list of positions to remove
     */
    public void difFrontier(List<Position> add, List<Position> rmv) {
        this.frontier.removeAll(rmv);
        this.frontier.addAll(add);
    }

    /**
     * A constructor for the class.
     * Create a new frontier copying the list of positions from the frontier passed as parameter
     * @param other the frontier to copy
     */
    public LightFrontier(LightFrontier other) {
        this(other.frontier);
    }

    /**
     * @return the frontier as a list of positions
     */
    @Override
    public List<Position> frontier() {
        return this.frontier;
    }

    /**
     * @return the size of the frontier
     */
    public int size() {
        return frontier.size();
    }
}
