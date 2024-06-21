package it.polimi.ingsw.utils;

public interface Observed {
    void attach(Observer observer);

    void detach(Observer observer);

    void notifyObservers();
}
