package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.lightModel.Differentiable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for the list of lobbies.
 */
public class LightLobby implements Differentiable {
    private List<String> nicknames;
    private String name;
    /**
     * Creates a LightLobby object.
     */
    public LightLobby (){
        nicknames=  new ArrayList<>();
        name = "";
    }
    public LightLobby(List<String> nicknames, String name){
        this.nicknames = nicknames;
        this.name = name;
    }
    /**
     * @return the list of nicknames
     */
    public List<String> nicknames() {
        return nicknames;
    }
    /**
     * @return the name of the lobby
     */
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

    public void setName(String name) {
        this.name = name;
    }

    public void setNicknames(List<String> nicknames) {
        this.nicknames = nicknames;
    }
}
