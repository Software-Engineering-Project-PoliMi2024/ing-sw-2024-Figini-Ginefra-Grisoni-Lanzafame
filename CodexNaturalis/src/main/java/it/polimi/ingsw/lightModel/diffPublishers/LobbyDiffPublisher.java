package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.diffObserverInterface.DiffPubliher;
import it.polimi.ingsw.lightModel.diffObserverInterface.DiffPublisherNick;
import it.polimi.ingsw.lightModel.diffObserverInterface.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffs.LobbyDiff;
import it.polimi.ingsw.lightModel.diffs.LobbyDiffEdit;
import it.polimi.ingsw.lightModel.diffs.LobbyListDiffEdit;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.LittleBoyLobby;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyDiffPublisher implements DiffPublisherNick {
    private final Map<DiffSubscriber, String> subscribers;
    //private final Map<DiffSubscriber, LobbyDiffEdit> lobbyDiffMap;

    public LobbyDiffPublisher() {
        this.subscribers = new HashMap<>();
    }
    /**
     * Add the subscriber in the subscribers map as require by the DiffPublisherNick Interface
     * @param diffSubscriber the subscriber of the user that joins the lobby
     */
    public synchronized void subscribe(DiffSubscriber diffSubscriber, String nickname) {
        subscribers.put(diffSubscriber, nickname);
    }
    /**
     * @param diffSubscriber the subscriber of the user that joins the lobby
     */
    public synchronized void subscribe(DiffSubscriber diffSubscriber, String nickname, String gameName) {
        LobbyDiffEdit others = createDiffSubscribed(nickname, gameName);
        for(DiffSubscriber subscriber : subscribers.keySet()){
            notifySubscriber(subscriber, others);
        }
        subscribe(diffSubscriber, nickname);
        LobbyDiffEdit yours = createDiffSubscriber(gameName);
        notifySubscriber(diffSubscriber, yours);
    }
    /**
     * @return the diff for the "others" people already in the lobby
     */
    private LobbyDiffEdit createDiffSubscribed(String nickname, String gameName){
        // create a list of the nickname containing the nick of the new subscriber
        ArrayList<String> addNicknames = new ArrayList<>();
        addNicknames.add(nickname);
        return new LobbyDiffEdit(addNicknames, new ArrayList<>(), gameName);
    }

    /**
     * @return the diff for the new subscriber
     */
    private LobbyDiffEdit createDiffSubscriber(String gameName){
        // create a list of the nickname already in the lobby
        ArrayList<String> addNicknames = new ArrayList<>(subscribers.values());
        return new LobbyDiffEdit(addNicknames, new ArrayList<>(), gameName);
    }

    /**
     * Remove diffUnsubscriber as require by the DiffPublisherNick Interface
     * @param diffUnsubscriber the subscriber being removed
     */
    @Override
    public synchronized void unsubscribe(DiffSubscriber diffUnsubscriber) {
        subscribers.remove(diffUnsubscriber);
    }

    public synchronized void unsubscribe(DiffSubscriber diffUnsubscriber, String gameName) {
        notifySubscriber(diffUnsubscriber, new LittleBoyLobby());
        String unsubscriberNick = subscribers.get(diffUnsubscriber);
        unsubscribe(diffUnsubscriber);
        LobbyDiffEdit others = createDiffUnsubscriber(unsubscriberNick, gameName);
        for (DiffSubscriber subscriber : subscribers.keySet()) {
            notifySubscriber(subscriber, others);
        }

    }

    /**
     * @param unsubscriberNickname the nick of who is leaving the lobby
     * @param gameName the name of the lobby being leaved
     * @return the LobbyDiffEdit for the remaining people in the lobby
     */
    private LobbyDiffEdit createDiffUnsubscriber(String unsubscriberNickname, String gameName){
        ArrayList<String> rmvNicknames = new ArrayList<>();
        rmvNicknames.add(unsubscriberNickname);
        ArrayList<String> addNicknames = new ArrayList<>();
        return new LobbyDiffEdit(addNicknames, rmvNicknames, gameName);
    }

    /**
     * @param diffSubscriber being notified
     * @param diff to be notified to the diffSubscriber
     */
    public synchronized void notifySubscriber(DiffSubscriber diffSubscriber, LobbyDiff diff){
        try{
            diffSubscriber.updateLobby(diff);
        }catch (RemoteException r){
            r.printStackTrace();
        }
    }

    /**
     * @return a List of all DiffSubscriber present in the lobby
     */
    public List<DiffSubscriber> getSubscribers(){
        return new ArrayList<>(subscribers.keySet());
    }
}

