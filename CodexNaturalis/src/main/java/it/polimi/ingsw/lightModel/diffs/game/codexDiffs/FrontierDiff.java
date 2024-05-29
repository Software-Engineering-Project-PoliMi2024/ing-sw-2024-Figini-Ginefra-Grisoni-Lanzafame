package it.polimi.ingsw.lightModel.diffs.game.codexDiffs;

import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.lightPlayerRelated.LightFrontier;
import it.polimi.ingsw.model.playerReleted.Position;

import java.util.List;

public record FrontierDiff(List<Position> add, List<Position> remove) implements ModelDiffs<LightFrontier> {
    /**
     * @param frontier the LightFrontier to which the diff applies
     */
    public void apply(LightFrontier frontier){
        frontier.difFrontier(add, remove);
    }
}
