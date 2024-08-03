package it.polimi.ingsw.model.tableReleted;

import java.io.Serializable;
import java.util.*;

/**
 * This class is used to represent a lobby in the game.
 * A lobby is a group of players that are waiting to start a game.
 */
public class Lobby implements Serializable {
    /** the name of the lobby */
    private final String lobbyName;
    /** the list of players in the lobby */
    final private List<String> lobbyPlayerList;
    /** the number of max player that can join the lobby, when is reached the game can start */
    private final int numberOfMaxPlayer;

    /** the number of agents in the lobby */
    private final int numberOfAgents;

    /**
     * @param numberOfMaxPlayer the number of player that can join the lobby
     * @param lobbyName the name of the lobby
     */
    public Lobby(int numberOfMaxPlayer, String lobbyName, int numberOfAgents) {
        if(numberOfAgents + numberOfMaxPlayer < 2 || numberOfAgents + numberOfMaxPlayer > 4){
            throw new IllegalArgumentException("The number of player must be at least 1 and at most 4");
        }
        this.numberOfMaxPlayer = numberOfMaxPlayer;
        this.lobbyName = lobbyName;
        this.numberOfAgents = numberOfAgents;

        lobbyPlayerList = new ArrayList<>();
    }

    /**
     * Copy constructor
     * @param other the lobby to copy
     */
    public Lobby(Lobby other){
        this.numberOfMaxPlayer = other.numberOfMaxPlayer;
        this.lobbyPlayerList = new ArrayList<>(other.lobbyPlayerList);
        this.lobbyName = other.lobbyName;
        this.numberOfAgents = other.numberOfAgents;
    }
    /**
     * Handles the adding of a user to the current lobby.
     * @param userName The user to add to the lobby.
     * @throws IllegalCallerException if the number of player currently in the lobby is equal to the number of max player allowed.
     */
    public Boolean addUserName(String userName) throws IllegalCallerException {
        synchronized (lobbyPlayerList) {
            if (lobbyPlayerList.size() == numberOfMaxPlayer) {
                return false;
            } else {
                return lobbyPlayerList.add(userName);
            }
        }
    }
    /**
     * Handles the removal of a user of the current game.
     * @param userName The user that need to be removed.
     * @throws IllegalArgumentException if the specified user is not found in the userList of this lobby.
     */
    public void removeUserName(String userName) throws IllegalArgumentException{
        synchronized (lobbyPlayerList) {
            if (!lobbyPlayerList.remove(userName)) {
                throw new IllegalArgumentException("The user is not in this game");
            }
        }
    }

    /**
     * @return the number of player needed to start the game
     */
    public int getNumberOfMaxPlayer() {
        return numberOfMaxPlayer;
    }

    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    /**
     * @return a list containing all the players' nick in the lobby
     */
    public List<String> getLobbyPlayerList() {
        synchronized (lobbyPlayerList) {
            return lobbyPlayerList;
        }
    }

    /**
     * @return the name of the lobby duh
     */
    public String getLobbyName() {
        return lobbyName;
    }
    @Override
    public boolean equals(Object obj){
        return obj instanceof Lobby && ((Lobby) obj).lobbyName.equals(lobbyName);
    }

}
