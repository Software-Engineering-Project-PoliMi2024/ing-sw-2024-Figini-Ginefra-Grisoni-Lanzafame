package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.model.playerReleted.PawnColors;
import it.polimi.ingsw.model.playerReleted.Player;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * This class is used to manage the players in the game
 */
public class GameParty implements Serializable {
    /** the list of the players in the game */
    final private List<Player> playerList; //the player that were in the lobby pre game
    /** the index of the current player */
    private int currentPlayerIndex;
    /** the chat manager of the game */
    final private ChatManager chatManager;
    /** the list of the pawn colors that are still available */
    private final List<PawnColors> pawnChoices;

    /** the lock used to manage the currentPlayerIndex race conditions*/
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
        this.pawnChoices = new ArrayList<>(Arrays.stream(PawnColors.values()).toList());
        playerList = players.stream().map(Player::new).collect(Collectors.toList());
        currentPlayerIndex = 0;
        chatManager = new ChatManager(playerNames);
    }

    /**
     * This method is used to get the current player that is supposed to play
     * @return the current player
     */
    public Player getCurrentPlayer() {
        currentPlayerLock.lock();
        try {
            return playerList.get(currentPlayerIndex);
        } finally {
            currentPlayerLock.unlock();
        }
    }

    /** @return the list of the pawn colors that are still available */
    public List<PawnColors> getPawnChoices() {
        synchronized (pawnChoices) {
            return pawnChoices;
        }
    }

    /** @param color the color of the pawn to remove from the list of available pawn colors */
    public void removeChoice(PawnColors color){
        synchronized (pawnChoices) {
            if (pawnChoices.contains(color)) {
                pawnChoices.remove(color);
            } else {
                throw new RuntimeException("Game.removeChoice pawnColor is not present in the list");
            }
        }
    }

    /** @return a map containing the nickname of the players and their points */
    public Map<String, Integer> getPointPerPlayerMap(){
        Map<String, Integer> userPointsMap = new HashMap<>();
        synchronized (playerList) {
            playerList.forEach(user ->{
                userPointsMap.put(user.getNickname(), user.getUserCodex().getPoints());
            });
        }
        return userPointsMap;
    }

    /** @return list of the users in this match*/
    public List<Player> getPlayersList() {
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
    public Player getPlayerFromNick(String nickname){
        synchronized (playerList) {
            if(!playerList.stream().map(Player::getNickname).toList().contains(nickname))
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
    public void removePlayer(String user){
        synchronized (playerList) {
            Player playerToRemove = playerList.stream().filter(u -> u.getNickname().equals(user)).findFirst().orElse(null);
            if (playerToRemove != null)
                playerList.remove(playerToRemove);
            else
                throw new IllegalArgumentException("User not in this gameParty");
        }
    }

    /** @return the index of the current player */
    public int getCurrentPlayerIndex() {
        synchronized (currentPlayerLock){
            return currentPlayerIndex;
        }
    }

    /** @param currentPlayerIndex the index of the current player to set */
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        synchronized (currentPlayerLock){
            this.currentPlayerIndex = currentPlayerIndex;
        }
    }

    /** @return the chat manager of the game */
    public ChatManager getChatManager() {
        return chatManager;
    }
}
