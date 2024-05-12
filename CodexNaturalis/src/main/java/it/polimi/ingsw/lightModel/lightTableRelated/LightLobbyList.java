package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for the list of lobbies.
 */
public class LightLobbyList implements Differentiable {
    private transient ObservableList<LightLobby> lobbies;
    /**
     * Creates a LightLobbyList object.
     */
    public LightLobbyList() {
        this.lobbies = FXCollections.observableArrayList();
    }
    public LightLobbyList(List<LightLobby> lobbies){
        ObservableList<LightLobby> newList = FXCollections.observableArrayList();
        newList.addAll(lobbies);
        this.lobbies = newList;
    }
    /**
     * Returns the list of lobbies.
     * @return the list of lobbies
     */
    public synchronized List<LightLobby> getLobbies() {
        return lobbies;
    }
    /**
     * @param add the lobbies to add
     * @param rmv the lobbies to remove
     */
    public synchronized void lobbiesDiff(List<LightLobby> add, List<LightLobby> rmv){
        List<LightLobby> lobbyModify = FXCollections.observableList(lobbies);
        for(LightLobby lightLobby : rmv){
            lobbyModify.remove(lightLobby);
        }
        lobbyModify.addAll(add);
    }

    public synchronized void setLobbies(List<LightLobby> lobbies) {
        ObservableList<LightLobby> newList = FXCollections.observableArrayList();
        newList.addAll(lobbies);
        this.lobbies = newList;
    }
    public ObservableList<LightLobby> bind(){
        return lobbies;
    }
}
