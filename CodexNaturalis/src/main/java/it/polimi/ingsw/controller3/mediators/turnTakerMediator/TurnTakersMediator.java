package it.polimi.ingsw.controller3.mediators.turnTakerMediator;

import java.util.HashMap;
import java.util.Map;

public class TurnTakersMediator {
    private final Map<String, TurnTaker> turnTakers;

    public TurnTakersMediator() {
        this.turnTakers = new HashMap<>();
    }

    /**
     * Subscribes a turnTaker to the mediator
     * @param nickname the nickname of the turnTaker
     * @param turnTaker the turnTaker
     */
    public synchronized void subscribe(String nickname, TurnTaker turnTaker) {
        turnTakers.put(nickname, turnTaker);
    }

    /**
     * Unsubscribes a turnTaker from the mediator
     * @param nickname the nickname of the turnTaker
     */
    public synchronized void unsubscribe(String nickname) {
        turnTakers.remove(nickname);
    }

    public void gameStarted() {
        turnTakers.values().forEach(TurnTaker::joinGame);
    }
}
