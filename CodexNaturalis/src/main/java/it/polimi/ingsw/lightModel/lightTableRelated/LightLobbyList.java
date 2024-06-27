package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.utils.designPatterns.Observed;
import it.polimi.ingsw.utils.designPatterns.Observer;
import it.polimi.ingsw.lightModel.Differentiable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is a container for the list of lobbies.
 */
public class LightLobbyList implements Differentiable, Observed {
    /** The list of observers */
    private final List<Observer> observers = new LinkedList<>();
    /** The list of lobbies available */
    private List<LightLobby> lobbies;

    /**
     * Creates a LightLobbyList object.
     */
    public LightLobbyList() {
        this.lobbies = new ArrayList<>();
    }

    /**
     * Creates a LightLobbyList object and populate it with the given list of lobbies.
     * @param lobbies the list of lobbies to put in the LightLobbyList
     */
    public LightLobbyList(List<LightLobby> lobbies) {
        this.lobbies = new ArrayList<>(lobbies);
    }

    /**
     * Returns the list of lobbies.
     * @return the list of lobbies
     */
    public synchronized List<LightLobby> getLobbies() {
        return lobbies;
    }

    /**
     * Update list of lobbies with the given lists of lobbies to add and remove.
     * Notifies the observers at the end of the update
     * @param add the list of lobbies to add
     * @param rmv the list of lobbies to remove
     */
    public synchronized void lobbiesDiff(List<LightLobby> add, List<LightLobby> rmv) {
        List<LightLobby> lobbyModify = lobbies;
        for (LightLobby lightLobby : rmv) {
            lobbyModify.remove(lightLobby);
        }
        lobbyModify.addAll(add);
        notifyObservers();
    }

    /**
     * Set the list of lobbies.
     * Notifies the observers at the end of the update
     * @param lobbies
     */
    public synchronized void setLobbies(List<LightLobby> lobbies) {
        this.lobbies = lobbies;
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
        observers.forEach(Observer::update);
    }
}
