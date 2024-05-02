package it.polimi.ingsw.model;

import it.polimi.ingsw.SignificantPaths;
import it.polimi.ingsw.controller2.ServerModelController;
import it.polimi.ingsw.lightModel.diffObserverInterface.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffPublishers.LobbyListDiffPublisher;
import it.polimi.ingsw.lightModel.diffs.LobbyListDiffEdit;
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
    private final Map<ServerModelController, String> username;
    private final CardLookUp<ObjectiveCard> cardLookUpObjective;
    private final CardLookUp<StartCard> cardLookUpStartCard;
    private final CardLookUp<ResourceCard> cardLookUpResourceCard;
    private final CardLookUp<GoldCard> cardLookUpGoldCard;
    public MultiGame() {
        this.games = new HashSet<>();
        //usernames
        this.username = new HashMap<>(); //user that are connected to the server
        lobbies = new LobbyList();
        String filePath = SignificantPaths.CardFolder;
        String sourceFileName = SignificantPaths.CardFile;
        cardLookUpObjective =
                new CardLookUp<>(new ObjectiveCardFactory(filePath+sourceFileName, filePath).getCards());
        cardLookUpStartCard =
                new CardLookUp<>(new StartCardFactory(filePath+sourceFileName, filePath).getCards());
        cardLookUpResourceCard =
                new CardLookUp<>(new ResourceCardFactory(filePath+sourceFileName, filePath).getCards());
        cardLookUpGoldCard =
                new CardLookUp<>(new GoldCardFactory(filePath+sourceFileName, filePath).getCards());
    }

    public synchronized Set<Game> getGames() {
        return games;
    }

    public Set<String> getUsernames() {
        synchronized (username) {
            return new HashSet<>(username.values());
        }
    }

    public synchronized boolean addGame(Game game) {
        return games.add(game);
    }

    public boolean addUser(ServerModelController controller, String username) {
        synchronized (this.username){
            if(isUnique(username)){
                this.username.put(controller, username);
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
        synchronized (this.username){
            this.username.remove(username);
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
    public void subscribe(LobbyListDiffEdit lightLobbyDiff){
        lobbies.subscribe(lightLobbyDiff);
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

    public Lobby getUserLobby(String nickname){
        for(Lobby lobby: lobbies.getLobbies()){
            for(String name : lobby.getLobbyPlayerList())
                if(name.equals(nickname))
                    return lobby;
        }
        return null;
    }

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

    public User getUserFromNick(String nickname){
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

    public Map<ServerModelController, String> getUsernameMap(){
        return username;
    }



    @Override
    public String toString() {
        return "MultiGame{" +
                "games=" + games +
                ", usernames=" + username +
                '}';
    }
}
