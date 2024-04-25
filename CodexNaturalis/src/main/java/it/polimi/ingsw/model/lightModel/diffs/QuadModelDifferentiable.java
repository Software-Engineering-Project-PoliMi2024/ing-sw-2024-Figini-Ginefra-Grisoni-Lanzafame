package it.polimi.ingsw.model.lightModel.diffs;

public interface QuadModelDifferentiable<DiffTypeA, DiffTypeB, DiffTypeC, DiffTypeD>{
    void applyDiff(QuadModelDiff<DiffTypeA, DiffTypeB, DiffTypeC, DiffTypeD> diff);
}
