package it.polimi.ingsw.lightModel.diffLists;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;

import java.util.List;

public interface DiffPublisher {
    void subscribe(DiffSubscriber diffSubscriber);
    void unsubscribe(DiffSubscriber diffSubscriber);
    void notifySubscriber();
}
