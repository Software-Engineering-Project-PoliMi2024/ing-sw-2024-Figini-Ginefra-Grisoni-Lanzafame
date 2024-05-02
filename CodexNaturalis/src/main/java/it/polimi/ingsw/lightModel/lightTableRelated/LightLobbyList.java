package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.lightTableRelated.LightLobby;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for the list of lobbies.
 */
public class LightLobbyList implements Differentiable {
    private List<LightLobby> lobbies;
    /**
     * Creates a LightLobbyList object.
     */
    public LightLobbyList() {
        this.lobbies = new ArrayList<>();
    }
    public LightLobbyList(List<LightLobby> lobbies){
        this.lobbies = lobbies;
    }
    /**
     * Returns the list of lobbies.
     * @return the list of lobbies
     */
    public List<LightLobby> getLobbies() {
        return lobbies;
    }
    /**
     * @param add the lobbies to add
     * @param rmv the lobbies to remove
     */
    public void lobbiesDiff(List<LightLobby> add, List<LightLobby> rmv){
        ArrayList<LightLobby> lobbyModify = new ArrayList<>(lobbies);
        for(LightLobby lightLobby : rmv){
            lobbyModify.remove(lightLobby);
        }
        lobbyModify.addAll(add);
        lobbies = lobbyModify;
    }

    public void setLobbies(List<LightLobby> lobbies) {
        this.lobbies = lobbies;
    }
}
