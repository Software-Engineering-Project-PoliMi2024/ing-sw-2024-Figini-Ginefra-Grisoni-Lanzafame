package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.diffObserverInterface.DiffSubscriber;
import it.polimi.ingsw.lightModel.diffs.LobbyDiff;
import it.polimi.ingsw.lightModel.diffs.LobbyDiffEdit;
import it.polimi.ingsw.lightModel.diffs.LobbyDiffEditLogin;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.LittleBoyLobby;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyDiffPublisher {
    private final Map<DiffSubscriber, String> subscribers;

    public LobbyDiffPublisher() {
        this.subscribers = new HashMap<>();
    }
    /**
     * @param diffSubscriber the subscriber of the user that joins the lobby
     */
    public synchronized void subscribe(DiffSubscriber diffSubscriber, String nickname, String gameName, int numberOfMaxPlayer) {
        LobbyDiffEdit others = createDiffSubscribed(nickname);
        for(DiffSubscriber subscriber : subscribers.keySet()){
            notifySubscriber(subscriber, others);
        }
        subscribers.put(diffSubscriber, nickname);
        LobbyDiffEditLogin yours = createDiffSubscriber(gameName, numberOfMaxPlayer);
        notifySubscriber(diffSubscriber, yours);
    }
    /**
     * @return the diff for the "others" people already in the lobby about the new player "nickname"
     */
    private LobbyDiffEdit createDiffSubscribed(String nickname){
        // create a list of the nickname containing the nick of the new subscriber
        ArrayList<String> addNicknames = new ArrayList<>();
        addNicknames.add(nickname);
        return new LobbyDiffEdit(addNicknames, new ArrayList<>());
    }

    /**
     * @param gameName name of the lobby being logged
     * @param numberOfMaxPlayer required to start the game from the lobby
     * @return a LobbyDiffEditLogin for the new subscriber
     */
    private LobbyDiffEditLogin createDiffSubscriber(String gameName, int numberOfMaxPlayer){
        // create a list of the nickname already in the lobby
        ArrayList<String> addNicknames = new ArrayList<>(subscribers.values());
        return new LobbyDiffEditLogin(addNicknames, new ArrayList<>(), gameName, numberOfMaxPlayer);
    }

    /**
     * Remove diffUnsubscriber as require by the DiffPublisherNick Interface
     * @param diffUnsubscriber the subscriber being removed
     */
    public synchronized void unsubscribe(DiffSubscriber diffUnsubscriber) {
        subscribers.remove(diffUnsubscriber);
    }

    public synchronized void unsubscribe(DiffSubscriber diffUnsubscriber, String gameName) {
        notifySubscriber(diffUnsubscriber, new LittleBoyLobby());
        String unsubscriberNick = subscribers.get(diffUnsubscriber);
        unsubscribe(diffUnsubscriber);
        LobbyDiffEdit others = createDiffUnsubscriber(unsubscriberNick);
        for (DiffSubscriber subscriber : subscribers.keySet()) {
            notifySubscriber(subscriber, others);
        }

    }

    /**
     * @param unsubscriberNickname the nick of who is leaving the lobby
     * @return the LobbyDiffEdit for the remaining people in the lobby
     */
    private LobbyDiffEdit createDiffUnsubscriber(String unsubscriberNickname){
        ArrayList<String> rmvNicknames = new ArrayList<>();
        rmvNicknames.add(unsubscriberNickname);
        ArrayList<String> addNicknames = new ArrayList<>();
        return new LobbyDiffEdit(addNicknames, rmvNicknames);
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

