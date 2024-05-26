package it.polimi.ingsw.controller3.mediators.loggerAndUpdaterMediators;

import it.polimi.ingsw.controller3.LogsOnClientStatic;
import it.polimi.ingsw.lightModel.DiffGenerator;
import it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces.LightLobbyUpdater;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.LittleBoyLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.tableReleted.Lobby;
import it.polimi.ingsw.view.ViewInterface;

public class LobbyMediator extends LoggerAndUpdaterMediator<LightLobbyUpdater, LightLobby> {
    /**
     * Subscribes the updater and the logger to the mediator (both interfaces are implemented by viewInterface)
     * Updates the lobby of the subscriber with the lobby passed as parameter
     * Logs the event
     * Notifies all the other subscribers that a new user has joined the lobby
     * adds the subscriber to the mediator
     * @param nickname the subscriber's nickname
     * @param loggerAndUpdater the logger and updater connected to the subscriber
     * @param lobbyJoined the lobby joined by the subscriber
     */
    public synchronized void subscribe(String nickname, ViewInterface loggerAndUpdater, Lobby lobbyJoined){
        try {
            loggerAndUpdater.updateLobby(DiffGenerator.diffJoinLobby(lobbyJoined));
            loggerAndUpdater.log((LogsOnClientStatic.LOBBY_JOIN_YOU));
        }catch (Exception e){
            System.out.println("LobbyMediator: error in subscribing" + e.getMessage());
        }
        subscribers.forEach((nick, pair)->{
            if(nick != nickname) {
                try {
                    pair.first().updateLobby(DiffGenerator.diffAddUserToLobby(nickname));
                    pair.second().log(nickname + LogsOnClientStatic.LOBBY_JOIN_OTHER);
                }catch (Exception e){
                    System.out.println("LobbyMediator: error in notifying other user" + e.getMessage());
                }
            }
        });
        super.subscribe(nickname, loggerAndUpdater, loggerAndUpdater);
    }

    /**
     * Unsubscribes the subscriber with the nickname passed as parameter
     * erases the lobby of the unSubscriber
     * notifies all the other subscribers that the unSubscriber has left the lobby
     * logs the event on the unSubscriber
     * removes the unSubscriber from the mediator
     * @param nickname the unSubscriber's nickname
     */
    public synchronized void unsubscribe(String nickname){
        try {
            subscribers.get(nickname).first().updateLobby(new LittleBoyLobby());
            subscribers.get(nickname).second().log(LogsOnClientStatic.LOBBY_LEFT);
        }catch (Exception e){
            System.out.println("LobbyMediator: error in unsubscribing" + e.getMessage());
        }

        subscribers.forEach((nick, pair)->{
            if(nick != nickname) {
                try {
                    pair.first().updateLobby(DiffGenerator.diffRemoveUserFromLobby(nickname));
                    pair.second().log(nickname + LogsOnClientStatic.LOBBY_LEFT_OTHER);
                }catch (Exception e){
                    System.out.println("LobbyMediator: error in notifying other user" + e.getMessage());
                }
            }
        });

        super.unsubscribe(nickname);
    }
}
