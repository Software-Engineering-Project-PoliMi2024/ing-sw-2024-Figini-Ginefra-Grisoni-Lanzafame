package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.Differentiable;

import java.io.Serializable;

/**
 * This interface mark all the classes that are used to update the Differentiable objects
 * @param <DiffType> the type of the object that can be updated
 */
public interface ModelDiffs<DiffType extends Differentiable> extends Serializable {
    /**
     * Apply the update to the object
     * @param differentiableType the object to update
     */
    void apply(DiffType differentiableType);
}
