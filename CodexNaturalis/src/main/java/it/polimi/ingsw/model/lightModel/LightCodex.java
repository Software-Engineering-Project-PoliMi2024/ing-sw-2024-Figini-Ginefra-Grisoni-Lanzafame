package it.polimi.ingsw.model.lightModel;

import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Resource;
import it.polimi.ingsw.model.cardReleted.utilityEnums.WritingMaterial;
import it.polimi.ingsw.model.lightModel.diffs.CodexDiff;
import it.polimi.ingsw.model.lightModel.diffs.FrontierDiff;
import it.polimi.ingsw.model.lightModel.diffs.ModelDifferentiable;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//a
public class LightCodex implements ModelDifferentiable<CodexDiff>{
    private int points;
    private final Map<Collectable, Integer> collectables;
    private final Map<Position, LightPlacement> placementHistory;
    private final LightFrontier frontier;
    /** Constructor of the Codex class
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

        this.frontier = new LightFrontier();

        this.placementHistory = new LinkedHashMap<>();
    }
    /** @return points of the related codex*/
    public int getPoints() { return this.points;}
    /** set the points related to the codex
     * @param diff the points value to set
     * */
    public void applyDiff(CodexDiff diff) {
        diff.apply(this);
    }
    public void addPoints(int points) {
        this.points += points;
    }
    public Map<Collectable, Integer> getEarnedCollectables(){
        return this.collectables;
    }
    public LightFrontier getFrontier() {
        return frontier;
    }
    public Map<Position, LightPlacement> getPlacementHistory() {
        return placementHistory;
    }
    public void addPlacement(List<LightPlacement> placementDiff){
        for (LightPlacement p : placementDiff)
            this.placementHistory.put(p.position(), p);
    }
    public void difFrontier(List<Position> addFrontier, List<Position> rmvFrontier){
        this.frontier.applyDiff(new FrontierDiff(addFrontier, rmvFrontier));
    }
    public void difCollectables(List<Collectable> addCollectables, List<Collectable> removeCollectables) {
        for (Collectable c : addCollectables)
            this.collectables.put(c, this.collectables.get(c) + 1);
        for (Collectable c : removeCollectables)
            this.collectables.put(c, this.collectables.get(c) - 1);
    }
}
