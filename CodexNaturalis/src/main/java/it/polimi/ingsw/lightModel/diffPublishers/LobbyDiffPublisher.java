package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.diffObserverInterface.DiffPublisherNick;
import it.polimi.ingsw.lightModel.diffObserverInterface.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffs.LobbyDiffEdit;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.FatManLobbyList;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.LittleBoyLobby;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyDiffPublisher implements DiffPublisherNick {
    private final Map<DiffSubscriber, String> subscribers;
    private final Map<DiffSubscriber, LobbyDiffEdit> lobbyDiffMap;

    public LobbyDiffPublisher() {
        this.subscribers = new HashMap<>();
        this.lobbyDiffMap = new HashMap<>();
    }
    /**
     * @param diffSubscriber the subscriber of the user that joins the lobby
     */
    public synchronized void subscribe(DiffSubscriber diffSubscriber, String nickname) {
            subscribers.put(diffSubscriber, nickname);
            // notify to the people already in the lobby the new subscriber
            lobbyDiffMap.replaceAll((diff, lobby) -> createDiffSubscribed(lobby, nickname));
            lobbyDiffMap.put(diffSubscriber, createDiffSubscriber());
            // notify all the subscribers of the new subscriber
            this.notifySubscriber();
    }
    /**
     * @return the diff for the people already in the lobby
     */
    private LobbyDiffEdit createDiffSubscribed(LobbyDiffEdit diffToModify, String nickname){
        // create a list of the nickname containing the nick of the new subscriber
        ArrayList<String> addNicknames = new ArrayList<>();
        addNicknames.add(nickname);
        return new LobbyDiffEdit(diffToModify, addNicknames, new ArrayList<>());
    }

    /**
     * @return the diff for the new subscriber
     */
    private LobbyDiffEdit createDiffSubscriber(){
        // create a list of the nickname already in the lobby
        ArrayList<String> addNicknames =
                new ArrayList<>(subscribers.values());
        return new LobbyDiffEdit(addNicknames, new ArrayList<>());
    }

    @Override
    public synchronized void unsubscribe(DiffSubscriber diffSubscriber) {
        for (DiffSubscriber subscriber : lobbyDiffMap.keySet()) {
            List<String> toRem = new ArrayList<>();
            toRem.add(subscribers.get(diffSubscriber));
            // notify to the people already in the lobby that one user unsubscribed subscriber
            try {
                subscriber.updateLobby(new LobbyDiffEdit(toRem, new ArrayList<>()));
            }catch (RemoteException r){
                r.printStackTrace();
            }
            lobbyDiffMap.put(subscriber, createDiffUnsubscriber(lobbyDiffMap.get(subscriber), diffSubscriber));
        }
        this.notifySubscriber();
        try {
            diffSubscriber.updateLobby(new LittleBoyLobby());
        }catch (RemoteException r){
            r.printStackTrace();
        }
        subscribers.remove(diffSubscriber);
        lobbyDiffMap.remove(diffSubscriber);
    }
    private LobbyDiffEdit createDiffUnsubscriber(LobbyDiffEdit diffToModify, DiffSubscriber diffSubscriber){
        ArrayList<String> rmvNicknames = new ArrayList<>();
        rmvNicknames.add(subscribers.get(diffSubscriber));
        ArrayList<String> addNicknames = new ArrayList<>();
        return new LobbyDiffEdit(diffToModify, addNicknames, rmvNicknames);
    }
    @Override
    public synchronized void notifySubscriber() {
        for(DiffSubscriber subscriber : lobbyDiffMap.keySet())
            try {
                subscriber.updateLobby(this.lobbyDiffMap.get(subscriber));
            }catch (RemoteException r){
                r.printStackTrace();
            }
        for(DiffSubscriber subscriber : lobbyDiffMap.keySet())
            lobbyDiffMap.put(subscriber, new LobbyDiffEdit(new ArrayList<>(), new ArrayList<>()));

    }
    public synchronized List<DiffSubscriber> getSubscribers(){
        return new ArrayList<>(lobbyDiffMap.keySet());
    }
}

