package it.polimi.ingsw.lightModel.diffLists;

import it.polimi.ingsw.lightModel.diffs.LobbyDiff;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyDiffPublisher implements DiffPublisherNick {
    Map<DiffSubscriber, String> subscribers;
    private final Map<DiffSubscriber, LobbyDiff> lobbyDiffMap;

    public LobbyDiffPublisher() {
        this.lobbyDiffMap = new HashMap<>();
    }
    /**
     * @param diffSubscriber the subscriber of the user that joins the lobby
     */
    public void subscribe(DiffSubscriber diffSubscriber, String nickname) {
        synchronized (lobbyDiffMap) {
            subscribers.put(diffSubscriber, nickname);
            // notify to the people already in the lobby the new subscriber
            lobbyDiffMap.replaceAll((s, v) -> createDiffSubscribed(v, subscribers.get(diffSubscriber)));
            lobbyDiffMap.put(diffSubscriber, createDiffSubscriber());
            // notify all the subscribers of the new subscriber
            this.notifySubscriber();
        }
    }
    /**
     * @return the diff for the people already in the lobby
     */
    private LobbyDiff createDiffSubscribed(LobbyDiff diffToModify, String nickname){
        ArrayList<String> rmvNicknames = new ArrayList<>();
        // create a list of the nickname containing the nick of the new subscriber
        ArrayList<String> addNicknames = new ArrayList<>();
        addNicknames.add(nickname);
        return new LobbyDiff(diffToModify, addNicknames, rmvNicknames);
    }

    /**
     * @return the diff for the new subscriber
     */
    private LobbyDiff createDiffSubscriber(){
        ArrayList<String> rmvNicknames = new ArrayList<>();
        // create a list of the nickname already in the lobby
        ArrayList<String> addNicknames =
                new ArrayList<>(subscribers.values());
        return new LobbyDiff(addNicknames, rmvNicknames);
    }

    @Override
    public void unsubscribe(DiffSubscriber diffSubscriber) {
        synchronized (lobbyDiffMap) {
            subscribers.remove(diffSubscriber);
            lobbyDiffMap.remove(diffSubscriber);
            for (DiffSubscriber subscriber : lobbyDiffMap.keySet()) {
                // notify to the people already in the lobby the new subscriber
                lobbyDiffMap.replaceAll((s,v)->createDiffUnsubscriber(v, subscriber));
            }
            this.notifySubscriber();
        }
    }
    private LobbyDiff createDiffUnsubscriber(LobbyDiff diffToModify, DiffSubscriber diffSubscriber){
        ArrayList<String> rmvNicknames = new ArrayList<>();
        rmvNicknames.add(subscribers.get(diffSubscriber));
        ArrayList<String> addNicknames = new ArrayList<>();
        return new LobbyDiff(diffToModify, addNicknames, rmvNicknames);
    }
    @Override
    public void notifySubscriber() {
        for(DiffSubscriber subscriber : lobbyDiffMap.keySet())
            subscriber.updateLobby(this.lobbyDiffMap.get(subscriber));
        for(DiffSubscriber subscriber : lobbyDiffMap.keySet())
            synchronized (lobbyDiffMap) {
                lobbyDiffMap.put(subscriber, new LobbyDiff(new ArrayList<>(), new ArrayList<>()));
            }
    }
    public List<DiffSubscriber> getSubscribers(){
        return new ArrayList<>(lobbyDiffMap.keySet());
    }
}

