package it.polimi.ingsw.model;

import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class MultiGame implements Serializable {
    private final Set<Game> games;
    private final Set<Lobby> lobbies;
    private final Set<String> usernames;

    public MultiGame() {
        this.games = new HashSet<>();
        //usernames
        this.usernames = new HashSet<>(); //user that are connected to the server
        this.lobbies = new HashSet<>();
    }

    public synchronized Set<Game> getGames() {
        return games;
    }

    public synchronized Set<String> getUsernames() {
        return usernames;
    }

    public synchronized boolean addGame(Game game) {
        return games.add(game);
    }

    public synchronized boolean addUser(String username) {
        return usernames.add(username);
    }

    public synchronized Game getGame(String name) {
        return games.stream().filter(game -> game.getName().equals(name)).findFirst().orElse(null);
    }

    public synchronized void removeGame(Game game) {
        games.remove(game);
    }

    public void removeUser(String username) {
        usernames.remove(username);
    }

    /**
     * @return an array of String of each game's name
     */
    public String[] getGameNames() {
        return games.stream().map(Game::getName).toArray(String[]::new);
    }

    @Override
    public String toString() {
        return "MultiGame{" +
                "games=" + games +
                ", usernames=" + usernames +
                '}';
    }
}
