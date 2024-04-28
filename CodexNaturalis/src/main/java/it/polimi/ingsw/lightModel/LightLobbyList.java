package it.polimi.ingsw.lightModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for the list of lobbies.
 */
public class LightLobbyList implements Differentiable{
    private final List<LightLobby> lobbies;
    /**
     * Creates a LightLobbyList object.
     */
    public LightLobbyList() {
        this.lobbies = new ArrayList<>();
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
        rmv.forEach(lobbies::remove);
        lobbies.addAll(add);
    }
}
