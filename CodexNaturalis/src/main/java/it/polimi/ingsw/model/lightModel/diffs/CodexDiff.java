package it.polimi.ingsw.model.lightModel.diffs;

import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.lightModel.LightCodex;
import it.polimi.ingsw.model.lightModel.LightPlacement;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.List;

public class CodexDiff implements ModelDiffs<LightCodex>{
    private final int addPoints;
    private final List<Collectable> addCollectables;
    private final List<Collectable> removeCollectables;
    private final List<LightPlacement> addPlacements;
    private final List<Position> addFrontier;
    private final List<Position> removeFrontier;
    /**
     * This class represents the differences between two codexes
     * @param addPoints the points to add
     * @param addCollectables the collectables to add
     * @param removeCollectables the collectables to remove
     * @param addPlacements the placements to add
     * @param addFrontier the frontier to add
     * @param removeFrontier the frontier to remove
     */
    public CodexDiff(int addPoints, List<Collectable> addCollectables, List<Collectable> removeCollectables, List<LightPlacement> addPlacements, List<Position> addFrontier, List<Position> removeFrontier){
        this.addPoints = addPoints;
        this.addCollectables = addCollectables;
        this.removeCollectables = removeCollectables;
        this.addPlacements = addPlacements;
        this.addFrontier = addFrontier;
        this.removeFrontier = removeFrontier;

    }
    @Override
    public void apply(LightCodex lightCodex) {
        lightCodex.addPoints(this.addPoints);
        lightCodex.addPlacement(this.addPlacements);
        lightCodex.difFrontier(this.addFrontier, this.removeFrontier);
        lightCodex.difCollectables(this.addCollectables, this.removeCollectables);
    }
}
