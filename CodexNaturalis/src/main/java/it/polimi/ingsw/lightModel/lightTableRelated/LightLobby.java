package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.utils.designPatterns.Observed;
import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.Differentiable;
import it.polimi.ingsw.lightModel.LightModelConfig;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the lobby in the View.
 * It holds the list of nicknames of the players in the lobby, the name of the lobby and the number of players needed to start the game.
 */
public class LightLobby implements Differentiable, Observed {
    /** The list of observers */
    private final List<Observer> observers = new LinkedList<>();
    /** The number of players in the lobby needed to start a game*/
    private int numberMaxPlayer = LightModelConfig.defaultNumberMaxPlayer;
    /** The list of nicknames of the players in the lobby */
    private List<String> nicknames = new ArrayList<>();
    /** The name of the lobby that will become the name of the game*/
    private String name = LightModelConfig.defaultLobbyName;

    /**
     * Creates a LightLobby object.
     */
    public LightLobby (){
    }

    /**
     * Create a lightLobby object as a copy of another lightLobby object
     * by copying the name, the number of max players and all of the nicknames
     * @param lobby the LightLobby to copy
     */
    public LightLobby(LightLobby lobby){
        this.nicknames = new ArrayList<>(lobby.nicknames);
        this.name = lobby.name;
        this.numberMaxPlayer = lobby.numberMaxPlayer;
    }

    /**
     * Createa a new lightLobby object with the given parameters
     * @param nicknames the list of nicknames in the lobby
     * @param name name of the lobby and later of the game
     * @param numberMaxPlayer the number of players needed to start the game
     */
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
     * Edit the list of nicknames in the lobby
     * notify the observers after the update
     * @param rmv the list of nicknames to remove
     * @param add the list of nicknames to add
     */
    public void nickDiff(List<String> add, List<String> rmv){
        nicknames.removeAll(rmv);
        nicknames.addAll(add);
        notifyObservers();
    }

    /**
     * Set the name of the lobby
     * Notify the player after the update
     * @param name of the Lobby
     */
    public void setName(String name) {
        this.name = name;
        notifyObservers();
    }

    /**
     * Define that two lobby are equals if they have the same name
     * @param obj the object to compare
     * @return true if the object is a LightLobby and has the same name
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LightLobby lightLobby){
            return lightLobby.name.equals(name);
        }
        return false;
    }

    /**
     * Set the number of the players in the lobby needed to start the game
     * Notify the observers after the update
     * @param numberMaxPlayer the number of players needed to start the game
     */
    public void setNumberMaxPlayer(int numberMaxPlayer) {
        this.numberMaxPlayer = numberMaxPlayer;
        notifyObservers();
    }

    /**
     * @return the number of players in the lobby needed to start the game
     */
    public int numberMaxPlayer(){
        return this.numberMaxPlayer;
    }

    /**
     * Reset the lobby to the default values
     * Remove all the nicknames and set the name to the default name
     * Notify the observers after the update
     */
    public void reset(){
        this.setName(LightModelConfig.defaultLobbyName);
        this.nicknames = new ArrayList<>();
        this.setNumberMaxPlayer(LightModelConfig.defaultNumberMaxPlayer);
        notifyObservers();
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observers){
            observer.update();
        }
    }
}
