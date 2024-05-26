package it.polimi.ingsw.controller3.mediators.turnTakerMediator;

import java.util.List;

public class TurnTakerMediator extends GenericMediator<TurnTaker>{
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
     * @param nickname the nickname of the player that is being notified
     */
    public synchronized void notifyTurn(String nickname){
        subscribers.get(nickname).takeTurn();
    }

    /**
     * This method is used to get the list of active player's nickname
     * @return the list of active player's nickname
     */
    public synchronized List<String> getActivePlayers(){
        return subscribers.keySet().stream().toList();
    }
}
