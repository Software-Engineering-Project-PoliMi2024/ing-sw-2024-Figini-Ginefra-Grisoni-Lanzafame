package it.polimi.ingsw.view.TUI.observers;

/**
 * This interface represents an object that can be observed by CommandObservers. That is objects that expect a CommandPromptResult as context.
 */
public interface CommandObserved {
    public void attach(CommandObserver observer);
    public void detach(CommandObserver observer);
    public void notifyObservers();
}
