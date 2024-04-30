package it.polimi.ingsw.lightModel.diffObserverInterface;

import java.io.Serializable;

public interface DiffPubliher extends Serializable {
    void subscribe(DiffSubscriber diffSubscriber);
    void unsubscribe(DiffSubscriber diffSubscriber);
    void notifySubscriber();
}
