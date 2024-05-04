package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightFrontier;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.List;
import java.util.Map;

public class CodexDiff extends GameDiff{
    private final String owner;
    private final int addPoints;
    private final Map<Collectable, Integer> collectablesToAdd;
    private final List<LightPlacement> addPlacements;
    private final List<Position> addFrontier;
    /**
     * This class represents the differences between two codexes
     * @param owner the owner of the codex
     * @param points the new points do to the placement
     * @param collectables the new collectable
     * @param placements the placements to add
     * @param frontier the new frontier
     */
    public CodexDiff(String owner, int points, Map<Collectable, Integer> collectables, List<LightPlacement> placements, List<Position> frontier){
        this.owner = owner;
        this.addPoints = points;
        this.collectablesToAdd = collectables;
        this.addPlacements = placements;
        this.addFrontier = frontier;

    }

    /**
     * @param lightGame the game from which get the codex to which apply the differences
     */
    @Override
    public void apply(LightGame lightGame) {
        lightGame.setPoint(addPoints, this.owner);
        lightGame.getCodexMap().get(this.owner).addPlacement(addPlacements);
        lightGame.setFrontier(new LightFrontier(addFrontier), this.owner);
        lightGame.setCollectable(collectablesToAdd, this.owner);
    }
}
