package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.Differentiable;

import java.io.Serializable;

public interface ModelDiffs<DiffType extends Differentiable> extends Serializable {
    void apply(DiffType differentiableType);
}
