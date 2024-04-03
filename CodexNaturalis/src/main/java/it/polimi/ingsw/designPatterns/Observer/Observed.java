package it.polimi.ingsw.designPatterns.Observer;

import java.util.function.Function;

public interface Observed {
    public void attach(Observer observer);
    public void detach(Observer observer);
    public void notifyObservers();
}
