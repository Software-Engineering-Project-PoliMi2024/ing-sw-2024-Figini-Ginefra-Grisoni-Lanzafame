package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.LobbyListDiffEdit;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.FatManLobbyList;
import it.polimi.ingsw.lightModel.diffs.LobbyListDiff;
import it.polimi.ingsw.model.tableReleted.LobbyList;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class LobbyListDiffPublisher {
    private final List<DiffSubscriber> diffSubscribers;
    private final LobbyList lobbyList;
    public LobbyListDiffPublisher(LobbyList lobbyList) {
        this.diffSubscribers = new ArrayList<>();
        this.lobbyList = lobbyList;
    }
    public synchronized void subscribe(DiffSubscriber diffSubscriber) {
        diffSubscribers.add(diffSubscriber);
        this.notifySubscriber(diffSubscriber, new LobbyListDiffEdit(new ArrayList<>(lobbyList.getLobbies().stream().map(Lightifier::lightify).toList()),new ArrayList<>()));
    }

    public synchronized void unsubscribe(DiffSubscriber diffSubscriber) {
        diffSubscribers.remove(diffSubscriber);
        notifySubscriber(diffSubscriber, new FatManLobbyList());
    }
    public synchronized void subscribe(LobbyListDiffEdit lobbyListDiff) {
        for(DiffSubscriber diffSubscriber : diffSubscribers){
            this.notifySubscriber(diffSubscriber, lobbyListDiff);
        }
    }
    public synchronized void notifySubscriber(DiffSubscriber diffSubscriber, LobbyListDiff lobbyListDiff) {
        try {
            diffSubscriber.updateLobbyList(lobbyListDiff);
        }catch (RemoteException r){
            r.printStackTrace();
        }
    }
}
