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
import java.util.Map;

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
        this.points += diff.addPoints();
        this.frontier.applyDiff(new FrontierDiff(diff.addFrontier(), diff.removeFrontier()));
        for (Collectable c : diff.addCollectables())
            this.collectables.put(c, this.collectables.get(c) + 1);
        for (Collectable c : diff.removeCollectables())
            this.collectables.put(c, this.collectables.get(c) - 1);
        for (LightPlacement p : diff.addPlacements())
            this.placementHistory.put(p.position(), p);
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
}
