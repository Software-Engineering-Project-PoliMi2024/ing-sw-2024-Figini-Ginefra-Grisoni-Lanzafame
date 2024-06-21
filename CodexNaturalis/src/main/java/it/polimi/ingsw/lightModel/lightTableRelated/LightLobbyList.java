package it.polimi.ingsw.lightModel.lightTableRelated;

import it.polimi.ingsw.utils.Observed;
import it.polimi.ingsw.utils.Observer;
import it.polimi.ingsw.lightModel.Differentiable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is a container for the list of lobbies.
 */
public class LightLobbyList implements Differentiable, Observed {
    private final List<Observer> observers = new LinkedList<>();
    private List<LightLobby> lobbies;

    /**
     * Creates a LightLobbyList object.
     */
    public LightLobbyList() {
        this.lobbies = new ArrayList<>();
    }

    public LightLobbyList(List<LightLobby> lobbies) {
        this.lobbies = new ArrayList<>(lobbies);
    }

    /**
     * Returns the list of lobbies.
     *
     * @return the list of lobbies
     */
    public synchronized List<LightLobby> getLobbies() {
        return lobbies;
    }

    /**
     * @param add the lobbies to add
     * @param rmv the lobbies to remove
     */
    public synchronized void lobbiesDiff(List<LightLobby> add, List<LightLobby> rmv) {
        List<LightLobby> lobbyModify = lobbies;
        for (LightLobby lightLobby : rmv) {
            lobbyModify.remove(lightLobby);
        }
        lobbyModify.addAll(add);
        notifyObservers();
    }

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
