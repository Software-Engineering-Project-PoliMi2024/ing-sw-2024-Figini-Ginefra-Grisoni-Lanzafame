package it.polimi.ingsw.view.TUI.observers;

/**
 * This interface represents an object that can be observed by CommandObservers. That is objects that expect the string inputted by the user as context.
 */
public interface InputObserver {
    public void updateInput(String input);
}
