package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.LightFrontier;
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
