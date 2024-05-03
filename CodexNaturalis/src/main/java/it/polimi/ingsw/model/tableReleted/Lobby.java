package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.lightModel.diffObserverInterface.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffPublishers.LobbyDiffPublisher;

import java.io.Serializable;
import java.util.*;

public class Lobby implements Serializable {
    private final LobbyDiffPublisher lobbyDiffPublisher;
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
        lobbyDiffPublisher = new LobbyDiffPublisher();
    }
    /**
     * Handles the adding of a user to the current lobby.
     * @param userName The user to add to the lobby.
     * @throws IllegalCallerException if the number of player currently in the lobby is equal to the number of max player allowed.
     */
    public Boolean addUserName(String userName) throws IllegalCallerException {
        if (lobbyPlayerList.size() == numberOfMaxPlayer) {
            return false;
        } else {
            return lobbyPlayerList.add(userName);
        }
    }
    /**
     * Handles the removal of a user of the current game.
     * @param userName The user that need to be removed.
     * @throws IllegalArgumentException if the specified user is not found in the userList of this lobby.
     */
    public void removeUserName(String userName) throws IllegalArgumentException{
        if(!lobbyPlayerList.remove(userName)){
            throw new IllegalArgumentException("The user is not in this game");
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
        return lobbyPlayerList;
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
     * @param diffSubscriber who is joining the lobby
     * @param nickname of the diffSubscriber joining the lobby
     * @param lobbyName of the lobby being joined
     */
    public void subscribe(DiffSubscriber diffSubscriber, String nickname, String lobbyName, int numberOfMaxPlayer){
        lobbyDiffPublisher.subscribe(diffSubscriber, nickname, lobbyName, numberOfMaxPlayer);
    }

    /**
     * @param unsubscriber who is leaving the lobby
     */
    public void unsubscribe(DiffSubscriber unsubscriber){
        lobbyDiffPublisher.unsubscribe(unsubscriber);
    }

    /**
     * @return a list of all DiffSubscriber present in the lobby
     */
    public List<DiffSubscriber> getSubscribers(){
        return lobbyDiffPublisher.getSubscribers();
    }
}
