package it.polimi.ingsw.controller3.mediators;

import it.polimi.ingsw.controller3.mediatorSubscriber.MediatorSubscriber;
import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;

import java.util.ArrayList;
import java.util.List;

public abstract class Mediator <SubscriberType extends MediatorSubscriber, DiffType extends Differentiable> {
    protected final List<SubscriberType> subscribers = new ArrayList<>();

    public void subscribe(SubscriberType subscriber){
        subscribers.add(subscriber);
    }
    public void unsubscribe(SubscriberType subscriber){
        subscribers.remove(subscriber);
    }

}
