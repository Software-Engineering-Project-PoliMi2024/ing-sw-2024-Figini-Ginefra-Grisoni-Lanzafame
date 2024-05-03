package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.lightModel.Differentiable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a container for the list of lobbies.
 */
public class LightLobby implements Differentiable {
    private int numberMaxPlayer = 0;
    private List<String> nicknames;
    private String name;
    /**
     * Creates a LightLobby object.
     */
    public LightLobby (){
        nicknames=  new ArrayList<>();
        name = "";
    }
    public LightLobby(List<String> nicknames, String name, int numberMaxPlayer){
        this.nicknames = nicknames;
        this.name = name;
        this.numberMaxPlayer = numberMaxPlayer;
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

    /**
     * @param name of the Lobby
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param nicknames List containing all users' nickname in the lobby
     */
    public void setNicknames(List<String> nicknames) {
        this.nicknames = nicknames;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LightLobby lightLobby){
            return lightLobby.name.equals(name);
        }
        return false;
    }
    public void setNumberMaxPlayer(int numberMaxPlayer) {
        this.numberMaxPlayer = numberMaxPlayer;
    }

    public int numberMaxPlayer(){
        return this.numberMaxPlayer;
    }
    public void reset(){
        this.setName(null);
        this.setNicknames(new ArrayList<>());
        this.setNumberMaxPlayer(0);
    }
}
