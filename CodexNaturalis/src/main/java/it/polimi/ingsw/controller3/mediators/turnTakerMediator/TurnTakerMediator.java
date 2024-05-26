package it.polimi.ingsw.controller3.mediators.turnTakerMediator;

public class TurnTakerMediator extends GenericMediator<TurnTaker>{
    public synchronized void subscribe(String nickname, TurnTaker subscriber){
        super.subscribe(nickname, subscriber);
    }
    public synchronized void unsubscribe(String nickname){
        super.unsubscribe(nickname);
    }
    public synchronized void takeTurn(String nickname){
        subscribers.get(nickname).takeTurn();
    }
}
