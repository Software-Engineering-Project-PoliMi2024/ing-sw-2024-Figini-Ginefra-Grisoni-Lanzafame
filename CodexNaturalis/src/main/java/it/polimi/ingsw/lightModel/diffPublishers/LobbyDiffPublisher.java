package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiff;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiffEdit;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiffEditLogin;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.LittleBoyLobby;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LobbyDiffPublisher {
    private final Map<DiffSubscriber, String> subscribers;

    /**
     * Constructor for the LobbyDiffPublisher
     */
    public LobbyDiffPublisher() {
        this.subscribers = new HashMap<>();
    }
    /**
     * the subscriber will receive the current state of the lobby: its name, the number of max players
     * and the list of the players already in the lobby
     * the players already in the lobby will receive the new subscriber nickname
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
     * @return a LobbyDiffEditLogin for the new subscriber containing the information of the lobby
     */
    private LobbyDiffEditLogin createDiffSubscriber(String gameName, int numberOfMaxPlayer){
        // create a list of the nickname already in the lobby
        ArrayList<String> addNicknames = new ArrayList<>(subscribers.values());
        return new LobbyDiffEditLogin(addNicknames, new ArrayList<>(), gameName, numberOfMaxPlayer);
    }

    /**
     * remove the subscriber from the lobbyPublisher
     * resets the LightLobby stored local on the subscriber
     * notifies the other subscribers of the leaving of the unsubscriber
     * @param diffUnsubscriber the subscriber being removed
     */
    public void unsubscribe(DiffSubscriber diffUnsubscriber) {
        notifySubscriber(diffUnsubscriber, new LittleBoyLobby());
        String unsubscriberNick = subscribers.get(diffUnsubscriber);
        subscribers.remove(diffUnsubscriber);
        LobbyDiffEdit others = createDiffUnsubscriber(unsubscriberNick);
        for (DiffSubscriber subscriber : subscribers.keySet()) {
            notifySubscriber(subscriber, others);
        }

    }

    /**
     * @param unsubscriberNickname the nick of who is leaving the lobby
     * @return the LobbyDiffEdit for the remaining people in the lobby about the unsubscriber
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
        diffSubscriber.updateLobby(diff);
    }

    public synchronized void clear(){
        for(DiffSubscriber diffSubscriber : subscribers.keySet()){
            diffSubscriber.updateLobby(new LittleBoyLobby());
        }
        subscribers.clear();
    }

    public synchronized void notifyStateDiff(DiffSubscriber diffSubscriber){

    }
}

