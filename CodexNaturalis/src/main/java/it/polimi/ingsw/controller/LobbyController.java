package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.Interfaces.GameControllerReceiver;
import it.polimi.ingsw.controller.Interfaces.GameList;
import it.polimi.ingsw.controller.Interfaces.LobbyControllerInterface;
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

public class LobbyController implements LobbyControllerInterface {
    private final Lobby lobby;
    private final Map<String, ViewInterface> viewMap = new HashMap<>();
    private final Map<String, GameControllerReceiver> gameReceiverMap = new HashMap<>();

    public LobbyController(Lobby lobby){
        this.lobby = lobby;
    }

    public synchronized Map<String, ViewInterface> getViewMap(){
        return new HashMap<>(viewMap);
    }

    public synchronized Lobby getLobby(){
        return new Lobby(lobby);
    }

    public synchronized void addPlayer(String nickname, ViewInterface view, GameControllerReceiver gameControllerReceiver){
        lobby.addUserName(nickname);
        viewMap.put(nickname, view);
        gameReceiverMap.put(nickname, gameControllerReceiver);
        this.notifyJoin(nickname);
    }

    public synchronized ViewInterface removePlayer(String nickname){
        ViewInterface view = viewMap.get(nickname);
        this.notifyLeft(nickname);
        lobby.removeUserName(nickname);
        viewMap.remove(nickname);
        gameReceiverMap.remove(nickname);
        return view;
    }

    public synchronized GameController startGame(CardTable cardTable, PersistenceFactory persistenceFactory, GameList gameList){
        Deck<ObjectiveCard> objectiveCardDeck = new Deck<>(0,cardTable.getCardLookUpObjective().getQueue());
        Deck<ResourceCard> resourceCardDeck = new Deck<>(2, cardTable.getCardLookUpResourceCard().getQueue());
        Deck<GoldCard> goldCardDeck = new Deck<>(2, cardTable.getCardLookUpGoldCard().getQueue());
        Deck<StartCard> startingCardDeck = new Deck<>(0, cardTable.getCardLookUpStartCard().getQueue());
        Game createdGame = new Game(lobby, objectiveCardDeck, resourceCardDeck, goldCardDeck, startingCardDeck);
        GameController gameController = new GameController(createdGame, cardTable, persistenceFactory, gameList);

        notifyGameStart();
        gameReceiverMap.forEach((nick, receiver)->receiver.setGameController(gameController));
        viewMap.forEach((nick, view) -> gameController.join(nick, view, false));

        return gameController;
    }

    public synchronized boolean isLobbyFull(){
        return lobby.getLobbyPlayerList().size() == lobby.getNumberOfMaxPlayer();
    }

    public synchronized boolean isLobbyEmpty(){
        return lobby.getLobbyPlayerList().isEmpty();
    }

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
