package it.polimi.ingsw.model.lightModel;

import it.polimi.ingsw.model.lightModel.diffs.ModelDiff;
import it.polimi.ingsw.model.lightModel.diffs.ModelDifferentiable;

import java.util.List;

/**
 * This class is a container for the list of lobbies.
 */
public class LightGameList implements ModelDifferentiable<LightLobby> {
    private final List<LightLobby> lobbies;

    /**
     * Creates a LightGameList object.
     * @param lobbies the list of lobbies
     */
    public LightGameList(List<LightLobby> lobbies) {
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
     * Applies the differences to the list of lobbies.
     * @param diff the differences to apply
     */
    @Override
    public void applyDiff(ModelDiff<LightLobby> diff) {
        diff.removeList().forEach(lobbies::remove);
        lobbies.addAll(diff.addList());
    }
}
