package it.polimi.ingsw.lightModel.diffLists;

import it.polimi.ingsw.lightModel.diffs.LobbyDiff;


import java.util.ArrayList;
import java.util.Map;

public class LobbyDiffPublisher implements DiffPublisher{
    private final Map<DiffSubscriber, LobbyDiff> lobbyDiffMap;

    public LobbyDiffPublisher(Map<DiffSubscriber, LobbyDiff> lobbyDiffMap) {
        this.lobbyDiffMap = lobbyDiffMap;
    }
/**
     * @param diffSubscriber the subscriber of the user that joins the lobby
     */
    @Override
    public void subscribe(DiffSubscriber diffSubscriber) {
        synchronized (lobbyDiffMap) {
            // notify to the people already in the lobby the new subscriber
            lobbyDiffMap.replaceAll((s, v) -> createDiffSubscribed(v, diffSubscriber.getNickname()));
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
                new ArrayList<>(this.lobbyDiffMap.keySet().stream()
                        .map(DiffSubscriber::getNickname).toList());
        return new LobbyDiff(addNicknames, rmvNicknames);
    }

    @Override
    public void unsubscribe(DiffSubscriber diffSubscriber) {
        synchronized (lobbyDiffMap) {
            lobbyDiffMap.remove(diffSubscriber);
            for (DiffSubscriber subscriber : lobbyDiffMap.keySet()) {
                // notify to the people already in the lobby the new subscriber
                lobbyDiffMap.replaceAll((s,v)->createDiffUnsubscriber(v, diffSubscriber));
                ;
            }
        }
    }
    private LobbyDiff createDiffUnsubscriber(LobbyDiff diffToModify, DiffSubscriber diffSubscriber){
        ArrayList<String> rmvNicknames = new ArrayList<>();
        rmvNicknames.add(diffSubscriber.getNickname());
        ArrayList<String> addNicknames = new ArrayList<>();
        return new LobbyDiff(diffToModify, addNicknames, rmvNicknames);
    }
    @Override
    public void notifySubscriber() {
        /*for(DiffSubscriber subscriber : lobbyDiffMap.keySet()){
            subscriber.update(this.lobbyDiffMap.get(subscriber));
        }*/
    }
}
