package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.model.cardReleted.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.ResourceCard;
import it.polimi.ingsw.model.cardReleted.GoldCard;
import it.polimi.ingsw.model.cardReleted.StartCard;
import it.polimi.ingsw.model.playerReleted.User;

import java.util.List;

public abstract class Game {
    final private Deck<ObjectiveCard> objectiveCardDeck = new Deck<>(0);
    final private Deck<ResourceCard> resourceCardDeck = new Deck<>(2);
    final private Deck<GoldCard> goldCardDeck = new Deck<>(2);
    final private Deck<StartCard> startingCardDeck = new Deck<>(0);
    final private List<User> users; //played by
    private User currentPlayer;
    private final int numberOfPlayer;
    public Game(int numberOfPlayer){
        users=null;
        this.numberOfPlayer=numberOfPlayer;
    }
    public void addUser(User user){

    }

    public void removeUser(User user){

    }
    public List<User> getUsers() {
        return users;
    }

    public User getCurrentPlayer() {
        return currentPlayer;
    }

    public void nextPlayer(){
    }
}
