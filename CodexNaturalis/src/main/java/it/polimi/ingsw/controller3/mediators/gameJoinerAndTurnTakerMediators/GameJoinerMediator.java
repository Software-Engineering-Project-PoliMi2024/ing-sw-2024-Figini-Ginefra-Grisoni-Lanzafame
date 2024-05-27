package it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators;

public class GameJoinerMediator extends GenericJoinAndTurnMediator<GameJoiner> {
    /**
     * Subscribes a gameJoiner to the mediator, so that it can be notified when the game starts
     * @param nickname the nickname of the turnTaker
     * @param gameJoiner the
     */
    public synchronized void subscribe(String nickname, GameJoiner gameJoiner) {
        super.subscribe(nickname, gameJoiner);
    }

    /**
     * Unsubscribes a turnTaker from the mediator
     * @param nickname the nickname of the turnTaker
     */
    public synchronized void unsubscribe(String nickname) {
        super.unsubscribe(nickname);
    }

    public synchronized void notifyGameStart() {
        subscribers.values().forEach(GameJoiner::joinGame);
    }
}
