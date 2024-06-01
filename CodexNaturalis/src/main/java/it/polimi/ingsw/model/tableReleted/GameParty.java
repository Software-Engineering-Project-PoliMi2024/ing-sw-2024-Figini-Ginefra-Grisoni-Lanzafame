package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.model.playerReleted.User;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class GameParty implements Serializable {
    final private List<User> playerList; //the player that were in the lobby pre game
    private int currentPlayerIndex;

    private final ReentrantLock currentPlayerLock = new ReentrantLock();

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
        currentPlayerLock.lock();
        try {
            currentPlayerIndex = index;
        } finally {
            currentPlayerLock.unlock();
        }
    }

    /**
     * This method is used to get the current player that is supposed to play
     * @return the current player
     */
    public User getCurrentPlayer() {
        currentPlayerLock.lock();
        try {
            return playerList.get(currentPlayerIndex);
        } finally {
            currentPlayerLock.unlock();
        }
    }

    public Map<String, Integer> getPointPerPlayerMap(){
        Map<String, Integer> userPointsMap = new HashMap<>();
        currentPlayerLock.lock();
        try {
            playerList.forEach(user ->{
                userPointsMap.put(user.getNickname(), user.getUserCodex().getPoints());
            });
        } finally {
            currentPlayerLock.unlock();
        }
        return userPointsMap;
    }

    /**
     * This method is used to get the index of the next player that is supposed to play
     * not considering the possible disconnection that may have occurred
     * @return the index of the next player
     */
    public int getNextPlayerIndex() {
        currentPlayerLock.lock();
        try{
            return (currentPlayerIndex + 1) % getNumberOfMaxPlayer();
        } finally {
            currentPlayerLock.unlock();
        }
    }

    /** @return list of the users in this match*/
    public List<User> getUsersList() {
        currentPlayerLock.lock();
        try{
            return new ArrayList<>(playerList);
        } finally {
            currentPlayerLock.unlock();
        }
    }

    /***
     * @return the number Of Player needed to start the game
     */
    public int getNumberOfMaxPlayer() {
        currentPlayerLock.lock();
        try{
            return playerList.size();
        } finally {
            currentPlayerLock.unlock();
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
            currentPlayerLock.lock();
            try{
                if(!playerList.stream().map(User::getNickname).toList().contains(nickname))
                    return null;
                else{
                    return playerList.stream().filter(u -> u.getNickname().equals(nickname)).findFirst().orElse(null);
                }
            }finally {
                currentPlayerLock.unlock();
            }
        }
    }

    /**
     * Remove a user from the gameParty
     * @param user being removed
     */
    public void removeUser(String user){
        currentPlayerLock.lock();
        try{
            User userToRemove = playerList.stream().filter(u -> u.getNickname().equals(user)).findFirst().orElse(null);
            if (userToRemove != null)
                playerList.remove(userToRemove);
            else
                throw new IllegalArgumentException("User not in this gameParty");
        }finally {
            currentPlayerLock.unlock();
        }
    }

    /**
     * This method is used to get the player from its index
     * @param index the index of the player
     * @return the player with the given index
     */
    public User getPlayerFromIndex(int index){
        currentPlayerLock.lock();
        try{
            return playerList.get(index);
        }finally {
            currentPlayerLock.unlock();
        }
    }
}
