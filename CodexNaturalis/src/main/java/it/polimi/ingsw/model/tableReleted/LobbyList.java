package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.lightModel.diffPublishers.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffPublishers.LobbyListDiffPublisher;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiffEdit;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class LobbyList implements Serializable{
    private final LobbyListDiffPublisher lobbyListDiffPublisher;
    private final Set<Lobby> lobbies;
    public LobbyList(){
        lobbies = new HashSet<>();
        this.lobbyListDiffPublisher = new LobbyListDiffPublisher(this);
    }
    public LobbyList(HashSet<Lobby> lobbies){
        this.lobbies = lobbies;
        this.lobbyListDiffPublisher = new LobbyListDiffPublisher(this);
    }
    public Set<Lobby> getLobbies() {
        return lobbies;
    }

    public boolean addLobby(Lobby lobby){
        return lobbies.add(lobby);
    }
    public void remove(Lobby lobby){
        lobbies.remove(lobby);
    }
    public void subscribe(DiffSubscriber diffSubscriber) {
        lobbyListDiffPublisher.subscribe(diffSubscriber);
    }
    public void unsubscribe(DiffSubscriber diffSubscriber) {
        lobbyListDiffPublisher.unsubscribe(diffSubscriber);
    }
    public void subscribe(LobbyListDiffEdit lightLobbyDiff){
        lobbyListDiffPublisher.subscribe(lightLobbyDiff);
    }
}
