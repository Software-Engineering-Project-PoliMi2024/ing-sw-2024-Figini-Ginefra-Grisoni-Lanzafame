package it.polimi.ingsw.model;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller.persistence.PersistenceFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.GoldCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.ObjectiveCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.ResourceCardFactory;
import it.polimi.ingsw.model.cardReleted.cardFactories.StartCardFactory;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.playerReleted.User;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.model.tableReleted.LobbyList;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.util.*;

public class MultiGame implements Serializable {
    private final Set<Game> games;
    private final LobbyList lobbies; //users that are currently connected to the server
    private final List<String> usernames;
    private final CardTable cardTable;
    public MultiGame() {
        this.games = PersistenceFactory.load();
        this.usernames = new ArrayList<>();
        lobbies = new LobbyList();
        this.cardTable = new CardTable(Configs.CardFolder, Configs.CardFile);
    }

    /**
     * @return the set of games that are were created
     */
    public Set<Game> getGames() {
        synchronized (usernames) {
            return games;
        }
    }

    public List<String> getUsernames() {
        synchronized (usernames) {
            return usernames;
        }
    }

    /**
     * Adds a game to the list of games in the MultiGame
     * @param game the game to be added
     */
    public void addGame(Game game) {
        synchronized (games) {
            games.add(game);
        }
    }

    /**
     * Adds a user to the list of usernames in the MultiGame
     * @param username the username to be added
     */
    public void addUser(String username) {
        synchronized (usernames){
            this.usernames.add(username);
        }
    }

    /**
     * returns the Game given the gameName
     * @param name the name of the game
     * @return the game with the given name
     */
    public Game getGameByName(String name) {
        synchronized (games) {
            return games.stream().filter(game -> game.getName().equals(name)).findFirst().orElse(null);
        }
    }

    /**
     * Removes a game from the list of games in the MultiGame
     * @param game the game to be removed
     */
    public void removeGame(Game game) {
        synchronized(games) {
            games.remove(game);
        }
    }

    /**
     * Adds a lobby to the list of lobbies in the MultiGame
     * @param lobby the lobby ti add
     */
    public void addLobby(Lobby lobby) {
        synchronized (lobbies) {
            if (getGameByName(lobby.getLobbyName()) == null) {
                lobbies.addLobby(lobby);
            }
        }
    }

    /**
     * Remove a lobby from the list of lobbies in the MultiGame
     * @param lobby the lobby to remove
     */
    public void removeLobby(Lobby lobby) {
        synchronized(lobbies){
            lobbies.remove(lobby);
        }
    }

    /**
     * get the lobby given the name
     * @param name the name of the lobby
     * @return the lobby with the given name
     */
    public Lobby getLobbyByName(String name) {
        synchronized(lobbies){
            return lobbies.getLobbies().stream().filter(lobby -> lobby.getLobbyName().equals(name)).findFirst().orElse(null);
        }
    }

    /**
     * @return the set of lobbies that are currently active
     */
    public Set<Lobby> getLobbies() {
        synchronized (lobbies){
            return lobbies.getLobbies();
        }
    }

    /**
     * Removes a user from the list of usernames in the MultiGame
     * @param username the username to be removed
     */
    public void removeUser(String username) {
        synchronized (usernames){
            this.usernames.remove(username);
        }
    }

    /**
     * @return an array of String of each game's name
     */
    public String[] getGameNames() {
        synchronized (games) {
            return games.stream().map(Game::getName).toArray(String[]::new);
        }
    }
    public String[] getLobbyNames() {
        synchronized (lobbies) {
            return lobbies.getLobbies().stream().map(Lobby::getLobbyName).toArray(String[]::new);
        }
    }
    /**
     * Subscribes viewInterface to the mediator
     * passes the lobbyHistory to the mediator
     * the viewInterface is updated with the lobbyHistory and the logs of the join
     * @param nickname the subscriber's nickname
     * @param loggerUpdater the logger connected to the subscriber
     */
    public void subscribe(String nickname, ViewInterface loggerUpdater) {
        lobbies.subscribe(nickname, loggerUpdater);
    }
    /**
     * Unsubscribes the subscriber with the nickname
     * passed as parameter from the lobbyList mediator
     * @param nickname the unSubscriber's nickname
     */
    public void unsubscribe(String nickname) {
        lobbies.unsubscribe(nickname);
    }
    /**
     * Notifies all the subscribers that a lobby has been added
     * Adds the lobby to the lightLobbyList connected to the updater
     * @param creator the subscriber that created the lobby
     * @param addedLobby the lobby added to the lobbyList
     */
    public void notifyNewLobby(String creator, Lobby addedLobby) {
        lobbies.notifyNewLobby(creator, addedLobby);
    }
    /**
     * Notifies the subscriber that removed the lobby
     * Removes the lobby from the lightLobbyList connected to the updater
     * @param destroyer the subscriber that removed the lobby
     * @param removedLobby the lobby removed from the lobbyList
     */
    public void notifyLobbyRemoved(String destroyer, Lobby removedLobby) {
        lobbies.notifyLobbyRemoved(destroyer, removedLobby);
    }

    /**
     * @param nickname of the player
     * @return True if the nickname is Unique, false otherwise
     */
    public boolean isUnique(String nickname){
        synchronized (usernames){
            return !this.getUsernames().contains(nickname);
        }
    }

    /**
     * @param nickname of the user
     * @return true if the nick is already present in a game (e.g. the user disconnected while still playing a match)
     */
    public Boolean isInGameParty(String nickname){
        synchronized (games) {
            return getGameFromUserNick(nickname) != null;
        }
    }

    /**
     * return the Game in which the player was playing before disconnecting
     * @param nickName of the player
     * @return the game in which the player is play, null otherwise
     */
    public Game getGameFromUserNick(String nickName){
        synchronized (games) {
            for (Game game : this.getGames()) {
                if (game.getUserFromNick(nickName) != null) {
                    return game;
                }
            }
            return null;
        }
    }

    /**
     * returns the lobby in which the user is
     * @param nickname of the user
     * @return the Lobby if the user is in a lobby, null otherwise
     */
    public Lobby getUserLobby(String nickname){
        synchronized (lobbies) {
            for (Lobby lobby : lobbies.getLobbies()) {
                for (String name : lobby.getLobbyPlayerList())
                    if (name.equals(nickname))
                        return lobby;
            }
            return null;
        }
    }

    /**
     * check if the user is in a lobby
     * @param nickname of the user
     * @return true if the user is in a lobby, false otherwise
     */
    public boolean isInLobby(String nickname){
        synchronized (lobbies) {
            return getUserLobby(nickname) != null;
        }
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

    /**
     * Create a new game from the lobby
     * @param lobby the lobby from which the game is created
     * @return the game created
     */
    public Game createGame(Lobby lobby){
        return new Game(lobby, cardTable.getCardLookUpObjective(), cardTable.getCardLookUpResourceCard(), cardTable.getCardLookUpStartCard(), cardTable.getCardLookUpGoldCard());
    }

    /**
     * Get the user from the nickname
     * @param nickname of the user
     * @return the user with the given nickname
     */
    public User getUserFromNick(String nickname) {
        User returnUser = null;
        List<User> userList;
        synchronized (games) {
             userList = this.getGames().stream()
                    .map(Game::getGameParty)
                    .flatMap(gameParty -> gameParty.getUsersList().stream()).toList();
        }
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
