package it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators;

import java.util.HashMap;
import java.util.Map;

public abstract class GenericJoinAndTurnMediator<subscriberType> {
    protected final Map<String, subscriberType> subscribers = new HashMap<>();

    protected void subscribe(String nickname, subscriberType subscriber){
        subscribers.put(nickname, subscriber);
    }
    protected void unsubscribe(String nickname){
        subscribers.remove(nickname);
    }
}
