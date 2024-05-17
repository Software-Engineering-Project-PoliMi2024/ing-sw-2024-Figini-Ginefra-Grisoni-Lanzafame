package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;

public interface DiffPublisher <T extends Differentiable> {
    void subscribe(DiffSubscriber diffSubscriber);
    void unsubscribe(DiffSubscriber diffSubscriber);
    void notifySubscribers(ModelDiffs<T> diff);
}
