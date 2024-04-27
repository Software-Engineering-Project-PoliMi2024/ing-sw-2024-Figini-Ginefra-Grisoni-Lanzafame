package it.polimi.ingsw.lightModel.diffs;

import it.polimi.ingsw.lightModel.Differentiable;

public interface ModelDiffs<DiffType extends Differentiable>{
    void apply(DiffType differentiableType);
}
