package it.polimi.ingsw.lightModel.lightPlayerRelated;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.diffs.FrontierDiff;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.*;

//a
public class LightCodex implements Differentiable {
    private int points;
    private Map<Collectable, Integer> collectables;
    private final Map<Position, LightPlacement> placementHistory;
    private LightFrontier frontier;
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

    /**
     * @param points the points of the related codex
     * @param collectables the collectables of the related codex
     * @param frontier the frontier of the related codex
     * @param placementHistory the placement history of the related codex
     */
    public LightCodex(int points, Map<Collectable, Integer> collectables, LightFrontier frontier, Map<Position, LightPlacement> placementHistory){
        this.points = points;
        this.collectables = collectables;
        this.frontier = frontier;
        this.placementHistory = placementHistory;
    }
    /** @return points of the related codex*/
    public int getPoints() { return this.points;}
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
        new FrontierDiff(addFrontier, rmvFrontier).apply(this.frontier);
    }

    /**
     * @param addCollectables the change in collectables to be added to the related codex
     * @param removeCollectables the change in collectables to be removed from the related codex
     */
    public void difCollectables(Map<Collectable,Integer> addCollectables, Map<Collectable,Integer> removeCollectables) {
        for (Collectable c : addCollectables.keySet())
            this.collectables.put(c, this.collectables.get(c) + addCollectables.get(c));
        for (Collectable c : removeCollectables.keySet())
            this.collectables.put(c, this.collectables.get(c) - removeCollectables.get(c));
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setFrontier(LightFrontier frontier) {
        this.frontier = frontier;
    }

    public void setCollectables(Map<Collectable, Integer> collectables) {
        this.collectables = collectables;
    }
}
