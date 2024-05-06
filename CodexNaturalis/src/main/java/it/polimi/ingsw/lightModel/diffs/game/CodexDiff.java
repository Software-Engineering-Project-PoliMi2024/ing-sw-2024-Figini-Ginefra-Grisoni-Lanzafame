package it.polimi.ingsw.lightModel.diffs.game;

import it.polimi.ingsw.lightModel.lightPlayerRelated.LightFrontier;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.List;
import java.util.Map;

public class CodexDiff extends GameDiff{
    private final String owner;
    private final int setPoints;
    private final Map<Collectable, Integer> collectables;
    private final List<LightPlacement> addPlacements;
    private final List<Position> frontier;
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
        this.setPoints = points;
        this.collectables = collectables;
        this.addPlacements = placements;
        this.frontier = frontier;

    }

    /**
     * @param lightGame the game from which get the codex to which apply the differences
     */
    @Override
    public void apply(LightGame lightGame) {
        lightGame.checkCodex(owner);
        lightGame.setPoint(setPoints, this.owner);
        lightGame.getCodexMap().get(this.owner).addPlacement(addPlacements);
        lightGame.setFrontier(new LightFrontier(frontier), this.owner);
        lightGame.setCollectable(collectables, this.owner);
    }
}
