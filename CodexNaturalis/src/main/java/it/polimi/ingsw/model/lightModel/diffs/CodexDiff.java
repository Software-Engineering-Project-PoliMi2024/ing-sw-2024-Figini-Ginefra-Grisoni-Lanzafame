package it.polimi.ingsw.model.lightModel.diffs;

import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.lightModel.LightCodex;
import it.polimi.ingsw.model.lightModel.LightPlacement;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.List;

/**
 * This class represents the differences between two codexes
 * @param addPoints the points to add
 * @param addCollectables the collectables to add
 * @param removeCollectables the collectables to remove
 * @param addPlacements the placements to add
 * @param addFrontier the frontier to add
 * @param removeFrontier the frontier to remove
 */
public record CodexDiff(int addPoints, List<Collectable> addCollectables, List<Collectable> removeCollectables,
                        List<LightPlacement> addPlacements, List<Position> addFrontier,
                        List<Position> removeFrontier) implements ModelDiffs<LightCodex>{
    @Override
    public void apply(LightCodex lightCodex) {

    }
}
