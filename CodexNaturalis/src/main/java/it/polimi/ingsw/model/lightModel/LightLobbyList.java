package it.polimi.ingsw.model.lightModel;

import it.polimi.ingsw.model.lightModel.diffs.LobbyListDiff;
import it.polimi.ingsw.model.lightModel.diffs.ModelDifferentiable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for the list of lobbies.
 */
public class LightLobbyList implements ModelDifferentiable<LobbyListDiff> {
    public void setLobbies(List<LightLobby> lobbies) {
        this.lobbies = lobbies;
    }

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
        diff.removedGame().forEach(lobbies::remove);
        lobbies.addAll(diff.addedGame());
    }
}
