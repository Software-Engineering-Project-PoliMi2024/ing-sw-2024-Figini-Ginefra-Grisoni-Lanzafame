package it.polimi.ingsw.model.lightModel.diffs;

public record QuadModelDiff<DiffTypeA, DiffTypeB, DiffTypeC, DiffTypeD>(ModelDiff<DiffTypeA> A, ModelDiff<DiffTypeB> B, ModelDiff<DiffTypeC> C, ModelDiff<DiffTypeD> D){
}
