package it.polimi.ingsw.view.TUI.observers;

import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public interface CommandObserved {
    public void attach(CommandObserver observer);
    public void detach(CommandObserver observer);
    public void notifyObservers();
}
