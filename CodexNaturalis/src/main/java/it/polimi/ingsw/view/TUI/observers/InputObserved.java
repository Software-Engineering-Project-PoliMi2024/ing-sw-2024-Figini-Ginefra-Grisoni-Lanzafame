package it.polimi.ingsw.view.TUI.observers;

/**
 * This interface represents an object that can be observed by InputObserver. That is objects that expect the string inputted by the user as context.
 */
public interface InputObserved {
    public interface ServerMsgObserved {
        public void attach(InputObserver observer);
        public void detach(InputObserver observer);
        public void notifyObservers(String input);
    }
}
