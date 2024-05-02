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
     * @param diffSubscriber the subscriber of the user that joins the lobby
     */
    public synchronized void subscribe(DiffSubscriber diffSubscriber, String nickname) {
        LobbyDiffEdit others = createDiffSubscribed(nickname);
        for(DiffSubscriber subscriber : subscribers.keySet()){
            notifySubscriber(subscriber, others);
        }
        subscribers.put(diffSubscriber, nickname);
        LobbyDiffEdit yours = createDiffSubscriber();
        notifySubscriber(diffSubscriber, yours);
    }
    /**
     * @return the diff for the people already in the lobby
     */
    private LobbyDiffEdit createDiffSubscribed(String nickname){
        // create a list of the nickname containing the nick of the new subscriber
        ArrayList<String> addNicknames = new ArrayList<>();
        addNicknames.add(nickname);
        return new LobbyDiffEdit(addNicknames, new ArrayList<>());
    }

    /**
     * @return the diff for the new subscriber
     */
    private LobbyDiffEdit createDiffSubscriber(){
        // create a list of the nickname already in the lobby
        ArrayList<String> addNicknames = new ArrayList<>(subscribers.values());
        return new LobbyDiffEdit(addNicknames, new ArrayList<>());
    }

    @Override
    public synchronized void unsubscribe(DiffSubscriber diffUnsubscriber) {
        notifySubscriber(diffUnsubscriber, new LittleBoyLobby());
        String unsubscriberNick = subscribers.get(diffUnsubscriber);
        subscribers.remove(diffUnsubscriber);

        LobbyDiffEdit others = createDiffUnsubscriber(unsubscriberNick);
        for (DiffSubscriber subscriber : subscribers.keySet()) {
            notifySubscriber(subscriber, others);
        }
    }
    private LobbyDiffEdit createDiffUnsubscriber(String unsubscriberNickname){
        ArrayList<String> rmvNicknames = new ArrayList<>();
        rmvNicknames.add(unsubscriberNickname);
        ArrayList<String> addNicknames = new ArrayList<>();
        return new LobbyDiffEdit(addNicknames, rmvNicknames);
    }
    public synchronized void notifySubscriber(DiffSubscriber diffSubscriber, LobbyDiff diff){
        try{
            diffSubscriber.updateLobby(diff);
        }catch (RemoteException r){
            r.printStackTrace();
        }
    }
    public List<DiffSubscriber> getSubscribers(){
        return new ArrayList<>(subscribers.keySet());
    }
}

