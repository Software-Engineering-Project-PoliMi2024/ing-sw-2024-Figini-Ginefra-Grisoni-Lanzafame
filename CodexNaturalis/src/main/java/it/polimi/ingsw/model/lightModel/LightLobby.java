package it.polimi.ingsw.model.lightModel;

import it.polimi.ingsw.model.lightModel.diffs.ModelDiff;
import it.polimi.ingsw.model.lightModel.diffs.ModelDifferentiable;

import java.util.List;

/**
 * This class is a container for the list of lobbies.
 */
public class LightLobby implements ModelDifferentiable<String> {
    private final List<String> nicknames;
    private final String name;

    /**
     * Creates a LightLobby object.
     * @param nicknames the list of nicknames
     * @param name the name of the lobby
     */
    public LightLobby(List<String> nicknames, String name) {
        this.nicknames = nicknames;
        this.name = name;
    }

    /**
     * Returns the list of nicknames.
     * @return the list of nicknames
     */
    public List<String> getNicknames() {
        return nicknames;
    }

    /**
     * Returns the name of the lobby.
     * @return the name of the lobby
     */
    public String getName() {
        return name;
    }

    /**
     * Applies the differences to the list of nicknames.
     * @param diff the differences to apply
     */
    @Override
    public void applyDiff(ModelDiff<String> diff) {
        nicknames.removeAll(diff.removeList());
        nicknames.addAll(diff.addList());
    }
}
