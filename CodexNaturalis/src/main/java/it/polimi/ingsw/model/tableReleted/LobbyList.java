package it.polimi.ingsw.model.tableReleted;

import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffPublishers.DiffPublisher;
import it.polimi.ingsw.lightModel.diffPublishers.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiffEdit;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LobbyList implements Serializable, DiffPublisher<LightLobbyList> {
    private final List<DiffSubscriber> diffSubscribers = new ArrayList<>();
    private final Set<Lobby> lobbies;

    public LobbyList(){
        lobbies = new HashSet<>();
    }
    public Set<Lobby> getLobbies() {
        return lobbies;
    }

    public void addLobby(Lobby lobby){
        lobbies.add(lobby);
        notifySubscribers(addLobbyDiff(lobby));
    }

    private LobbyListDiffEdit addLobbyDiff(Lobby lobby) {
        ArrayList<LightLobby> listDiffAdd = new ArrayList<>();
        listDiffAdd.add(Lightifier.lightify(lobby));
        return new LobbyListDiffEdit(listDiffAdd, new ArrayList<>());
    }

    public void remove(Lobby lobby){
        lobbies.remove(lobby);
        notifySubscribers(removeLobbyDiff(lobby));
    }

    private LobbyListDiffEdit removeLobbyDiff(Lobby lobby) {
        ArrayList<LightLobby> listDiffRmv = new ArrayList<>();
        listDiffRmv.add(Lightifier.lightify(lobby));
        return new LobbyListDiffEdit(new ArrayList<>(), listDiffRmv);
    }

    public void subscribe(DiffSubscriber diffSubscriber) {
        diffSubscribers.add(diffSubscriber);
    }
    public void unsubscribe(DiffSubscriber diffSubscriber) {
        diffSubscribers.remove(diffSubscriber);
    }

    @Override
    public void notifySubscribers(ModelDiffs<LightLobbyList> diff) {
        for(DiffSubscriber diffSubscriber : diffSubscribers){
            diffSubscriber.updateLobbyList(diff);
        }
    }
}
