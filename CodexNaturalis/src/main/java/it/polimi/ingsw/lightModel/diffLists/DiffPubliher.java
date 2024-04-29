package it.polimi.ingsw.lightModel.diffLists;

import java.io.Serializable;

public interface DiffPubliher extends Serializable {
    void subscribe(DiffSubscriber diffSubscriber);
    void unsubscribe(DiffSubscriber diffSubscriber);
    void notifySubscriber();
}
