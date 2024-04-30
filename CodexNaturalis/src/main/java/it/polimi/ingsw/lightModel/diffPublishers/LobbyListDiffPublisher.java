package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.diffObserverInterface.DiffPubliher;
import it.polimi.ingsw.lightModel.diffObserverInterface.DiffSubscriber;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobbyList;
import it.polimi.ingsw.lightModel.diffs.LobbyListDiff;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class LobbyListDiffPublisher implements DiffPubliher {
    private final List<DiffSubscriber> diffSubscribers;
    private final LobbyListDiff lobbyListDiff;
    private final LightLobbyList lobbyList;
    public LobbyListDiffPublisher(LightLobbyList lobbyList) {
        this.diffSubscribers = new ArrayList<>();
        this.lobbyListDiff = new LobbyListDiff(new ArrayList<>(), new ArrayList<>());
        this.lobbyList = lobbyList;
    }
    @Override
    public void subscribe(DiffSubscriber diffSubscriber) {
        synchronized (diffSubscribers) {
            diffSubscribers.add(diffSubscriber);
            this.notifySubscriber(diffSubscriber, new LobbyListDiff(new ArrayList<>(lobbyList.getLobbies()),new ArrayList<>()));
        }
    }

    @Override
    public void unsubscribe(DiffSubscriber diffSubscriber) {
        synchronized (diffSubscribers){
            diffSubscribers.remove(diffSubscriber);
        }
    }
    public void notifySubscriber(DiffSubscriber diffSubscriber, LobbyListDiff lobbyListDiff) {
        try {
            diffSubscriber.updateLobbyList(lobbyListDiff);
        }catch (RemoteException r){
            r.printStackTrace();
        }
    }
    public void subscribe(LobbyListDiff lightLobbyDiff) {
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
