package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.diffs.ModelDiffs;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiff;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiffEdit;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyDiffEditLogin;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.LittleBoyLobby;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import it.polimi.ingsw.model.tableReleted.Lobby;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyDiffPublisher implements DiffPublisher<LightLobby, DiffSubscriberLobby> {
    private final List<DiffSubscriberLobby> subscribers;

    /**
     * Constructor for the LobbyDiffPublisher
     */
    public LobbyDiffPublisher() {
        this.subscribers = new ArrayList<>();
    }

    @Override
    public void subscribe(DiffSubscriberLobby diffSubscriber) {
        LobbyDiffEdit others = createDiffSubscribed(diffSubscriber.getNickname());
        notifySubscribers(others);
        subscribers.add(diffSubscriber);
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
     * remove the subscriber from the lobbyPublisher
     * notifies the other subscribers of the leaving of the unsubscriber
     * @param diffUnsubscriber the subscriber being removed
     */
    public void unsubscribe(DiffSubscriberLobby diffUnsubscriber) {
        subscribers.remove(diffUnsubscriber);
        LobbyDiffEdit others = createDiffUnsubscriber(diffUnsubscriber.getNickname());
        notifySubscribers(others);
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

    @Override
    public void notifySubscribers(ModelDiffs<LightLobby> diff) {
        for(DiffSubscriberLobby diffSubscriber : subscribers){
            diffSubscriber.updateLobby(diff);
        }
    }

    public void notifyStartGame(){
        for(DiffSubscriberLobby diffSubscriber : subscribers){
            diffSubscriber.gameStarted();
        }
    }

}

