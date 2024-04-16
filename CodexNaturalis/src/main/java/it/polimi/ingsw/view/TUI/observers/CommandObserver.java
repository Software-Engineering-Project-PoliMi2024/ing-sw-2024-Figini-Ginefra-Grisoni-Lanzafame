package it.polimi.ingsw.view.TUI.observers;

import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

public interface CommandObserver {
    public void updateCommand(CommandPromptResult input);

}
