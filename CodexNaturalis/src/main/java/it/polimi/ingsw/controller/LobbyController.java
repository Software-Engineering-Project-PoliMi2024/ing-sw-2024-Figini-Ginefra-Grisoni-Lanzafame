package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.Interfaces.GameControllerReceiver;
import it.polimi.ingsw.controller.Interfaces.GameList;
import it.polimi.ingsw.controller.Interfaces.LobbyControllerInterface;
import it.polimi.ingsw.controller.Interfaces.MalevolentPlayerManager;
import it.polimi.ingsw.controller.persistence.PersistenceFactory;
import it.polimi.ingsw.lightModel.diffs.DiffGenerator;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.LittleBoyLobby;
import it.polimi.ingsw.model.cardReleted.cards.*;
import it.polimi.ingsw.model.tableReleted.Deck;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is the controller of the lobby,
 * it manages the players that are in the lobby (join and leave)
 * and is responsible for starting the game.
 */
public class LobbyController implements LobbyControllerInterface {
    private final Lobby lobby;
    private final Map<String, ViewInterface> viewMap = new HashMap<>();
    private final Map<String, GameControllerReceiver> gameReceiverMap = new HashMap<>();

    /**
     * Constructor of the class
     * @param lobby the lobby that the controller will manage
     */
    public LobbyController(Lobby lobby){
        this.lobby = lobby;
    }

    /**
     * This method returns a copy of the viewMap of the controller
     * containing the players nicknames in the lobby and their views
     */
    public synchronized Map<String, ViewInterface> getViewMap(){
        return new HashMap<>(viewMap);
    }

    /**
     * @return a copy of the lobby managed by the controller
     */
    public synchronized Lobby getLobby(){
        return new Lobby(lobby);
    }

    /**
     * This method adds a player to the lobby,
     * adding his nickname to the lobby and his view to the viewMap of the controller
     * and adding the game controller receiver to the gameReceiverMap of the controller
     * Notifies all the players in the lobby that a new player has joined
     * Notifies the new player that he has joined the lobby
     * @param nickname joiner nickname
     * @param view joiner view
     * @param gameControllerReceiver joiner game controller receiver
     */
    public synchronized void addPlayer(String nickname, ViewInterface view, GameControllerReceiver gameControllerReceiver){
        lobby.addUserName(nickname);
        viewMap.put(nickname, view);
        gameReceiverMap.put(nickname, gameControllerReceiver);
        this.notifyJoin(nickname);
    }

    /**
     * This method removes a player from the lobby,
     * removing his nickname from the lobby and his view from the viewMap of the controller
     * and removing the game controller receiver from the gameReceiverMap of the controller
     * Notifies the players left in the lobby that a player has left
     * @param nickname leaver nickname
     * @return the view of the player that has left
     */
    public synchronized ViewInterface removePlayer(String nickname){
        ViewInterface view = viewMap.get(nickname);
        this.notifyLeft(nickname);
        lobby.removeUserName(nickname);
        viewMap.remove(nickname);
        gameReceiverMap.remove(nickname);
        return view;
    }

    /**
     * This method creates a game with the players in the lobby, the card table,
     * the persistence factory, the game list and the malevolent player manager
     * passed as parameters
     * It creates a game controller to manage the game created
     * It makes all the players in the lobby join the game
     * @param cardTable the card table used to map cards to their ids
     * @param persistenceFactory the persistence factory used to save the game
     * @param gameList the game list used to remove the game when it ends
     * @param malevolentPlayerManager the malevolent player manager used to manage malevolent players
     * @return the game controller created
     */
    public synchronized GameController startGame(CardTable cardTable, PersistenceFactory persistenceFactory, GameList gameList, MalevolentPlayerManager malevolentPlayerManager){
        Game createdGame = getGame(cardTable);
        GameController gameController = new GameController(createdGame, cardTable, persistenceFactory, gameList, malevolentPlayerManager);
        createdGame.getGameParty().setAgentsController(gameController);

        notifyGameStart();
        gameReceiverMap.forEach((nick, receiver)->receiver.setGameController(gameController));
        viewMap.forEach((nick, view) -> gameController.join(nick, view, false));
        createdGame.getGameParty().getAgentNicknames().forEach(agentNick -> gameController.join(agentNick, null, false));
        createdGame.startAgents();
        return gameController;
    }

    /**
     * This method creates a game with the players in the lobby and the card table passed as parameters
     * @param cardTable the card table used to map cards to their ids
     * @return the game created
     */
    private Game getGame(CardTable cardTable) {
        Deck<ObjectiveCard> objectiveCardDeck = new Deck<>(0, cardTable.getCardLookUpObjective().getQueue());
        Deck<ResourceCard> resourceCardDeck = new Deck<>(2, cardTable.getCardLookUpResourceCard().getQueue());
        Deck<GoldCard> goldCardDeck = new Deck<>(2, cardTable.getCardLookUpGoldCard().getQueue());
        Deck<StartCard> startingCardDeck = new Deck<>(0, cardTable.getCardLookUpStartCard().getQueue());
        return new Game(lobby, objectiveCardDeck, resourceCardDeck, goldCardDeck, startingCardDeck);
    }

    /**
     * @return true if the number of players in the lobby is equal to the maximum number of players allowed in the lobby
     *         false otherwise
     */
    public synchronized boolean isLobbyFull(){
        return lobby.getLobbyPlayerList().size() == lobby.getNumberOfMaxPlayer();
    }

    /**
     * @return true if there are no players in the lobby
     */
    public synchronized boolean isLobbyEmpty(){
        return lobby.getLobbyPlayerList().isEmpty();
    }

    /**
     * This method notifies all the players in the lobby that a player has joined
     * updating their lightLobby with the new player and logging the join
     * Updates the lightLobby of the player that has joined with the players in the lobby
     * the max number of players allowed in the lobby and logs the join
     * @param nickname the nickname of the player that has joined
     */
    private synchronized void notifyJoin(String nickname) {
        viewMap.forEach((nick, view) -> {
            try {
                if (!nick.equals(nickname)) {
                    view.updateLobby(DiffGenerator.diffAddUserToLobby(nickname));
                    view.logOthers(nickname + LogsOnClient.LOBBY_JOIN_OTHER);
                } else {
                    view.updateLobby(DiffGenerator.diffJoinLobby(this.lobby));
                    view.log((LogsOnClient.LOBBY_JOIN_YOU));
                }
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * This method notifies all the players in the lobby that a player has left
     * updating their lightLobby with the player that has left and logging the leave
     * It erases all lightLobby data of the player that has left
     * @param nickname the nickname of the player that has left
     */
    private synchronized void notifyLeft(String nickname){
        viewMap.forEach((nick, view) -> {
            try {
                if(nick.equals(nickname)){
                    view.updateLobby(new LittleBoyLobby());
                    view.log(LogsOnClient.LOBBY_LEFT);
                }else {
                    view.updateLobby(DiffGenerator.diffRemoveUserFromLobby(nickname));
                    view.logOthers(nickname + LogsOnClient.LOBBY_LEFT_OTHER);
                }
            } catch (Exception ignored) {
            }
        });
    }

    /**
     * This method notifies all the players in the lobby that the game has started
     * erasing all lightLobby data of the players in the lobby and logging the game start
     */
    private synchronized void notifyGameStart(){
        viewMap.forEach((nick, view) -> {
            try {
                view.updateLobby(new LittleBoyLobby());
                view.logGame(LogsOnClient.GAME_CREATED);
            } catch (Exception ignored) {
            }
        });

    }
}
