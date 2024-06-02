package it.polimi.ingsw.controller4;

import it.polimi.ingsw.Configs;
import it.polimi.ingsw.OSRelated;
import it.polimi.ingsw.controller4.Interfaces.GameControllerReceiver;
import it.polimi.ingsw.controller4.Interfaces.ReceptionControllerInterface;
import it.polimi.ingsw.lightModel.DiffGenerator;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.FatManLobbyList;
import it.polimi.ingsw.model.cardReleted.cards.CardTable;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.ViewState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the controller that handles the reception of the clients. It manages the lobbies, the nicknames and the offline games
 */
public class LobbyGameListController implements ReceptionControllerInterface {
    private final CardTable cardTable = new CardTable(Configs.CardResourcesFolderPath, Configs.CardJSONFileName, OSRelated.cardFolderDataPath);
    private final Map<String, ViewInterface> viewMap = new HashMap<>();
    private final Map<String, LobbyController> lobbyMap = new HashMap<>();
    private final Map<String, GameController> gameMap = new HashMap<>();


    @Override
    public synchronized void login(String nickname, ViewInterface view) {
        //check if the nickname is already taken
        if(allConnectedUsers().containsKey(nickname)) {
            try {
                view.logErr(LogsOnClientStatic.NAME_TAKEN);
                view.transitionTo(ViewState.LOGIN_FORM);
            }catch (Exception ignored){}
            //check if the nickname is valid
        }else if(nickname.matches(Configs.invalidNicknameRegex)){
            try {
                view.logErr(LogsOnClientStatic.NOT_VALID_NICKNAME);
                view.transitionTo(ViewState.LOGIN_FORM);
            }catch (Exception ignored){}
        }else{
            //Client is now logged-In. If he disconnects we have to update the model
            System.out.println(nickname + " has connected");

            //check if the player was playing a game before disconnecting
            if(isInGameParty(nickname)){
                GameController gameToJoin = this.getGameFromUserNick(nickname);
                gameToJoin.join(nickname, view);
            }else{
                joinLobbyList(nickname, view);
                try{view.transitionTo(ViewState.JOIN_LOBBY);}catch (Exception ignored){}
            }
        }
    }

    @Override
    public synchronized void createLobby(String creator, String lobbyName, int maxPlayerCount, GameControllerReceiver gameReceiver) {
        //check if the lobby name is already taken
        ViewInterface view = viewMap.get(creator);
        if(lobbyMap.get(lobbyName)!=null || gameMap.get(lobbyName)!=null) {
            try {
                view.logErr(LogsOnClientStatic.LOBBY_NAME_TAKEN);
                view.transitionTo(ViewState.JOIN_LOBBY);
            }catch (Exception ignored){}
            //check if the lobby name is valid
        }else if(lobbyName.matches(Configs.invalidLobbyNameRegex)) {
            try {
                view.logErr(LogsOnClientStatic.NOT_VALID_LOBBY_NAME);
                view.transitionTo(ViewState.JOIN_LOBBY);
            }catch (Exception ignored){}
        }else if(maxPlayerCount < 2 || maxPlayerCount > 4){
            try {
                view.logErr(LogsOnClientStatic.INVALID_MAX_PLAYER_COUNT);
                view.transitionTo(ViewState.JOIN_LOBBY);
            }catch (Exception ignored){}
        }else { //create the lobby
            System.out.println(creator + " create" + lobbyName + " lobby");
            Lobby lobbyCreated = new Lobby(maxPlayerCount, lobbyName);

            //add the lobby to the model
            lobbyMap.put(lobbyName, new LobbyController(lobbyCreated));
            lobbyMap.get(lobbyName).addPlayer(creator, view, gameReceiver);

            this.notifyNewLobby(creator, lobbyCreated); //notify the lobbyList mediator of the new lobby creation

            try{view.transitionTo(ViewState.LOBBY);}catch (Exception ignored){}
            leaveLobbyList(creator);
        }
    }

    private synchronized void notifyNewLobby(String creator, Lobby addedLobby){
        viewMap.forEach((nickname, view) -> {
            try {
                view.updateLobbyList(DiffGenerator.addLobbyDiff(addedLobby));
                if(!nickname.equals(creator)) {
                    view.log(LogsOnClientStatic.LOBBY_CREATED_OTHERS);
                }else{
                    view.log(LogsOnClientStatic.LOBBY_CREATED_YOU);
                    view.log(LogsOnClientStatic.LOBBY_JOIN_YOU);
                }
            } catch (Exception ignored) {}
        });
    }

    @Override
    public synchronized void joinLobby(String joiner, String lobbyName, GameControllerReceiver gameReceiver) {
        LobbyController lobbyToJoin = lobbyMap.get(lobbyName);
        ViewInterface view = viewMap.get(joiner);
        if (lobbyToJoin == null) {
            try {
                view.logErr(LogsOnClientStatic.LOBBY_NONEXISTENT);
                view.transitionTo(ViewState.JOIN_LOBBY);
            }catch (Exception ignored){}
        } else {
            System.out.println(joiner + " joined " + lobbyName + " lobby");
            leaveLobbyList(joiner);
            //add the player to the lobby, updated model
            lobbyToJoin.addPlayer(joiner, view, gameReceiver);

            if (!lobbyToJoin.isLobbyFull()) {
                try{view.transitionTo(ViewState.LOBBY);}catch (Exception ignored){}
            }else{
                System.out.println(lobbyName + " lobby is full, game started");
                GameController gameController = lobbyToJoin.startGame(cardTable);
                lobbyMap.remove(lobbyName);
                gameMap.put(lobbyName, gameController);
                this.notifyLobbyRemoved(joiner, lobbyToJoin.getLobby());
            }
        }
    }

    private synchronized void notifyLobbyRemoved(String destroyer, Lobby removedLobby){
        viewMap.forEach((nickname, view) -> {
            try {
                view.updateLobbyList(DiffGenerator.removeLobbyDiff(removedLobby));
                if(nickname.equals(destroyer))
                    view.log(LogsOnClientStatic.LOBBY_REMOVED_YOU);
            } catch (Exception ignored) {}
        });
    }

    @Override
    public synchronized void disconnect(String nickname) {
        /*//If the game is empty, remove it from the MultiGame
        if (gameToLeave.getGameLoopController().getActivePlayers().isEmpty()) {
            multiGame.removeGame(gameToLeave);
        }*/
    }

    @Override
    public synchronized void leaveLobby(String leaverNick) {
        LobbyController lobbyToLeave = this.getLobbyFromUserNick(leaverNick);

        if(lobbyToLeave!=null) {
            System.out.println(leaverNick + " left lobby");
            ViewInterface leaverView = lobbyToLeave.removePlayer(leaverNick);
            //update the model
            if(lobbyToLeave.isLobbyEmpty()) {
                lobbyMap.remove(lobbyToLeave.getLobby().getLobbyName());
                this.notifyLobbyRemoved(leaverNick, lobbyToLeave.getLobby());
            }
            this.joinLobbyList(leaverNick, leaverView);

            try{leaverView.transitionTo(ViewState.JOIN_LOBBY);}catch (Exception ignored){}
        }
    }

    private synchronized Boolean isInGameParty(String nickname){
        return getGameFromUserNick(nickname) != null;
    }

    private synchronized GameController getGameFromUserNick(String nickName) {
        return gameMap.values().stream()
                .filter(game -> game.getGamePlayers().contains(nickName))
                .findFirst()
                .orElse(null);
    }

    private synchronized LobbyController getLobbyFromUserNick(String nickName) {
        return lobbyMap.values().stream()
                .filter(lobby -> lobby.getLobby().getLobbyPlayerList().contains(nickName))
                .findFirst()
                .orElse(null);
    }

    private synchronized Map<String, ViewInterface> allConnectedUsers(){
        Map<String, ViewInterface> allConnectedUsers = new HashMap<>(viewMap);
        gameMap.forEach((s, gameController) -> allConnectedUsers.putAll(gameController.getPlayerViewMap()));
        lobbyMap.forEach((s, gameController) -> allConnectedUsers.putAll(gameController.getViewMap()));
        return allConnectedUsers;
    }

    private synchronized void joinLobbyList(String nickname, ViewInterface view){
        viewMap.put(nickname, view);
        updateJoinLobbyList(view);
    }

    private synchronized void updateJoinLobbyList(ViewInterface joinerView){
        List<Lobby> lobbyHistory = lobbyMap.values().stream().map(LobbyController::getLobby).toList();
        try {
            joinerView.updateLobbyList(DiffGenerator.lobbyListHistory(lobbyHistory));
            joinerView.log(LogsOnClientStatic.JOIN_LOBBY_LIST);
        }catch (Exception ignored){}
    }

    private synchronized ViewInterface leaveLobbyList(String nickname){
        ViewInterface view = viewMap.get(nickname);
        viewMap.remove(nickname);
        updateLeaveLobbyList(view);
        return view;
    }

    private synchronized void updateLeaveLobbyList(ViewInterface leaverView){
        try {
            leaverView.updateLobbyList(new FatManLobbyList());
            leaverView.log(LogsOnClientStatic.LEFT_LOBBY_LIST);
        }catch (Exception ignored){}
    }
}
