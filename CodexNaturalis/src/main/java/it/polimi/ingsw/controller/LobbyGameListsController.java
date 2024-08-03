package it.polimi.ingsw.controller;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.utils.OSRelated;
import it.polimi.ingsw.controller.Interfaces.GameControllerReceiver;
import it.polimi.ingsw.controller.persistence.PersistenceFactory;
import it.polimi.ingsw.lightModel.diffs.DiffGenerator;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.FatManLobbyList;
import it.polimi.ingsw.model.cardReleted.cards.CardTable;
import it.polimi.ingsw.model.tableReleted.Game;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.utils.logger.LoggerLevel;
import it.polimi.ingsw.utils.logger.LoggerSources;
import it.polimi.ingsw.utils.logger.ServerLogger;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.util.*;
import java.util.concurrent.*;

/**
 * This class is the controller that handles the reception of the clients.
 * It manages list of lobbies, the list of games,
 * the join and leave of the players, the creation and destruction of the lobbies and the games.
 */
public class LobbyGameListsController implements it.polimi.ingsw.controller.Interfaces.LobbyGameListsController {
    private transient final CardTable cardTable = new CardTable(Configs.CardResourcesFolderPath, Configs.CardJSONFileName, OSRelated.cardFolderDataPath);
    private final Map<String, ViewInterface> viewMap = new HashMap<>();
    private final Map<String, LobbyController> lobbyMap = new HashMap<>();
    private final Map<String, GameController> gameMap = new HashMap<>();
    private transient final PersistenceFactory persistenceFactory = new PersistenceFactory(OSRelated.gameDataFolderPath);
    private transient final ScheduledExecutorService gamesLoadExecutor = Executors.newScheduledThreadPool(1);
    private transient final ServerLogger logger = new ServerLogger(LoggerSources.LOBBY_GAME_LISTS_CONTROLLER, "");

    /**
     * Constructor of the class.
     * It schedules a periodic task to refresh the list of games,
     * removing the expired games from the list.
     */
    public LobbyGameListsController(){
        gamesLoadExecutor.scheduleAtFixedRate(this::refreshGames, Configs.delayBeforeLoadingGameSaves, Configs.gameSaveExpirationTimeMinutes, TimeUnit.MINUTES);
    }

    /**
     * Method that handles the login of a player.
     * It checks if the nickname is valid, depending on the regex in the Configs class.
     * If it is not valid, it sends an error message to the client and re-prompts the login form.
     * If the nickname is already taken, it sends an error message to the client and re-prompts the login form.
     * If the nickname is in a gameParty, it sets the gameController of the player to the gameController and re-joins the game.
     * Otherwise, it adds the player to the lobbyList updates the lightModel
     * with the list of all opened lobbies and prompts the joinLobby view
     * @param nickname the nickname chosen by the player
     * @param view the view of the player
     * @param controllerReceiver the controller receiver of the player used to
     *                           set the gameController if the player is in a game
     * @return true if the player is successfully logged in, false otherwise
     */
    @Override
    public synchronized boolean login(String nickname, ViewInterface view, GameControllerReceiver controllerReceiver) {
        boolean loggedIn;
        if(view != null && controllerReceiver != null ) {
            //check if the nickname is already taken
            if (allConnectedUsers().containsKey(nickname)) {
                loggedIn = false;
                try {
                    view.logErr(LogsOnClient.NAME_TAKEN);
                    view.transitionTo(ViewState.LOGIN_FORM);
                } catch (Exception ignored) {
                }
                //check if the nickname is valid
            } else if (nickname.matches(Configs.invalidNicknameRegex)) {
                loggedIn = false;
                try {
                    view.logErr(LogsOnClient.NOT_VALID_NICKNAME);
                    view.transitionTo(ViewState.LOGIN_FORM);
                } catch (Exception ignored) {
                }
            } else {
                loggedIn = true;
                //Client is now logged-In. If he disconnects we have to update the model
                logger.log(LoggerLevel.INFO, nickname + " has connected");
                //check if the player was playing a game before disconnecting
                if (isInGameParty(nickname)) {
                    GameController gameToJoin = this.getGameFromUserNick(nickname);
                    controllerReceiver.setGameController(gameToJoin);
                    gameToJoin.join(nickname, view, true);
                } else {
                    joinLobbyList(nickname, view);
                    try {
                        view.transitionTo(ViewState.JOIN_LOBBY);
                    } catch (Exception ignored) {
                    }
                }
            }
        }else {
            loggedIn = false;
            this.manageMalevolentPlayer(nickname);
        }
        return loggedIn;
    }

    /**
     * Method that handles the creation of a lobby.
     * It checks the parameters are not null and if the player is logged in,
     * if that is not the case, the player is considered malevolent.
     * If the lobby name is not valid, it sends an error message to the client and re-prompts the joinLobby view.
     * If the lobby name is already taken, it sends an error message to the client and re-prompts the joinLobby view.
     * If the maximum number of players is not valid, it sends an error message to the client and re-prompts the joinLobby view.
     * Otherwise, it creates the lobby, adds the player to the lobby, updates the lightModel with the new lobby
     * and prompts the lobby view.
     * After joining the lobby, the player lightLobbyList is erased.
     * @param creator the nickname of the player creating the lobby
     * @param lobbyName the name of the lobby
     * @param maxPlayerCount the maximum number of players that can join the lobby
     * @param gameReceiver the game controller receiver of the player
     */
    @Override
    public synchronized void createLobby(String creator, String lobbyName, int maxPlayerCount, int numberOfAgents, GameControllerReceiver gameReceiver) {
        //check if the lobby name is already taken
        ViewInterface view = viewMap.get(creator);

        if(creator != null && gameReceiver != null && view != null && !isInGameParty(creator)){
            if (lobbyMap.get(lobbyName) != null || gameMap.get(lobbyName) != null) {
                try {
                    view.logErr(LogsOnClient.LOBBY_NAME_TAKEN);
                    view.transitionTo(ViewState.JOIN_LOBBY);
                } catch (Exception ignored) {
                }
                //check if the lobby name is valid
            } else if (lobbyName.matches(Configs.invalidLobbyNameRegex)) {
                try {
                    view.logErr(LogsOnClient.NOT_VALID_LOBBY_NAME);
                    view.transitionTo(ViewState.JOIN_LOBBY);
                } catch (Exception ignored) {
                }
            } else if (maxPlayerCount < 2 || maxPlayerCount > 4) {
                try {
                    view.logErr(LogsOnClient.INVALID_MAX_PLAYER_COUNT);
                    view.transitionTo(ViewState.JOIN_LOBBY);
                } catch (Exception ignored) {
                }
            } else { //create the lobby
                logger.log(LoggerLevel.INFO, creator + " created " + lobbyName + " lobby");
                Lobby lobbyCreated = new Lobby(maxPlayerCount, lobbyName, numberOfAgents);

                leaveLobbyList(creator);
                //add the lobby to the model
                lobbyMap.put(lobbyName, new LobbyController(lobbyCreated));
                this.notifyNewLobby(creator, view, lobbyCreated); //notify the lobbyList mediator of the new lobby creation

                lobbyMap.get(lobbyName).addPlayer(creator, view, gameReceiver);

                try {
                    view.transitionTo(ViewState.LOBBY);
                } catch (Exception ignored) {
                }
            }
        }else {
            this.manageMalevolentPlayer(creator);
        }
    }

    /**
     * Method that handles the joining of a lobby.
     * It checks the parameters are not null and if the player is logged in,
     * if that is not the case, the player is considered malevolent.
     * If the lobby name is not valid, it sends an error message to the client and re-prompts the joinLobby view.
     * If the lobby does not exist, it sends an error message to the client and re-prompts the joinLobby view.
     * It adds the player to the lobby, updates the lightModel of the joiner with the new lobby
     * and prompts the lobby view if the lobby is not full, otherwise it starts the game.
     * After joining the lobby, the player lightLobbyList is erased.
     * @param joiner the nickname of the player joining the lobby
     * @param lobbyName the name of the lobby to join
     * @param gameReceiver the game controller receiver of the player
     */
    @Override
    public synchronized void joinLobby(String joiner, String lobbyName, GameControllerReceiver gameReceiver) {
        LobbyController lobbyToJoin = lobbyMap.get(lobbyName);
        ViewInterface view = viewMap.get(joiner);

        if(view != null && gameReceiver != null && !isInGameParty(joiner)) {
            if (lobbyToJoin == null) {
                try {
                    view.logErr(LogsOnClient.LOBBY_NONEXISTENT);
                    view.transitionTo(ViewState.JOIN_LOBBY);
                } catch (Exception ignored) {
                }
            } else {
                logger.log(LoggerLevel.INFO, joiner + " joined " + lobbyName + " lobby");
                leaveLobbyList(joiner);
                //add the player to the lobby, updated model
                lobbyToJoin.addPlayer(joiner, view, gameReceiver);

                if (!lobbyToJoin.isLobbyFull()) {
                    try {
                        view.transitionTo(ViewState.LOBBY);
                    } catch (Exception ignored) {
                    }
                } else {
                    logger.log(LoggerLevel.INFO, lobbyName + " lobby is full, game started");
                    GameController gameController = lobbyToJoin.startGame(cardTable, persistenceFactory, this, this);
                    lobbyMap.remove(lobbyName);
                    gameMap.put(lobbyName, gameController);
                    this.notifyLobbyRemoved(joiner, lobbyToJoin.getLobby());
                }
            }
        }else {
            this.manageMalevolentPlayer(joiner);
        }
    }

    /**
     * Method that notifies the creation of a new lobby to all the players in the lobbyList.
     * It updates the lightModel of all the players in the lobbyList with the new lobby.
     * @param creator the nickname of the player that created the lobby
     * @param creatorView the view of the player that created the lobby
     * @param addedLobby the lobby that was created
     */
    private synchronized void notifyNewLobby(String creator, ViewInterface creatorView, Lobby addedLobby){
        try{creatorView.log(LogsOnClient.LOBBY_CREATED_YOU);}catch (Exception ignored){}
        viewMap.forEach((nickname, view) -> {
            try {
                view.updateLobbyList(DiffGenerator.addLobbyDiff(addedLobby));
                if(!nickname.equals(creator)) {
                    view.log(LogsOnClient.LOBBY_CREATED_OTHERS);
                }
            } catch (Exception ignored) {}
        });
    }

    /**
     * Method that notifies the removal of a lobby to all the players in the lobbyList.
     * It updates the lightModel of all the players in the lobbyList with the removed lobby.
     * @param destroyer the nickname of the player that removed the lobby
     * @param removedLobby the lobby that was removed
     */
    private synchronized void notifyLobbyRemoved(String destroyer, Lobby removedLobby){
        viewMap.forEach((nickname, view) -> {
            try {
                view.updateLobbyList(DiffGenerator.removeLobbyDiff(removedLobby));
                if(nickname.equals(destroyer))
                    view.log(LogsOnClient.LOBBY_REMOVED_YOU);
            } catch (Exception ignored) {}
        });
    }

    /**
     * Method that handles the leaving of a player.
     * It checks if the player is in a gameParty, in a lobby or in the lobbyList.
     * Depending on the case, it calls the leave method of the gameController,
     * the leave method of the lobby, and finally it removes the player from the lobbyList.
     * @param nickname the nickname of the player leaving
     */
    @Override
    public synchronized void leave(String nickname) {
        if(allConnectedUsers().containsKey(nickname)) {
            if (this.isInGameParty(nickname)) {
                GameController gameToLeaveController = this.getGameFromUserNick(nickname);
                gameToLeaveController.leave(nickname);
            } else if (this.isActiveInLobby(nickname)) {
                this.leaveLobby(nickname);
                this.leaveLobbyList(nickname);
            } else {
                this.leaveLobbyList(nickname);
            }
            logger.log(LoggerLevel.INFO, nickname + " disconnected");
        }
    }

    /**
     * Method that handles the leaving of a player from a lobby.
     * It checks if the player is in a lobby, if that is not the case, the player is considered malevolent.
     * It removes the player from the lobby calling the removePlayer method of the lobbyController.
     * It logs the leaving of the player and updates the lightModel of the player with the joinLobby view.
     * If the lobby is empty, it removes the lobby from the lobbyMap and notifies the lobbyList mediator of the removal.
     * @param leaverNick the nickname of the player leaving the lobby
     */
    @Override
    public synchronized void leaveLobby(String leaverNick) {
        LobbyController lobbyToLeave = this.getLobbyFromUserNick(leaverNick);

        if(lobbyToLeave!=null) {
            logger.log(LoggerLevel.INFO, leaverNick + " left lobby");
            ViewInterface leaverView = lobbyToLeave.removePlayer(leaverNick);
            //update the model
            if(lobbyToLeave.isLobbyEmpty()) {
                lobbyMap.remove(lobbyToLeave.getLobby().getLobbyName());
                this.notifyLobbyRemoved(leaverNick, lobbyToLeave.getLobby());
                logger.log(LoggerLevel.INFO, "Lobby " + lobbyToLeave.getLobby().getLobbyName() + " removed");
            }
            this.joinLobbyList(leaverNick, leaverView);

            try{leaverView.transitionTo(ViewState.JOIN_LOBBY);}catch (Exception ignored){}
        }else {
            this.manageMalevolentPlayer(leaverNick);
        }
    }

    /**
     * Method that checks if the player passed as parameter is in a lobby.
     * @param nickname the nickname of the player to check
     * @return true if the player is in a lobby, false otherwise
     */
    private synchronized Boolean isActiveInLobby(String nickname){
        return getLobbyFromUserNick(nickname) != null;
    }

    /**
     * Method that checks if the player passed as parameter is in a gameParty.
     * i.e. checks if the player have joined a game before re-joining the lobbyList.
     * @param nickname the nickname of the player to check
     * @return true if the player is in a gameParty, false otherwise
     */
    private synchronized Boolean isInGameParty(String nickname){
        return getGameFromUserNick(nickname) != null;
    }

    /**
     * Method that returns the gameController of the game in which the player passed as parameter is.
     * i.e. the gameController of the game in which the player is actively playing.
     * @param nickName the nickname of the player to search
     * @return the gameController of the game in which the player is, null if the player is not in a game
     */
    private synchronized GameController getGameFromUserNick(String nickName) {
        return gameMap.values().stream()
                .filter(game -> game.getGamePlayers().contains(nickName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Method that returns the lobbyController of the lobby in which the player passed as parameter is.
     * @param nickName the nickname of the player to search
     * @return the lobbyController of the lobby in which the player is, null if the player is not in a lobby
     */
    private synchronized LobbyController getLobbyFromUserNick(String nickName) {
        return lobbyMap.values().stream()
                .filter(lobby -> lobby.getLobby().getLobbyPlayerList().contains(nickName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Method that returns the list of all the connected users.
     * Either they are in a gameParty, in a lobby or in the lobbyList.
     * @return a Map containing the nicknames and the relative views of all the connected users
     */
    private synchronized Map<String, ViewInterface> allConnectedUsers(){
        Map<String, ViewInterface> allConnectedUsers = new HashMap<>(viewMap);
        gameMap.forEach((s, gameController) -> allConnectedUsers.putAll(gameController.getPlayerViewMap()));
        lobbyMap.forEach((s, gameController) -> allConnectedUsers.putAll(gameController.getViewMap()));
        return allConnectedUsers;
    }

    /**
     * Method that adds a player to the lobbyList.
     * It updates the lightModel of the player joining the lobbyList with the list of all opened lobbies.
     * It logs the joining of the player on its view.
     * @param nickname the nickname of the player joining the lobbyList
     * @param view the view of the player joining the lobbyList
     */
    private synchronized void joinLobbyList(String nickname, ViewInterface view){
        updateJoinLobbyList(view);
        viewMap.put(nickname, view);
    }

    /**
     * Method that updates the lightModel of the player joining the lobbyList.
     * It updates the lightModel with the list of all opened lobbies.
     * and logs the joining of the player on its view.
     * @param joinerView the view of the player joining the lobbyList
     */
    private synchronized void updateJoinLobbyList(ViewInterface joinerView){
        List<Lobby> lobbyHistory = lobbyMap.values().stream().map(LobbyController::getLobby).toList();
        try {
            joinerView.updateLobbyList(DiffGenerator.lobbyListHistory(lobbyHistory));
            joinerView.log(LogsOnClient.JOIN_LOBBY_LIST);
        }catch (Exception ignored){}
    }

    /**
     * Method that removes a player from the lobbyList.
     * It removes the player from the viewMap.
     * It erases the lightLobbyList of the player.
     * @param nickname the nickname of the player leaving the lobbyList
     */
    private synchronized void leaveLobbyList(String nickname){
        ViewInterface view = viewMap.get(nickname);
        viewMap.remove(nickname);
        updateLeaveLobbyList(view);
    }

    /**
     * Method that updates the lightModel of the player leaving the lobbyList.
     * It erases the lightLobbyList of the player.
     * @param leaverView the view of the player leaving the lobbyList
     */
    private synchronized void updateLeaveLobbyList(ViewInterface leaverView){
        try {
            leaverView.updateLobbyList(new FatManLobbyList());
            leaverView.log(LogsOnClient.LEFT_LOBBY_LIST);
        }catch (Exception ignored){}
    }

    /**
     * Method that refreshes the list of games.
     * It loads the games from the persistenceFactory and updates the gameMap.
     * If the gameMap does not contain a game that is in the loadedGames, it creates a new gameController
     * and adds it to the gameMap.
     * If the loadedGames do not contain a game that is in the gameMap, it removes the game from the gameMap,
     * meaning that the game has expired.
     */
    private synchronized void refreshGames(){
        Future<HashSet<Game>> loadedGamesFuture = persistenceFactory.load();
        HashSet<Game> loadedGames = new HashSet<>();
        try {
            loadedGames = loadedGamesFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.log(LoggerLevel.WARNING, "un-able to load games");
        }

        for(Game game : loadedGames){
            if(gameMap.get(game.getName()) == null){
                GameController gameController = new GameController(game, cardTable, persistenceFactory, this, this);
                gameMap.put(game.getName(), gameController);
            }
        }

        for(String game : gameMap.keySet()){
            if(loadedGames.stream().noneMatch(g -> g.getName().equals(game))){
                gameMap.remove(game);
            }
        }
    }

    /**
     * Method that delete a game from the gameMap.
     * @param gameName is the name of the game to be removed
     */
    @Override
    public synchronized void deleteGame(String gameName){
        gameMap.remove(gameName);
    }

    /**
     * Method that handles the malevolent player,
     * i.e. the players that try to perform actions that are not allowed.
     * It logs the malevolent player on the server and sends an error message to the client.
     * @param player is the nickname of the malevolent player
     */
    public synchronized void manageMalevolentPlayer(String player) {
        try {
            viewMap.get(player).logErr(LogsOnClient.MALEVOLENT);
            logger.log(LoggerLevel.WARNING, player + " is malevolent");
        }catch (Exception ignored){}
    }
}
