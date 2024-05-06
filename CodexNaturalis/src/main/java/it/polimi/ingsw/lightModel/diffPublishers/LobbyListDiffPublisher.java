package it.polimi.ingsw.lightModel.diffPublishers;

import it.polimi.ingsw.lightModel.Lightifier;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiffEdit;
import it.polimi.ingsw.lightModel.diffs.nuclearDiffs.FatManLobbyList;
import it.polimi.ingsw.lightModel.diffs.lobby_lobbyList.LobbyListDiff;
import it.polimi.ingsw.model.tableReleted.LobbyList;

import java.util.ArrayList;
import java.util.List;

public class LobbyListDiffPublisher {
    private final List<DiffSubscriber> diffSubscribers;
    private final LobbyList lobbyList;

    /**
     * Constructor of the LobbyListDiffPublisher
     * @param lobbyList the lobbyList from which read the data related to the lobbies
     */
    public LobbyListDiffPublisher(LobbyList lobbyList) {
        this.diffSubscribers = new ArrayList<>();
        this.lobbyList = lobbyList;
    }

    /**
     * Subscribe a new subscriber to the lobbyList
     * the subscriber will receive the current list of active lobbies
     * @param diffSubscriber the subscriber to the lobbyList
     */
    public synchronized void subscribe(DiffSubscriber diffSubscriber) {
        diffSubscribers.add(diffSubscriber);
        this.notifySubscriber(diffSubscriber, new LobbyListDiffEdit(new ArrayList<>(lobbyList.getLobbies().stream().map(Lightifier::lightify).toList()),new ArrayList<>()));
    }

    /**
     * Unsubscribe a subscriber from the lobbyList
     * the unsubscribed subscriber will receive a diff to reset its local state
     * the unsubscribed subscriber will stop receiving updates from the lobbyList
     * @param diffSubscriber
     */
    public synchronized void unsubscribe(DiffSubscriber diffSubscriber) {
        diffSubscribers.remove(diffSubscriber);
        notifySubscriber(diffSubscriber, new FatManLobbyList());
    }

    /**
     * Notify all the subscribers of the lobbyList about the new diff
     * the diff contains information about the changes in the lobbyList (added lobby, removed lobby)
     * @param lobbyListDiff the diff to be sent to the subscribers
     */
    public synchronized void subscribe(LobbyListDiffEdit lobbyListDiff) {
        for(DiffSubscriber diffSubscriber : diffSubscribers){
            this.notifySubscriber(diffSubscriber, lobbyListDiff);
        }
    }

    /**
     * Notify a single subscriber of the lobbyList about the new diff
     * @param diffSubscriber the subscriber to be notified
     * @param lobbyListDiff the diff to be sent to the subscriber
     */
    public synchronized void notifySubscriber(DiffSubscriber diffSubscriber, LobbyListDiff lobbyListDiff) {
        diffSubscriber.updateLobbyList(lobbyListDiff);
    }
}
