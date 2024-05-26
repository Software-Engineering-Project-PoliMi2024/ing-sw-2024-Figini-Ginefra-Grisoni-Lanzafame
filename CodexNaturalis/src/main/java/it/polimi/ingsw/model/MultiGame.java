package it.polimi.ingsw.model;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.controller.ServerModelController;
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
    private final CardLookUp<ObjectiveCard> cardLookUpObjective;
    private final CardLookUp<StartCard> cardLookUpStartCard;
    private final CardLookUp<ResourceCard> cardLookUpResourceCard;
    private final CardLookUp<GoldCard> cardLookUpGoldCard;
    public MultiGame() {
        this.games = PersistenceFactory.load();
        this.usernames = new ArrayList<>();
        lobbies = new LobbyList();
        String filePath = Configs.CardFolder;
        String sourceFileName = Configs.CardFile;
        cardLookUpObjective =
                new CardLookUp<>(new ObjectiveCardFactory(filePath+sourceFileName, filePath).getCards(Configs.objectiveCardBinFileName));
        cardLookUpStartCard =
                new CardLookUp<>(new StartCardFactory(filePath+sourceFileName, filePath).getCards(Configs.startCardBinFileName));
        cardLookUpResourceCard =
                new CardLookUp<>(new ResourceCardFactory(filePath+sourceFileName, filePath).getCards(Configs.resourceCardBinFileName));
        cardLookUpGoldCard =
                new CardLookUp<>(new GoldCardFactory(filePath+sourceFileName, filePath).getCards(Configs.goldCardBinFileName));
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
        return !this.getUsernames().contains(nickname);
    }

    /**
     * @param nickname of the user
     * @return true if the nick is already present in a game (e.g. the user disconnected while still playing a match)
     */
    public synchronized Boolean isInGameParty(String nickname){
        return getUserGame(nickname) != null;
    }

    /**
     * @param nickName of the player
     * @return Game if the player is in a Game, null otherwise
     */
    public synchronized Game getUserGame(String nickName){
        for(Game game : this.getGames()){
            if(game.getUserFromNick(nickName) != null){
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
    public synchronized boolean isInLobby(String nickname){
        return getUserLobby(nickname)!=null;
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
        return new Game(lobby, cardLookUpObjective, cardLookUpResourceCard, cardLookUpStartCard, cardLookUpGoldCard);
    }

    public CardLookUp<ObjectiveCard> getCardLookUpObjective() {
        return cardLookUpObjective;
    }

    public CardLookUp<StartCard> getCardLookUpStartCard() {
        return cardLookUpStartCard;
    }

    public CardLookUp<ResourceCard> getCardLookUpResourceCard() {
        return cardLookUpResourceCard;
    }

    public CardLookUp<GoldCard> getCardLookUpGoldCard() {
        return cardLookUpGoldCard;
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



    @Override
    public String toString() {
        return "MultiGame{" +
                "games=" + games +
                ", usernames=" + usernames +
                '}';
    }
}
