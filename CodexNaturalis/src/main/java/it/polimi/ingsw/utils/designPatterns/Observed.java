package it.polimi.ingsw.utils.designPatterns;

public interface Observed {
    void attach(Observer observer);

    void detach(Observer observer);

    void notifyObservers();
}
