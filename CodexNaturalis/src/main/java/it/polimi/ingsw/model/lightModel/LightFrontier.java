package it.polimi.ingsw.model.lightModel;

import it.polimi.ingsw.model.lightModel.diffs.FrontierDiff;
import it.polimi.ingsw.model.lightModel.diffs.ModelDifferentiable;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.List;

public class LightFrontier implements ModelDifferentiable<FrontierDiff> {
    List<Position> frontier;
    /**returns the map describing for each
     * Updates the frontier after a placement
     * @throws IllegalArgumentException if codex or position are null
     */
    public void applyDiff(FrontierDiff diff){

    }
    public void difFrontier(List<Position> add, List<Position> rmv){
        frontier.removeAll(rmv);
        frontier.addAll(add);
    }
    /**
     * Returns the frontier
     * @return the frontier
     */
    public List<Position> getFrontier(){
        return this.frontier;
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
