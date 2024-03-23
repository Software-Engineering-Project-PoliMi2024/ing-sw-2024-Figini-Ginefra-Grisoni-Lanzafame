package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.model.cardReleted.ObjectiveCard;
import it.polimi.ingsw.model.cardReleted.ResourceCard;
import it.polimi.ingsw.model.cardReleted.GoldCard;
import it.polimi.ingsw.model.cardReleted.StartCard;
import it.polimi.ingsw.model.playerReleted.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Samuele
 * This class represents the game as a whole.
 * It contains a reference to all the players who are playing the match
 * and the decks that are being used.
 */
public class Game {
    final private Deck<ObjectiveCard> objectiveCardDeck = new Deck<>(0);
    final private Deck<ResourceCard> resourceCardDeck = new Deck<>(2);
    final private Deck<GoldCard> goldCardDeck = new Deck<>(2);
    final private Deck<StartCard> startingCardDeck = new Deck<>(0);
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
        } else if (usersList.isEmpty()) {
            usersList.add(user);
            currentPlayerIndex = 0;
            currentPlayer = usersList.get(currentPlayerIndex);
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
            if(user==currentPlayer){
                nextPlayer();
            }
        }
    }

    /**
     * This method advances the game to the next player in the rotation sequence.
     *
     * @throws EmptyMatchException if the game is empty, meaning there is no current player to advance.
     */
    public void nextPlayer() throws EmptyMatchException {
        if (currentPlayer == null) {
            throw new EmptyMatchException("The game is empty, there is no next player");
        } else {
            currentPlayerIndex = (currentPlayerIndex + 1) % usersList.size();
            currentPlayer = usersList.get(currentPlayerIndex);
        }
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
                ", currentPlayerIndex=" + currentPlayerIndex +
                ", numberOfMaxPlayer=" + numberOfMaxPlayer +
                '}';
    }
}
