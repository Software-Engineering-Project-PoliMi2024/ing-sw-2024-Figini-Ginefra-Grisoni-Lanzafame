package it.polimi.ingsw.lightModel.diffLists;

public interface DiffPublisherDouble<DiffObjectType> extends DiffPublisher{
    void notifySubscriber(DiffSubscriber diffSubscriber, DiffObjectType diffObject);
    void subscribe(DiffObjectType diffSubscriber);
    void unsubscribe(DiffObjectType diffSubscriber);

}
