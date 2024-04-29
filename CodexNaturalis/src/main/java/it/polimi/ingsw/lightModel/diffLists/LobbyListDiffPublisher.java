package it.polimi.ingsw.lightModel.diffLists;

import it.polimi.ingsw.lightModel.diffs.LobbyListDiff;

import java.util.ArrayList;
import java.util.List;

public class LobbyListDiffPublisher implements DiffPublisherDouble<LobbyListDiff> {
    private final List<DiffSubscriber> diffSubscribers;
    private final LobbyListDiff lobbyListDiff;

    public LobbyListDiffPublisher() {
        this.diffSubscribers = new ArrayList<>();
        this.lobbyListDiff = new LobbyListDiff(new ArrayList<>(), new ArrayList<>());
    }
    @Override
    public void subscribe(DiffSubscriber diffSubscriber) {
        synchronized (diffSubscribers) {
            diffSubscribers.add(diffSubscriber);
        }
        synchronized (lobbyListDiff){
            this.notifySubscriber(diffSubscriber, new LobbyListDiff(new ArrayList<>(diffSubscriber.getLobbyList().getLobbies()),new ArrayList<>()));
        }
    }

    @Override
    public void unsubscribe(DiffSubscriber diffSubscriber) {
        synchronized (diffSubscribers){
            diffSubscribers.remove(diffSubscriber);
        }
    }

    @Override
    public void notifySubscriber(DiffSubscriber diffSubscriber, LobbyListDiff lobbyListDiff) {
        diffSubscriber.updateLobbyList(lobbyListDiff);
    }

    @Override
    public void subscribe(LobbyListDiff lightLobbyDiff) {
        synchronized (lobbyListDiff){
            lobbyListDiff.updateLobbyListDiff(lightLobbyDiff);
        }
        this.notifySubscriber();
    }

    @Override
    public void unsubscribe(LobbyListDiff lightLobbyDiff) {
        synchronized (lobbyListDiff){
            lobbyListDiff.updateLobbyListDiff(lightLobbyDiff);
        }
        this.notifySubscriber();
    }
    @Override
    public void notifySubscriber() {
        synchronized (lobbyListDiff){
            for (DiffSubscriber diffSubscriber : diffSubscribers) {
                this.notifySubscriber(diffSubscriber, lobbyListDiff);
            }
        }
    }

}
