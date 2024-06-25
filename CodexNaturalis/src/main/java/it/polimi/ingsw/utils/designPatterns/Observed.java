package it.polimi.ingsw.utils.designPatterns;

public interface Observed {
    /**
     * attach an observer to the observed class
     * @param observer the observer to attach
     */
    void attach(Observer observer);

    /**
     * detach an observer from the observed class
     * @param observer the observer to detach
     */
    void detach(Observer observer);

    /**
     * notify all the observers attached to the observed class
     */
    void notifyObservers();
}
