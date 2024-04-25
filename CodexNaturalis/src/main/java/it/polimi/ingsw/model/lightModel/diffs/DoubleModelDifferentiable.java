package it.polimi.ingsw.model.lightModel.diffs;

public interface DoubleModelDifferentiable<DiffTypeA, DiffTypeB> {
    public void applyDiff(DoubleModelDiff<DiffTypeA, DiffTypeB> diff);
}
