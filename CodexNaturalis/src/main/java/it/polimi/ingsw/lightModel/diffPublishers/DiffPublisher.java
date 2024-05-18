package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;

public interface DiffPublisher <DiffType extends Differentiable, SubscriberType>{
    void subscribe(SubscriberType diffSubscriber);
    void unsubscribe(SubscriberType diffSubscriber);
    void notifySubscribers(ModelDiffs<DiffType> diff);
}
