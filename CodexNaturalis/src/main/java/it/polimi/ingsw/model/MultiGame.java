package it.polimi.ingsw.model;

import it.polimi.ingsw.model.tableReleted.Game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiGame {
    private final Set<Game> games;
    private final Set<String> usernames;

    public MultiGame() {
        this.games = new HashSet<>();
        this.usernames = new HashSet<>();
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
