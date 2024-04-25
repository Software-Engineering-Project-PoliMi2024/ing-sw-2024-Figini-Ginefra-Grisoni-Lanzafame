package it.polimi.ingsw.model.lightModel;

import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import it.polimi.ingsw.model.lightModel.diffs.CodexDiff;
import it.polimi.ingsw.model.lightModel.diffs.FrontierDiff;
import it.polimi.ingsw.model.lightModel.diffs.ModelDifferentiable;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.*;

//a
public class LightCodex implements ModelDifferentiable<CodexDiff>{
    private int points;
    private final Map<Collectable, Integer> collectables;
    private final Map<Position, LightPlacement> placementHistory;
    private final LightFrontier frontier;
    /** Constructor of the light codex class
     * initializes the points to 0
     * initializes the collectables to 0 for each collectable
     * initializes the frontier
     * initializes the placement history
     * */
    public LightCodex(){
        this.points = 0;
        this.collectables = new HashMap<>();
        for (Collectable c : Resource.values())
            this.collectables.put(c, 0);
        for (Collectable c : WritingMaterial.values())
            this.collectables.put(c, 0);

        this.frontier = new LightFrontier(new ArrayList<Position>());

        this.placementHistory = new LinkedHashMap<>();
    }
    /** @return points of the related codex*/
    public int getPoints() { return this.points;}
    /** set the points related to the codex
     * @param diff the difference between the new codex and the old one
     * */
    public void applyDiff(CodexDiff diff) {
        diff.apply(this);
    }
    /**
     * @param points the points to be added to the codex
     */
    public void addPoints(int points) {
        this.points += points;
    }

    /**
     * @return the collectables of the related codex
     */
    public Map<Collectable, Integer> getEarnedCollectables(){
        return this.collectables;
    }
    /**
     * @return the frontier of the related codex
     */
    public LightFrontier getFrontier() {
        return frontier;
    }
    /**
     * @return the placement history of the related codex
     */
    public Map<Position, LightPlacement> getPlacementHistory() {
        return placementHistory;
    }
    /**
     * @param placementDiff the placements to be added to the placement history
     */
    public void addPlacement(List<LightPlacement> placementDiff){
        for (LightPlacement p : placementDiff)
            this.placementHistory.put(p.position(), p);
    }

    /**
     * @param addFrontier the frontier changes to be added to the related codex
     * @param rmvFrontier the frontier changes to be removed from the related codex
     */
    public void difFrontier(List<Position> addFrontier, List<Position> rmvFrontier){
        this.frontier.applyDiff(new FrontierDiff(addFrontier, rmvFrontier));
    }

    /**
     * @param addCollectables the change in collectables to be added to the related codex
     * @param removeCollectables the change in collectables to be removed from the related codex
     */
    public void difCollectables(List<Collectable> addCollectables, List<Collectable> removeCollectables) {
        for (Collectable c : addCollectables)
            this.collectables.put(c, this.collectables.get(c) + 1);
        for (Collectable c : removeCollectables)
            this.collectables.put(c, this.collectables.get(c) - 1);
    }
}
