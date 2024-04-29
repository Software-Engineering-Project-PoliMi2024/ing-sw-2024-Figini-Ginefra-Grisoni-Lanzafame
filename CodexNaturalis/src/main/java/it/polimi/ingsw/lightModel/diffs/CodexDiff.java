package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.LightGame;
import it.polimi.ingsw.lightModel.LightHand;
import it.polimi.ingsw.lightModel.LightPlacement;
import it.polimi.ingsw.model.cardReleted.utilityEnums.Collectable;
import it.polimi.ingsw.lightModel.LightCodex;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.List;
import java.util.Map;

public class CodexDiff extends GameDiff{
    private final String owner;
    private final int addPoints;
    private final Map<Collectable, Integer> collectablesToAdd;
    private final Map<Collectable, Integer> collectablesToRemove;
    private final List<LightPlacement> addPlacements;
    private final List<Position> addFrontier;
    private final List<Position> removeFrontier;
    /**
     * This class represents the differences between two codexes
     * @param owner the owner of the codex
     * @param addPoints the points to add
     * @param addCollectables the collectables to add
     * @param removeCollectables the collectables to remove
     * @param addPlacements the placements to add
     * @param addFrontier the frontier to add
     * @param removeFrontier the frontier to remove
     */
    public CodexDiff(String owner, int addPoints, Map<Collectable, Integer> addCollectables, Map<Collectable, Integer> removeCollectables, List<LightPlacement> addPlacements, List<Position> addFrontier, List<Position> removeFrontier){
        this.owner = owner;
        this.addPoints = addPoints;
        this.collectablesToAdd = addCollectables;
        this.collectablesToRemove = removeCollectables;
        this.addPlacements = addPlacements;
        this.addFrontier = addFrontier;
        this.removeFrontier = removeFrontier;

    }

    /**
     * @param lightGame the game from which get the codex to which apply the differences
     */
    @Override
    public void apply(LightGame lightGame) {
        lightGame.getCodexMap().get(this.owner).addPoints(this.addPoints);
        lightGame.getCodexMap().get(this.owner).addPlacement(this.addPlacements);
        lightGame.getCodexMap().get(this.owner).difFrontier(this.addFrontier, this.removeFrontier);
        lightGame.getCodexMap().get(this.owner).difCollectables(this.collectablesToAdd, this.collectablesToRemove);
    }
}
