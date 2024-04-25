package it.polimi.ingsw.model.lightModel.diffs;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for the differences between two models.
 * @param <DiffType> the type of the elements that have been added or removed
 */
public class ModelDiff<DiffType>{
    private final List<DiffType> addList;
    private final List<DiffType> removeList;

    public ModelDiff(){
        addList = new ArrayList<>();
        removeList = new ArrayList<>();
    }
    public List<DiffType> getAddList() {
        return addList;
    }

    public List<DiffType> getRemoveList() {
        return removeList;
    }
}
