package it.polimi.ingsw.model.lightModel;

import it.polimi.ingsw.model.lightModel.diffs.LobbyDiff;
import it.polimi.ingsw.model.lightModel.diffs.ModelDifferentiable;

import java.util.List;

/**
 * This class is a container for the list of lobbies.
 */
public record LightLobby(List<String> nicknames, String name) implements ModelDifferentiable<LobbyDiff> {
    /**
     * Creates a LightLobby object.
     *
     * @param nicknames the list of nicknames
     * @param name      the name of the lobby
     */
    public LightLobby {
    }
    /**
     * @return the list of nicknames
     */
    @Override
    public List<String> nicknames() {
        return nicknames;
    }
    /**
     * @return the name of the lobby
     */
    @Override
    public String name() {
        return name;
    }
    /**
     * Applies the differences to the list of nicknames.
     * @param diff the differences to apply
     */
    @Override
    public void applyDiff(LobbyDiff diff) {
        diff.apply(this);
    }
    /**
     * @param rmv the nicknames to remove
     * @param add the nicknames to add
     */
    public void nickDiff(List<String> rmv, List<String> add){
        nicknames.removeAll(rmv);
        nicknames.addAll(add);
    }
}
