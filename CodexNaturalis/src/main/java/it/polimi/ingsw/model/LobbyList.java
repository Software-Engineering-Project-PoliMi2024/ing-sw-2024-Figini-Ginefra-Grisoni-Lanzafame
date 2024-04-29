package it.polimi.ingsw.model;

import it.polimi.ingsw.lightModel.diffLists.DiffSubscriber;
import it.polimi.ingsw.model.tableReleted.Lobby;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LobbyList {

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
