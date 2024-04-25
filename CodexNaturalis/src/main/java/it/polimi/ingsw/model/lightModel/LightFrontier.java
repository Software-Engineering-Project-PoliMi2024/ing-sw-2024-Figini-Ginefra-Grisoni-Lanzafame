package it.polimi.ingsw.model.lightModel;

import it.polimi.ingsw.model.lightModel.diffs.FrontierDiff;
import it.polimi.ingsw.model.lightModel.diffs.ModelDifferentiable;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.List;

public record LightFrontier(List<Position> frontier) implements ModelDifferentiable<FrontierDiff> {
    /**
     * * @param diff the diff to apply
     */
    public void applyDiff(FrontierDiff diff) {

    }
    /**
     * @param add the positions to add
     * @param rmv the positions to remove
     */
    public void difFrontier(List<Position> add, List<Position> rmv) {
        this.frontier.removeAll(rmv);
        this.frontier.addAll(add);
    }
    /**
     * @return the frontier
     */
    @Override
    public List<Position> frontier() {
        return this.frontier;
    }

    /**
     * Checks if a position is in the frontier
     * @param position position to check
     * @return true if the position is in the frontier, false otherwise
     */
    public boolean isInFrontier(Position position) {
        return frontier.contains(position);
    }

    /**
     * @return the size of the frontier
     */
    public int size() {
        return frontier.size();
    }
}
