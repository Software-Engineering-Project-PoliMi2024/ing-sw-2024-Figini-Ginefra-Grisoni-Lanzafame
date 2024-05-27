package it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GenericJoinAndTurnMediator<subscriberType> {
    protected final Map<String, subscriberType> subscribers = new ConcurrentHashMap<>();

    protected void subscribe(String nickname, subscriberType subscriber){
        subscribers.put(nickname, subscriber);
    }
    protected void unsubscribe(String nickname){
        subscribers.remove(nickname);
    }
}
