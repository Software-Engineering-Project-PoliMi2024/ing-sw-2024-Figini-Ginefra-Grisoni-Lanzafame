package it.polimi.ingsw.lightModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for the list of lobbies.
 */
public record LightLobby(List<String> nicknames, String name) implements Differentiable {
    /**
     * Creates a LightLobby object.
     */
    public LightLobby (){
        this(new ArrayList<>(), "");
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
     * @param rmv the nicknames to remove
     * @param add the nicknames to add
     */
    public void nickDiff(List<String> add, List<String> rmv){
        nicknames.removeAll(rmv);
        nicknames.addAll(add);
    }
}
