package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiffEdit;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.model.tableReleted.Lobby;

import java.util.ArrayList;
import java.util.List;

public class LobbyListDiffPublisher implements DiffPublisher<LightLobbyList>{
    private final List<DiffSubscriber> diffSubscribers = new ArrayList<>();

    public void addLobbyNotification(Lobby lobby){
        notifySubscribers(addLobbyDiffCalc(lobby));
    }
    private LobbyListDiffEdit addLobbyDiffCalc(Lobby lobby) {
        ArrayList<LightLobby> listDiffAdd = new ArrayList<>();
        listDiffAdd.add(Lightifier.lightify(lobby));
        return new LobbyListDiffEdit(listDiffAdd, new ArrayList<>());
    }

    public void removeLobbyNotification(Lobby lobby){
        notifySubscribers(removeLobbyDiffCalc(lobby));
    }

    private LobbyListDiffEdit removeLobbyDiffCalc(Lobby lobby) {
        ArrayList<LightLobby> listDiffRmv = new ArrayList<>();
        listDiffRmv.add(Lightifier.lightify(lobby));
        return new LobbyListDiffEdit(new ArrayList<>(), listDiffRmv);
    }

    @Override
    public void subscribe(DiffSubscriber diffSubscriber) {
        diffSubscribers.add(diffSubscriber);
    }

    @Override
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
