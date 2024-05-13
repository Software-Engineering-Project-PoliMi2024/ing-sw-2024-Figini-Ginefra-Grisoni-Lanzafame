package it.polimi.ingsw.designPatterns;

public interface Observed {
    void attach(Observer observer);

    void detach(Observer observer);

    void notifyObservers();
}
