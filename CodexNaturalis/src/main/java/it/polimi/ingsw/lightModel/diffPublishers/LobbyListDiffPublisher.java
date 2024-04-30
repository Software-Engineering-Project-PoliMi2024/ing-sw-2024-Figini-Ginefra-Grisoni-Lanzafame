package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffObserverInterface.DiffPubliher;
import it.polimi.ingsw.lightModel.diffObserverInterface.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffs.LobbyListDiffEdit;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.FatManLobbyList;
import it.polimi.ingsw.model.tableReleted.LobbyList;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class LobbyListDiffPublisher implements DiffPubliher {
    private final List<DiffSubscriber> diffSubscribers;
    private final LobbyListDiffEdit lobbyListDiff;
    private final LobbyList lobbyList;
    public LobbyListDiffPublisher(LobbyList lobbyList) {
        this.diffSubscribers = new ArrayList<>();
        this.lobbyListDiff = new LobbyListDiffEdit(new ArrayList<>(), new ArrayList<>());
        this.lobbyList = lobbyList;
    }
    @Override
    public void subscribe(DiffSubscriber diffSubscriber) {
        synchronized (diffSubscribers) {
            diffSubscribers.add(diffSubscriber);
            this.notifySubscriber(diffSubscriber, new LobbyListDiffEdit(new ArrayList<>(lobbyList.getLobbies().stream().map(Lightifier::lightify).toList()),new ArrayList<>()));
        }
    }

    @Override
    public synchronized void unsubscribe(DiffSubscriber diffSubscriber) {
        diffSubscribers.remove(diffSubscriber);
        try {
            diffSubscriber.updateLobbyList(new FatManLobbyList());
        }catch (RemoteException r){
            r.printStackTrace();
        }
    }
    public void notifySubscriber(DiffSubscriber diffSubscriber, LobbyListDiffEdit lobbyListDiff) {
        try {
            diffSubscriber.updateLobbyList(lobbyListDiff);
        }catch (RemoteException r){
            r.printStackTrace();
        }
    }
    public void subscribe(LobbyListDiffEdit lightLobbyDiff) {
        synchronized (diffSubscribers){
            lobbyListDiff.updateLobbyListDiff(lightLobbyDiff);
        }
        this.notifySubscriber();
    }
    @Override
    public void notifySubscriber() {
        synchronized (diffSubscribers){
            for (DiffSubscriber diffSubscriber : diffSubscribers) {
                this.notifySubscriber(diffSubscriber, lobbyListDiff);
            }
        }
    }

}
