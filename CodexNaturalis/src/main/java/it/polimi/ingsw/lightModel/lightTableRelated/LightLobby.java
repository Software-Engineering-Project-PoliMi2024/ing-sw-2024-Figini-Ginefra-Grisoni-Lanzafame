package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.designPatterns.Observed;
import it.polimi.ingsw.designPatterns.Observer;
import it.polimi.ingsw.lightModel.Differentiable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is a container for the list of lobbies.
 */
public class LightLobby implements Differentiable, Observed {
    private final List<Observer> observers = new LinkedList<>();
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

    /**
     *
     * @param lobby the LightLobby to copy
     */
    public LightLobby(LightLobby lobby){
        this.nicknames = new ArrayList<>(lobby.nicknames);
        this.name = lobby.name;
        this.numberMaxPlayer = lobby.numberMaxPlayer;
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
        notifyObservers();
        nicknames.removeAll(rmv);
        nicknames.addAll(add);
    }

    /**
     * @param name of the Lobby
     */
    public void setName(String name) {
        notifyObservers();
        this.name = name;
    }

    /**
     * @param nicknames List containing all users' nickname in the lobby
     */
    public void setNicknames(List<String> nicknames) {
        notifyObservers();
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
        notifyObservers();
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
