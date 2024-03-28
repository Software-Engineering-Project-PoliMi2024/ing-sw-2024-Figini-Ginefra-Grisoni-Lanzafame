package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.model.cardReleted.*;
import it.polimi.ingsw.model.playerReleted.User;

import java.util.*;

/**
 * @author Samuele
 * This class represents the game as a whole.
 * It contains a reference to all the players who are playing the match
 * and the decks that are being used.
 */
public class Game {
    final private CardFactory cardFactory = new CardFactory();
    final private Deck<ObjectiveCard> objectiveCardDeck =
            new Deck<>(0, cardFactory.getFactoryOf(ObjectiveCard.class).getCards());
    final private Deck<ResourceCard> resourceCardDeck =
            new Deck<>(2, cardFactory.getFactoryOf(ResourceCard.class).getCards());
    final private Deck<GoldCard> goldCardDeck =
            new Deck<>(2, cardFactory.getFactoryOf(GoldCard.class).getCards());
    final private Deck<StartCard> startingCardDeck =
            new Deck<>(0, cardFactory.getFactoryOf(StartCard.class).getCards());
    final private List<User> usersList; //played by
    private User currentPlayer;
    private int currentPlayerIndex;
    private final int numberOfMaxPlayer;

    /**
     * Constructs a new Game instance with a specified maximum number of players.
     *
     * @param numberOfMaxPlayer The maximum amount of player allow in this match.
     */
    public Game(int numberOfMaxPlayer) {
        usersList = new ArrayList<>();
        this.numberOfMaxPlayer = numberOfMaxPlayer;
        currentPlayer = null;
    }

    /**
     * Handles the adding of a user to the current game.
     * If the game is empty, the first player added will become the current player.
     * @param user The user to add to the game.
     * @throws FullMatchException if the number of player currently in the game is equal to the number of max player allowed.
     */
    public void addUser(User user) throws FullMatchException {
        if (usersList.size() == numberOfMaxPlayer) {
            throw new FullMatchException("The match is already full");
        } else {
            usersList.add(user);
        }
    }

    /**
     * Handles the removal of a user of the current game.
     *
     * @param user The user that need to be removed.
     * @throws EmptyMatchException if, after the removal of the player, the game is left without users.
     * @throws UserNotFoundException if the specified user is not found in the userList of this game.
     */
    public void removeUser(User user) throws EmptyMatchException, UserNotFoundException{
        if(!usersList.remove(user)){
            throw new UserNotFoundException("The user is not in this game");
        }else{
            if (usersList.isEmpty()) {
                currentPlayer=null;
                throw new EmptyMatchException("There are more not active player in this game");
            }
            assert user != null;        //handle the case of user being null while currentPlayer is null
            if(user==currentPlayer){
                nextPlayer();
            }
        }
    }

    /**
     * This method advances the game to the next player in the rotation sequence.
     * if there is no currentPlayer, it creates it by launching the chooseStartingOrder method
     * @throws EmptyMatchException if the game is empty.
     */
    public void nextPlayer() throws EmptyMatchException {
        if(usersList.isEmpty()){
            throw new EmptyMatchException("The game is empty, there is no next player");
        }
        if (currentPlayer == null) {
            chooseStartingOrder();
        } else {
            currentPlayerIndex = (currentPlayerIndex + 1) % usersList.size();
            currentPlayer = usersList.get(currentPlayerIndex);
        }
    }

    /**
     * Randomize the userList and set the currentPlayer to the first of the list
     */
    public void chooseStartingOrder(){
        Collections.shuffle(usersList);
        currentPlayerIndex = 0;
        currentPlayer=usersList.getFirst();
    }

    /** @return list of active player in this match*/
    public List<User> getUsersList() {
        return usersList;
    }

    /** @return the Objective Card Deck*/
    public Deck<ObjectiveCard> getObjectiveCardDeck() {
        return objectiveCardDeck;
    }

    /** @return the Resource Card Deck*/
    public Deck<ResourceCard> getResourceCardDeck() {
        return resourceCardDeck;
    }

    /** @return the Gold Card Deck*/
    public Deck<GoldCard> getGoldCardDeck() {
        return goldCardDeck;
    }

    /** @return the Starting Card Deck*/
    public Deck<StartCard> getStartingCardDeck() {
        return startingCardDeck;
    }

    /** @return the current Player*/
    public User getCurrentPlayer() {
        return currentPlayer;
    }
    @Override
    public String toString() {
        StringBuilder userListName = new StringBuilder();
        for (User user : usersList) {
            userListName.append(user.getNickname()).append(", ");
        }
        return "Game{" +
                ", usersList=" + userListName +
                ", currentPlayer=" + currentPlayer.getNickname() +
                ", numberOfMaxPlayer=" + numberOfMaxPlayer +
                '}';
    }
}
