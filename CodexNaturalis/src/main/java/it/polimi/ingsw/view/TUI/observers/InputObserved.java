package it.polimi.ingsw.view.TUI.observers;

/**
 * This interface represents an object that can be observed by InputObserver. That is objects that expect the string inputted by the user as context.
 */
public interface InputObserved {
    void attach(InputObserver observer);
    void detach(InputObserver observer);
    void notifyObservers(String input);
}
