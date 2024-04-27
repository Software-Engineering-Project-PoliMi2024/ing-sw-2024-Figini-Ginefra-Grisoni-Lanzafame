package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.controller.socket.messages.observers.ServerMsgObserved;
import it.polimi.ingsw.controller.socket.messages.observers.ServerMsgObserver;
import it.polimi.ingsw.controller.socket.messages.serverMessages.ServerMsg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lobby implements ServerMsgObserved {
    final List<ServerMsgObserver> observers = new ArrayList<>();
    private final String lobbyName;
    final private List<String> lobbyPlayerList;
    private final int numberOfMaxPlayer;

    /**
     * @param numberOfMaxPlayer the number of player that can join the lobby
     * @param userNameCreator  the name of the user that created the lobby
     * @param lobbyName       the name of the lobby
     */
    public Lobby(int numberOfMaxPlayer, String userNameCreator, String lobbyName) {
        if(numberOfMaxPlayer < 2 || numberOfMaxPlayer > 4){
            throw new IllegalArgumentException("The number of player must be at least 1 and at most 4");
        }
        this.numberOfMaxPlayer = numberOfMaxPlayer;
        lobbyPlayerList = new ArrayList<>();
        lobbyPlayerList.add(userNameCreator);
        this.lobbyName = lobbyName;
    }
    /**
     * Handles the adding of a user to the current lobby.
     * @param userName The user to add to the lobby.
     * @throws IllegalCallerException if the number of player currently in the lobby is equal to the number of max player allowed.
     */
    public void addUserName(String userName) throws IllegalCallerException {
        if (lobbyPlayerList.size() == numberOfMaxPlayer) {
            throw new IllegalCallerException("The match is already full");
        } else {
            lobbyPlayerList.add(userName);
        }
    }
    /**
     * Handles the removal of a user of the current game.
     *
     * @param userName The user that need to be removed.
     * @throws IllegalArgumentException if the specified user is not found in the userList of this lobby.
     */
    public void removeUserName(String userName) throws IllegalArgumentException{
        if(!lobbyPlayerList.remove(userName)){
            throw new IllegalArgumentException("The user is not in this game");
        }
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public List<String> getLobbyList() {
        return lobbyPlayerList;
    }
    public void attach(ServerMsgObserver observer){
        observers.add(observer);
    }
    public void detach(ServerMsgObserver observer){
        observers.remove(observer);
    }
    public void notifyObservers(ServerMsg serverMsg){
        for(ServerMsgObserver observer : observers){
            observer.update(serverMsg);
        }
    }
    @Override
    public boolean equals(Object obj){
            return obj instanceof Lobby && ((Lobby) obj).lobbyName.equals(lobbyName);
    }
}
