package it.polimi.ingsw.lightModel.diffLists;

import java.io.Serializable;

public interface DiffPublisherNick extends Serializable {
    void subscribe(DiffSubscriber diffSubscriber, String nickname);
    void unsubscribe(DiffSubscriber diffSubscriber);
    void notifySubscriber();
}
