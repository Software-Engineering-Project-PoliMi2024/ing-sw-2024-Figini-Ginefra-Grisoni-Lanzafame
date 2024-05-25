package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.model.playerReleted.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GameParty implements Serializable {
    final private List<User> playerList; //the player that were in the lobby pre game
    final private List<User> activePlayers; //the player that are currently playing in the game
    private int currentPlayerIndex;

    /**
     * The constructor of the class
     * playerList is shuffled to create a random order of the players
     * all players are considered active at the creation of the gameParty
     * @param playerNames list of all players nicks' who can join this game
     */
    public GameParty(List<String> playerNames) {
        ArrayList<String> players = new ArrayList<>(playerNames);
        Collections.shuffle(players);
        playerList = players.stream().map(User::new).collect(Collectors.toList());
        currentPlayerIndex = -1;
        activePlayers = new ArrayList<>(playerList);
    }

    /**
     * This method advances the game to the next player in the rotation sequence.
     * if there is no currentPlayer, it creates it by launching the chooseStartingOrder method
     * @throws IllegalCallerException if the game is empty.
     */
    public User nextPlayer() throws IllegalCallerException {
        if(playerList.isEmpty()){
            throw new IllegalCallerException("The game is empty, there is no next player");
        } else {
            currentPlayerIndex = (currentPlayerIndex + 1) % playerList.size();
            return playerList.get(currentPlayerIndex);
        }
    }
    /** @return list of the players in this match*/
    public List<User> getUsersList() {
        return new LinkedList<>(playerList);
    }

    /***
     * @return the number Of Player needed to start the game
     */
    public int getNumberOfMaxPlayer() {
        return playerList.size();
    }

    /**
     * Remove a user from the gameParty
     * @param user being removed
     */
    public void removeUser(User user){
        playerList.remove(user);
    }

    public User getFirstPlayerInOrder() {
        return new User(playerList.getFirst());
    }

    public User getCurrentPlayer() {
        return playerList.get(currentPlayerIndex);
    }
    public List<String> getActivePlayers() {
        return activePlayers.stream().map(User::getNickname).toList();
    }

    public void addActivePlayer(User user){
        activePlayers.add(user);
    }

    public void removeActivePlayer(User user){
        activePlayers.remove(user);
    }
}
