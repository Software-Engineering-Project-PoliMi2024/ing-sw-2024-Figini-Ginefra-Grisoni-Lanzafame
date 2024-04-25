package it.polimi.ingsw.model.lightModel.diffs;

import java.util.List;

/**
 * This class is a container for the differences between two models.
 * @param addList list of elements that have been added
 * @param removeList list of elements that have been removed
 * @param <DiffType> the type of the elements that have been added or removed
 */
public record ModelDiff<DiffType>(List<DiffType> addList, List<DiffType> removeList){
}
