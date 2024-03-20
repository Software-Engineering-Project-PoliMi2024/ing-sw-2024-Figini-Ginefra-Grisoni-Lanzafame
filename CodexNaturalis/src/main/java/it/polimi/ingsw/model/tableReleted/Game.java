package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.model.cardReleted.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.ResourceCard;
import it.polimi.ingsw.model.cardReleted.GoldCard;
import it.polimi.ingsw.model.cardReleted.StartCard;

public abstract class Game {
    private Deck<ObjectiveCard> objectiveCardDeck = new Deck<ObjectiveCard>(0);
    private Deck<ResourceCard> resourceCardDeck = new Deck<ResourceCard>(2);
    private Deck<GoldCard> goldCardDeck = new Deck<GoldCard>(2);
    private Deck<StartCard> startingCardDeck = new Deck<StartCard>(0);
    private List<User> users; //played by
    private User currentPlayer;
    private final int numberOfPlayer;
    public Game(int numberOfPlayer){
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
