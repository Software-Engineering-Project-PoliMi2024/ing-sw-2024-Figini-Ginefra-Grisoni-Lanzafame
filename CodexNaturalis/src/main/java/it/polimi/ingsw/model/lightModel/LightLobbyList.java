package it.polimi.ingsw.model.lightModel;

import it.polimi.ingsw.model.lightModel.diffs.LobbyListDiff;
import it.polimi.ingsw.model.lightModel.diffs.ModelDifferentiable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for the list of lobbies.
 */
public class LightLobbyList implements ModelDifferentiable<LobbyListDiff> {
    private List<LightLobby> lobbies;
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
     * Applies the differences to the list of lobbies.
     * @param diff the differences to apply
     */
    @Override
    public void applyDiff(LobbyListDiff diff) {
        diff.apply(this);
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
