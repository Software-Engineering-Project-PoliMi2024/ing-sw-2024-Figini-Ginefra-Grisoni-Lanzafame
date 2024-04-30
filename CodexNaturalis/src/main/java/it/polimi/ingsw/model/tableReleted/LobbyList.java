package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.lightModel.diffObserverInterface.DiffSubscriber;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LobbyList implements Serializable{
    private final List<DiffSubscriber> diffSubscribers;
    private final Set<Lobby> lobbies;
    public LobbyList(){
        lobbies = new HashSet<>();
        diffSubscribers = new ArrayList<>();
    }
    public LobbyList(HashSet<Lobby> lobbies){
        this.lobbies = lobbies;
        diffSubscribers = new ArrayList<>();
    }
    public Set<Lobby> getLobbies() {
        return lobbies;
    }

    public List<DiffSubscriber> getDiffSubscribers() {
        return diffSubscribers;
    }
    public boolean addLobby(Lobby lobby){
        return lobbies.add(lobby);
    }
    public void remove(Lobby lobby){
        lobbies.remove(lobby);
    }
}
