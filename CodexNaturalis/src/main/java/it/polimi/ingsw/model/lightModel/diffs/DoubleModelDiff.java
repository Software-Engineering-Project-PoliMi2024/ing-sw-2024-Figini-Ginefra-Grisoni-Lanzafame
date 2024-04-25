package it.polimi.ingsw.model.lightModel.diffs;

public record DoubleModelDiff<DiffTypeA, DiffTypeB>(ModelDiff<DiffTypeA> A, ModelDiff<DiffTypeB> B){

}

