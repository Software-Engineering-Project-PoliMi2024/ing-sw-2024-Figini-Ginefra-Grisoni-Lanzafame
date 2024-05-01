package it.polimi.ingsw.lightModel.diffObserverInterface;

import java.io.Serializable;

public interface DiffPublisherNick extends Serializable {
    void subscribe(DiffSubscriber diffSubscriber, String nickname);
    void unsubscribe(DiffSubscriber diffSubscriber);
}
