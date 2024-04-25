package it.polimi.ingsw.model.lightModel.diffs;

public interface ModelDiffs<DifferentiableType> {
    void apply(DifferentiableType differentiableType);
}
