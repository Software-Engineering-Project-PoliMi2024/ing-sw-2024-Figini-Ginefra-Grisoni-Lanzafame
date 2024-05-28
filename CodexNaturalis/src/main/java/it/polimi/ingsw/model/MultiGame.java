package it.polimi.ingsw.model;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller.ServerModelController;
import it.polimi.ingsw.controller.persistence.PersistenceFactory;
import it.polimi.ingsw.lightModel.diffPublishers.DiffSubscriber;
import it.polimi.ingsw.model.cardReleted.cardFactories.GoldCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.ObjectiveCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.ResourceCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.StartCardFactory;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.model.tableReleted.LobbyList;

import java.io.Serializable;
import java.util.*;

public class MultiGame implements Serializable {
    private final Set<Game> games;
    private final LobbyList lobbies;
    private final List<String> usernames;
    private final CardTable cardTable;
    public MultiGame() {
        this.games = PersistenceFactory.load();
        this.usernames = new ArrayList<>(); //users that are currently connected to the server
        lobbies = new LobbyList();
        this.cardTable = new CardTable(Configs.CardFolder, Configs.CardFile);
    }

    public synchronized Set<Game> getGames() {
        return games;
    }

    public List<String> getUsernames() {
        synchronized (usernames) {
            return usernames;
        }
    }

    public synchronized boolean addGame(Game game) {
        return games.add(game);
    }

    public boolean addUser(String username) {
        synchronized (usernames){
            if(isUnique(username)){
                this.usernames.add(username);
                return true;
            }else{
                return false;
            }
        }
    }

    public synchronized Game getGameByName(String name) {
        return games.stream().filter(game -> game.getName().equals(name)).findFirst().orElse(null);
    }
    public synchronized void removeGame(Game game) {
        games.remove(game);
    }

    public synchronized Boolean addLobby(Lobby lobby) {
        if (getGameByName(lobby.getLobbyName()) != null) {
            return false;
        } else {
            lobbies.addLobby(lobby);
            return true;
        }
    }
    public synchronized void removeLobby(Lobby lobby) {
        lobbies.remove(lobby);
    }
    public synchronized Lobby getLobbyByName(String name) {
        return lobbies.getLobbies().stream().filter(lobby -> lobby.getLobbyName().equals(name)).findFirst().orElse(null);
    }

    public synchronized Set<Lobby> getLobbies() {
        return lobbies.getLobbies();
    }

    public void removeUser(String username) {
        synchronized (this.usernames){
            this.usernames.remove(username);
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
        lobbies.subscribe(diffSubscriber);
    }
    public void unsubscribe(DiffSubscriber diffSubscriber) {
        lobbies.unsubscribe(diffSubscriber);
    }

    /**
     * @param nickname of the player
     * @return True if the nickname is Unique, false otherwise
     */
    public boolean isUnique(String nickname){
        return !this.getUsernames().contains(nickname);
    }

    /**
     * @param nickname of the user
     * @return true if the nick is already present in a game (e.g. the user disconnected while still playing a match)
     */
    public synchronized Boolean isInGameParty(String nickname){
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
    public synchronized Game getUserGame(String nickName){
        for(Game game : this.getGames()){
            if(game.getGameParty().getUsersList().stream().map(User::getNickname).toList().contains(nickName)){
                return game;
            }
        }
        return null;
    }

    /**
     * @param nickname of the user
     * @return the Lobby if the user is in a lobby, null otherwise
     */
    public synchronized Lobby getUserLobby(String nickname){
        for(Lobby lobby: lobbies.getLobbies()){
            for(String name : lobby.getLobbyPlayerList())
                if(name.equals(nickname))
                    return lobby;
        }
        return null;
    }

    /**
     * Add player to the lobbyName
     * @param lobbyName of the targetLobby
     * @param nickname of the player
     * @return true if the player is successfully added to the Lobby, false otherwise
     * @throws IllegalCallerException if the lobby doesn't exist
     */
    public Boolean addPlayerToLobby(String lobbyName, String nickname){
        Lobby lobbyToJoin = null;
        for(Lobby lobby: this.lobbies.getLobbies()){
            if(lobby.getLobbyName().equals(lobbyName)){
                lobbyToJoin=lobby;
                break;
            }
        }
        if(lobbyToJoin==null){
            throw new IllegalCallerException(lobbyName + " does not exits");
        }else{
            return lobbyToJoin.addUserName(nickname);
        }
    }

    public Game createGame(Lobby lobby){
        return new Game(lobby, cardTable.getCardLookUpObjective(), cardTable.getCardLookUpResourceCard(), cardTable.getCardLookUpStartCard(), cardTable.getCardLookUpGoldCard());
    }

    public synchronized User getUserFromNick(String nickname){
        User returnUser = null;
        List<User> userList = this.getGames().stream()
                .map(Game::getGameParty)
                .flatMap(gameParty -> gameParty.getUsersList().stream()).toList();
        for(User user : userList){
            if(user.getNickname().equals(nickname)){
                returnUser = user;
                break;
            }
        }
        if(returnUser==null){
            throw new IllegalCallerException(nickname + " does not exist");
        }else{
            return returnUser;
        }
    }

    public CardTable getCardTable() {
        return cardTable;
    }

    @Override
    public String toString() {
        return "MultiGame{" +
                "games=" + games +
                ", usernames=" + usernames +
                '}';
    }
}
