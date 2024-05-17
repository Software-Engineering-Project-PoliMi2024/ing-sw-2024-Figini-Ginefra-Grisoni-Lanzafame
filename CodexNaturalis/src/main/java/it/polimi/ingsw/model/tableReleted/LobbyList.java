package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffPublishers.DiffPublisher;
import it.polimi.ingsw.lightModel.diffPublishers.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffPublishers.LobbyListDiffPublisher;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiffEdit;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LobbyList implements Serializable {
    private final Set<Lobby> lobbies;
    private final LobbyListDiffPublisher lobbyListDiffPublisher = new LobbyListDiffPublisher();

    public LobbyList(){
        lobbies = new HashSet<>();
    }
    public Set<Lobby> getLobbies() {
        return lobbies;
    }

    public void addLobby(Lobby lobby){
        lobbies.add(lobby);
        lobbyListDiffPublisher.addLobbyNotification(lobby);
    }

    public void remove(Lobby lobby){
        lobbies.remove(lobby);
        lobbyListDiffPublisher.removeLobbyNotification(lobby);
    }

    public void subscribe(DiffSubscriber diffSubscriber) {
        lobbyListDiffPublisher.subscribe(diffSubscriber);
    }
    public void unsubscribe(DiffSubscriber diffSubscriber) {
        lobbyListDiffPublisher.unsubscribe(diffSubscriber);
    }

}
