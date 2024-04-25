package it.polimi.ingsw.model.lightModel.diffs;

/**
 * This class is a container for two modelDiff
 * @param A the first ModelDiff
 * @param B the second ModelDiff
 * */
public record DoubleModelDiff<DiffType>(ModelDiff<DiffType> A, ModelDiff<DiffType> B){
}
