package it.polimi.ingsw.model;

import it.polimi.ingsw.lightModel.diffLists.DiffPublisher;
import it.polimi.ingsw.lightModel.diffLists.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffLists.LobbyListDiffPublisher;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class MultiGame implements Serializable {
    private final LobbyListDiffPublisher lobbyListDiffPublisher;
    private final Set<Game> games;
    private final Set<Lobby> lobbies;
    private final Set<String> usernames;

    public MultiGame() {
        this.games = new HashSet<>();
        //usernames
        this.usernames = new HashSet<>(); //user that are connected to the server
        this.lobbies = new HashSet<>();
        this.lobbyListDiffPublisher = new LobbyListDiffPublisher();
    }

    public synchronized Set<Game> getGames() {
        return games;
    }

    public Set<String> getUsernames() {
        synchronized (usernames) {
            return usernames;
        }
    }

    public synchronized boolean addGame(Game game) {
        return games.add(game);
    }

    public boolean addUser(String username) {
        synchronized (usernames){
            return usernames.add(username);
        }
    }

    public synchronized Game getGame(String name) {
        return games.stream().filter(game -> game.getName().equals(name)).findFirst().orElse(null);
    }
    public synchronized void removeGame(Game game) {
        games.remove(game);
    }

    public synchronized Boolean addLobby(Lobby lobby) {
        if (getGame(lobby.getLobbyName()) != null) {
            return false;
        } else
            return lobbies.add(lobby);
    }
    public synchronized void removeLobby(Lobby lobby) {
        lobbies.remove(lobby);
    }
    public synchronized Lobby getLobby(String name) {
        return lobbies.stream().filter(lobby -> lobby.getLobbyName().equals(name)).findFirst().orElse(null);
    }

    public synchronized Set<Lobby> getLobbies() {
        return lobbies;
    }

    public void removeUser(String username) {
        synchronized (usernames){
            usernames.remove(username);
        }
    }
    /**
     * @return an array of String of each game's name
     */
    public synchronized String[] getGameNames() {
        return games.stream().map(Game::getName).toArray(String[]::new);
    }
    public synchronized String[] getLobbyNames() {
        return lobbies.stream().map(Lobby::getLobbyName).toArray(String[]::new);
    }
    public void subscribe(DiffSubscriber diffSubscriber) {
        lobbyListDiffPublisher.subscribe(diffSubscriber);
    }
    public void unsubscribe(DiffSubscriber diffSubscriber) {
        lobbyListDiffPublisher.unsubscribe(diffSubscriber);
    }
    @Override
    public String toString() {
        return "MultiGame{" +
                "games=" + games +
                ", usernames=" + usernames +
                '}';
    }
}
