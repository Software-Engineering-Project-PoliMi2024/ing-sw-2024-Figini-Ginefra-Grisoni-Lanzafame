package it.polimi.ingsw.model.lightModel.diffs;

import java.util.List;

public record DoubleModelDiff<DiffTypeA, DiffTypeB>(ModelDiff<DiffTypeA> A, ModelDiff<DiffTypeB> B){

}

