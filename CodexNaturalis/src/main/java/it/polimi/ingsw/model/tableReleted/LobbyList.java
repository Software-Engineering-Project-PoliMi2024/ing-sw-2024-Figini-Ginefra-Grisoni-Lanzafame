package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.controller3.mediators.LobbyListMediator;
import it.polimi.ingsw.lightModel.LightModelUpdaterInterfaces.LightLobbyListUpdater;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiff;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.view.LoggerInterface;
import it.polimi.ingsw.view.ViewInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LobbyList implements Serializable {
    private final Set<Lobby> lobbies;
    private final LobbyListMediator mediator = new LobbyListMediator();

    public LobbyList(){
        lobbies = new HashSet<>();
    }
    public Set<Lobby> getLobbies() {
        return lobbies;
    }

    public void addLobby(Lobby lobby){
        lobbies.add(lobby);
    }

    public void remove(Lobby lobby){
        lobbies.remove(lobby);
    }
    /**
     * Subscribes viewInterface to the mediator
     * passes the lobbyHistory to the mediator
     * the viewInterface is updated with the lobbyHistory and the logs of the join
     * @param nickname the subscriber's nickname
     * @param loggerUpdater the logger connected to the subscriber
     */
    public void subscribe(String nickname, ViewInterface loggerUpdater){
        mediator.subscribe(nickname, loggerUpdater, new ArrayList<>(this.lobbies));
    }
    /**
     * Unsubscribes the subscriber with the nickname
     * passed as parameter from the lobbyList mediator
     * @param nickname the unSubscriber's nickname
     */
    public void unsubscribe(String nickname){
        mediator.unsubscribe(nickname);
    }
    /**
     * Notifies all the subscribers that a lobby has been added
     * Adds the lobby to the lightLobbyList connected to the updater
     * @param creator the subscriber that created the lobby
     * @param addedLobby the lobby added to the lobbyList
     */
    public void notifyNewLobby(String creator, Lobby addedLobby){
        mediator.notifyNewLobby(creator, addedLobby);
    }
    /**
     * Notifies the subscriber that removed the lobby
     * Removes the lobby from the lightLobbyList connected to the updater
     * @param destroyer the subscriber that removed the lobby
     * @param removedLobby the lobby removed from the lobbyList
     */
    public void notifyLobbyRemoved(String destroyer, Lobby removedLobby){
        mediator.notifyLobbyRemoved(destroyer, removedLobby);
    }
}
