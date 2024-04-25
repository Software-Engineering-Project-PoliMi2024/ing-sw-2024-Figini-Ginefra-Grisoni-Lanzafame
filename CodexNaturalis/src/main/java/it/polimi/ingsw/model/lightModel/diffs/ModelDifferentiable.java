package it.polimi.ingsw.model.lightModel.diffs;

/**
 * This interface is used to apply differences to a model.
 * @param <DiffType> the type of the elements that have been added or removed
 */
public interface ModelDifferentiable <DiffType>{
    void applyDiff(ModelDiff<DiffType> diff);
}
