package it.polimi.ingsw.controller3.mediators.turnTakerMediator;

import it.polimi.ingsw.model.utilities.Pair;
import it.polimi.ingsw.view.LoggerInterface;

import java.util.HashMap;
import java.util.Map;

public abstract class GenericMediator <subscriberType> {
    protected final Map<String, subscriberType> subscribers = new HashMap<>();

    protected void subscribe(String nickname, subscriberType subscriber){
        subscribers.put(nickname, subscriber);
    }
    protected void unsubscribe(String nickname){
        subscribers.remove(nickname);
    }
}
