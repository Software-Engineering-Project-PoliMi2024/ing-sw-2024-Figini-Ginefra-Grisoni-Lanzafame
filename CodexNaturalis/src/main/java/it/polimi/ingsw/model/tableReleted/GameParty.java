package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.TurnTaker;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.TurnTakerMediator;
import it.polimi.ingsw.model.playerReleted.User;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class GameParty implements Serializable {
    final private List<User> playerList; //the player that were in the lobby pre game
    private int currentPlayerIndex;
    private final TurnTakerMediator activeTurnTakerMediator = new TurnTakerMediator();

    /**
     * The constructor of the class
     * playerList is shuffled to create a random order of the players
     * all players are considered inactive at the creation of the gameParty
     * @param playerNames list of all players nicks' who can join this game
     */
    public GameParty(List<String> playerNames) {
        ArrayList<String> players = new ArrayList<>(playerNames);
        Collections.shuffle(players);
        playerList = players.stream().map(User::new).collect(Collectors.toList());
        currentPlayerIndex = 0;
    }

    /**
     * This method is used to set the player index in the game
     * @param index the index of the player that is playing
     */
    public void setPlayerIndex(int index){
        synchronized (playerList){
            currentPlayerIndex = index;
        }
    }

    /**
     * This method is used to get the current player that is supposed to play
     * @return the current player
     */
    public User getCurrentPlayer() {
        synchronized (playerList) {
            return playerList.get(currentPlayerIndex);
        }
    }

    /**
     * This method is used to get the index of the next player that is supposed to play
     * not considering the possible disconnection that may have occurred
     * @return the index of the next player
     */
    public int getNextPlayerIndex() {
        return (currentPlayerIndex % getNumberOfMaxPlayer());
    }

    /** @return list of the users in this match*/
    public List<User> getUsersList() {
        synchronized (playerList) {
            return playerList;
        }
    }

    /**
     * This method is used to get the user from its nickname
     * if the user is not in the gameParty, it returns null
     * @param nickname the nickname of the user
     * @return the user with the given nickname; returns null if the user is not in the gameParty
     */
    public User getUserFromNick(String nickname){
        synchronized (playerList) {
            if(!playerList.stream().map(User::getNickname).toList().contains(nickname))
                return null;
            else{
                return playerList.stream().filter(u -> u.getNickname().equals(nickname)).findFirst().orElse(null);
            }
        }
    }

    /***
     * @return the number Of Player needed to start the game
     */
    public int getNumberOfMaxPlayer() {
        synchronized (playerList) {
            return playerList.size();
        }
    }

    /**
     * Remove a user from the gameParty
     * @param user being removed
     */
    public void removeUser(String user){
        synchronized (playerList) {
            User userToRemove = playerList.stream().filter(u -> u.getNickname().equals(user)).findFirst().orElse(null);
            if (userToRemove != null)
                playerList.remove(userToRemove);
            else
                throw new IllegalArgumentException("User not in this gameParty");
        }
    }

    /**
     * subscribe a turnTaker to the activeTurnTakerMediator in order to receive notification
     * on when it is their turn to play
     * @param nickname the nickname of the player that is subscribing
     * @param turnTaker the turnTaker that is subscribing
     */
    public void subscribe(String nickname, TurnTaker turnTaker){
        activeTurnTakerMediator.subscribe(nickname, turnTaker);
    }

    /**
     * unsubscribe a player from the activeTurnTakerMediator
     * removing them from the list of players that will receive notifications
     * @param nickname the nickname of the player that is unsubscribing
     */
    public void unsubscribe(String nickname){
        activeTurnTakerMediator.unsubscribe(nickname);
    }

    /**
     * notify a player that it is their turn to play by
     * calling the takeTurnMediator method of the subscriber
     * @param nickname the nickname of the player that is being notified
     */
    public void notifyTurn(String nickname){
        activeTurnTakerMediator.notifyTurn(nickname);
    }

    /**
     * get the list of active players
     * @return the list of active players
     */
    public List<String> getActivePlayers(){
        return activeTurnTakerMediator.getActivePlayers();
    }


//TODO to remove
    public User getFirstPlayerInOrder() {
        return new User(playerList.getFirst());
    }

    /**
     * This method advances the game to the next player in the rotation sequence.
     * if there is no currentPlayer, it creates it by launching the chooseStartingOrder method
     * @throws IllegalCallerException if the game is empty.
     */
    public User nextPlayer() {
        synchronized (playerList) {
            if (playerList.isEmpty()) {
                throw new IllegalCallerException("The game is empty, there is no next player");
            } else {
                currentPlayerIndex = getNextPlayerIndex();
                return playerList.get(currentPlayerIndex);
            }
        }
    }
}
