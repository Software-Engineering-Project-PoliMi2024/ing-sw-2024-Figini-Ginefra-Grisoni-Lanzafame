package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.controller3.mediators.loggerAndUpdaterMediators.LobbyMediator;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.GameJoiner;
import it.polimi.ingsw.controller3.mediators.gameJoinerAndTurnTakerMediators.GameJoinerMediator;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Lobby implements Serializable {
    private final LobbyMediator lobbyMediator;
    private final GameJoinerMediator gameJoinerMediator;
    private final String lobbyName;
    final private List<String> lobbyPlayerList;
    private final int numberOfMaxPlayer;

    /**
     * @param numberOfMaxPlayer the number of player that can join the lobby
     * @param creatorNickname the diffSubscriber of the user that created the lobby
     * @param lobbyName the name of the lobby
     */
    public Lobby(int numberOfMaxPlayer, String creatorNickname, String lobbyName) {
        if(numberOfMaxPlayer < 2 || numberOfMaxPlayer > 4){
            throw new IllegalArgumentException("The number of player must be at least 1 and at most 4");
        }
        this.numberOfMaxPlayer = numberOfMaxPlayer;
        lobbyPlayerList = new ArrayList<>();
        this.lobbyName = lobbyName;
        lobbyPlayerList.add(creatorNickname);
        lobbyMediator = new LobbyMediator();
        gameJoinerMediator = new GameJoinerMediator();
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

    /**
     * Subscribes the viewInterface to the mediator
     * Updates the lobby of the subscriber with the lobby passed as parameter
     * Logs the event on the view
     * Notifies all the other subscribers that a new user has joined the lobby
     * adds the subscriber to the LoggerAndUpdater mediator
     * adds the subscriber to the turnTakersMediator
     * @param nickname the subscriber's nickname
     * @param loggerAndUpdater the logger and updater connected to the subscriber
     * @param gameJoiner the gameJoiner connected to the subscriber, used to notify the subscriber when the game starts
     */
    public void subscribe(String nickname, ViewInterface loggerAndUpdater, GameJoiner gameJoiner){
        lobbyMediator.subscribe(nickname, loggerAndUpdater, this);
        gameJoinerMediator.subscribe(nickname, gameJoiner);
    }

    /**
     * Unsubscribes the subscriber with the nickname passed as parameter
     * erases the lobby of the unSubscriber
     * notifies all the other subscribers that the unSubscriber has left the lobby
     * logs the event on the unSubscriber
     * removes the unSubscriber from the LoggerAndUpdaterMediator
     * removes the unSubscriber from the turnTakersMediator
     * @param nickname the unSubscriber's nickname
     */
    public void unsubscribe(String nickname){
        lobbyMediator.unsubscribe(nickname);
        gameJoinerMediator.unsubscribe(nickname);
    }

    public void notifyGameStart() {
        lobbyMediator.notifyGameStart();
        gameJoinerMediator.notifyGameStart();
    }
}
