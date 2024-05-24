package it.polimi.ingsw.controller3.mediators;

import it.polimi.ingsw.controller3.LogsOnClientStatic;
import it.polimi.ingsw.lightModel.DiffGenerator;
import it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces.LightLobbyListUpdater;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.LoggerInterface;
import it.polimi.ingsw.view.ViewInterface;

import java.util.List;

public class LobbyListMediator extends Mediator<LightLobbyListUpdater, LightLobbyList> {
    /**
     * Subscribes the updater and the logger to the mediator (both interfaces are implemented by viewInterface)
     * Updates the lobbyList of the subscriber with the lobbyHistory passed as parameter
     * Logs the event
     * @param nickname the subscriber's nickname
     * @param loggerUpdater the logger connected to the subscriber
     * @param lobbyHistory the history of the lobbies
     */
    public synchronized void subscribe(String nickname, ViewInterface loggerUpdater, List<Lobby> lobbyHistory){
        super.subscribe(nickname, loggerUpdater, loggerUpdater);
        try {
            loggerUpdater.updateLobbyList(DiffGenerator.lobbyListHistory(lobbyHistory));
            loggerUpdater.log(LogsOnClientStatic.SERVER_JOINED);
        }catch (Exception e){
            System.out.println("LobbyListMediator: error in subscribing" + e.getMessage());
        }
    }

    /**
     * Unsubscribes the subscriber with the nickname passed as parameter
     * @param nickname the unSubscriber's nickname
     */
    public synchronized void unsubscribe(String nickname){
        super.unsubscribe(nickname);
    }
    /**
     * Notifies all the subscribers that a lobby has been added
     * Adds the lobby to the lightLobbyList connected to the updater
     * @param creator the subscriber that created the lobby
     * @param addedLobby the lobby added to the lobbyList
     */
    public synchronized void notifyNewLobby(String creator, Lobby addedLobby){
        subscribers.forEach((nickname, pair) -> {
            try {
                pair.first().updateLobbyList(DiffGenerator.addLobbyDiff(addedLobby));
                if(!nickname.equals(creator))
                    pair.second().log(LogsOnClientStatic.LOBBY_CREATED_OTHERS);
                else
                    pair.second().log(LogsOnClientStatic.LOBBY_CREATED_YOU);
            } catch (Exception e) {
                System.out.println("LobbyListMediator: error in notifying about the new lobby" + e.getMessage());
            }
        });
    }

    /**
     * Notifies the subscriber that removed the lobby
     * Removes the lobby from the lightLobbyList connected to the updater
     * @param destroyer the subscriber that removed the lobby
     * @param removedLobby the lobby removed from the lobbyList
     */
    public synchronized void notifyLobbyRemoved(String destroyer, Lobby removedLobby){
        subscribers.forEach((nickname, pair) -> {
            try {
                pair.first().updateLobbyList(DiffGenerator.removeLobbyDiff(removedLobby));
                if(nickname.equals(destroyer))
                    pair.second().log(LogsOnClientStatic.LOBBY_REMOVED_YOU);
            } catch (Exception e) {
                System.out.println("LobbyListMediator: error in notifying about the removed lobby" + e.getMessage());
            }
        });
    }

}
