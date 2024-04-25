package it.polimi.ingsw.model.lightModel.diffs;

public interface ModelDifferentiable<DiffType> {
    void applyDiff(DiffType diff);
}
