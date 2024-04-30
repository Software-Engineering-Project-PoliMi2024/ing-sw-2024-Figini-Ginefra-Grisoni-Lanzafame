package it.polimi.ingsw.model;

import it.polimi.ingsw.controller2.ServerModelController;
import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffObserverInterface.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffPublishers.LobbyListDiffPublisher;
import it.polimi.ingsw.lightModel.diffs.LobbyListDiff;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.model.tableReleted.LobbyList;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MultiGame implements Serializable {
    private final LobbyListDiffPublisher lobbyListDiffPublisher;
    private final Set<Game> games;
    private final LobbyList lobbies;
    private final Map<ServerModelController, String> usernames;
    public MultiGame() {
        this.games = new HashSet<>();
        //usernames
        this.usernames = new HashMap<>(); //user that are connected to the server
        lobbies = new LobbyList();
        this.lobbyListDiffPublisher = new LobbyListDiffPublisher(Lightifier.lightify(lobbies));
    }

    public synchronized Set<Game> getGames() {
        return games;
    }

    public Set<String> getUsernames() {
        synchronized (usernames) {
            return new HashSet<>(usernames.values());
        }
    }

    public synchronized boolean addGame(Game game) {
        return games.add(game);
    }

    public boolean addUser(ServerModelController controller, String username) {
        synchronized (usernames){
            if(isUnique(username)){
                usernames.put(controller, username);
                return true;
            }else
                return false;
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
            return lobbies.addLobby(lobby);
    }
    public synchronized void removeLobby(Lobby lobby) {
        lobbies.remove(lobby);
    }
    public synchronized Lobby getLobby(String name) {
        return lobbies.getLobbies().stream().filter(lobby -> lobby.getLobbyName().equals(name)).findFirst().orElse(null);
    }

    public synchronized Set<Lobby> getLobbies() {
        return lobbies.getLobbies();
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
        return lobbies.getLobbies().stream().map(Lobby::getLobbyName).toArray(String[]::new);
    }
    public void subscribe(DiffSubscriber diffSubscriber) {
        lobbyListDiffPublisher.subscribe(diffSubscriber);
    }
    public void unsubscribe(DiffSubscriber diffSubscriber) {
        lobbyListDiffPublisher.unsubscribe(diffSubscriber);
    }
    public void subscribe(LobbyListDiff lightLobbyDiff){
        lobbyListDiffPublisher.subscribe(lightLobbyDiff);
    }
    /**
     * @param nickname of the player
     * @return True if the nickname is Unique, false otherwise
     */
    public boolean isUnique(String nickname){
        return !this.getUsernames().contains(nickname);
    }

    public Boolean inGame(String nickname){
        if(getUserGame(nickname)==null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * @param nickName of the player
     * @return Game if the player is in a Game, null otherwise
     */
    public Game getUserGame(String nickName){
        for(Game game : this.getGames()){
            if(game.getGameParty().getUsersList().stream().map(User::getNickname).toList().contains(nickName)){
                return game;
            }
        }
        return null;
    }
    @Override
    public String toString() {
        return "MultiGame{" +
                "games=" + games +
                ", usernames=" + usernames +
                '}';
    }
}
