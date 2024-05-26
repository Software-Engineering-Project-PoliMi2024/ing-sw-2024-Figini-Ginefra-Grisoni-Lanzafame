package it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators;

import java.util.List;

public class TurnTakerMediator extends GenericJoinAndTurnMediator<TurnTaker> {
    /**
     * This method is used to subscribe a new player to the mediator
     * subscribing a player means that he will be able to be notified when it is his turn
     * and will be considered active
     * @param nickname the nickname of the player that wants to subscribe
     * @param subscriber the subscriber that wants to subscribe
     */
    public synchronized void subscribe(String nickname, TurnTaker subscriber){
        super.subscribe(nickname, subscriber);
    }

    /**
     * This method is used to unsubscribe a player from the mediator
     * thus making him inactive and unable to receive notifications
     * @param nickname the nickname of the player that wants to unsubscribe
     */
    public synchronized void unsubscribe(String nickname){
        super.unsubscribe(nickname);
    }

    /**
     * This method is used to notify a player that it is his turn
     * by calling the takeTurn method of the subscriber
     */
    public synchronized void notifyTurn(){
        subscribers.values().forEach(TurnTaker::takeTurn);
    }

    /**
     * This method is used to notify all players that
     * it is his turn to choose the objective
     */
    public synchronized void notifyChooseObjective(){
        subscribers.values().forEach(TurnTaker::chooseObjective);
    }

    /**
     * This method is used to get the list of active player's nickname
     * @return the list of active player's nickname
     */
    public synchronized List<String> getActivePlayers(){
        return subscribers.keySet().stream().toList();
    }
}
