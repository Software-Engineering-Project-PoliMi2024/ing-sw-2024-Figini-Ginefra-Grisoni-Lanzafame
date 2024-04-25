package it.polimi.ingsw.model.lightModel.diffs;

import it.polimi.ingsw.model.playerReleted.Position;

import java.util.List;

public record FrontierDiff(List<Position> add, List<Position> Remove){
}
