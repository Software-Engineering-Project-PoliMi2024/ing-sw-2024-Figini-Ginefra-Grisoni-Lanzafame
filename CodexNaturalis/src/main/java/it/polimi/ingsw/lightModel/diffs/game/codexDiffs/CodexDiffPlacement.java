package it.polimi.ingsw.lightModel.diffs.game.codexDiffs;

import it.polimi.ingsw.lightModel.diffs.game.GameDiff;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightFrontier;
import it.polimi.ingsw.lightModel.lightTableRelated.LightGame;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.List;
import java.util.Map;

/**
 * This class represent an update for the codex of a player
 */
public class CodexDiffPlacement extends GameDiff {
    /** The owner of the updated codex */
    private final String owner;
    /** The new points of the codex after the update*/
    private final int setPoints;
    /** The new value of each collectable of the codex after the update*/
    private final Map<Collectable, Integer> collectables;
    /** The new placement for the updated codex*/
    private final List<LightPlacement> addPlacements;
    /** The new frontier position the updatedCodex created*/
    private final List<Position> frontier;
    /**
     * Constructor
     * This class represents the differences between two codexes
     * @param owner the owner of the codex
     * @param points the new points do to the placement
     * @param collectables the new collectable
     * @param placements the placements to add
     * @param frontier the new frontier
     */
    public CodexDiffPlacement(String owner, int points, Map<Collectable, Integer> collectables, List<LightPlacement> placements, List<Position> frontier){
        this.owner = owner;
        this.setPoints = points;
        this.collectables = collectables;
        this.addPlacements = placements;
        this.frontier = frontier;

    }

    /**
     * This method update apply the new values to the codex. The new points, the new collectable values, the placement added and the new frontier positions
     * @param lightGame the game from which get the codex to which apply the differences
     */
    @Override
    public void apply(LightGame lightGame) {
        lightGame.setPoint(setPoints, this.owner);
        lightGame.getCodexMap().get(this.owner).addPlacement(addPlacements);
        lightGame.setFrontier(new LightFrontier(frontier), this.owner);
        lightGame.setCollectable(collectables, this.owner);
    }
}
