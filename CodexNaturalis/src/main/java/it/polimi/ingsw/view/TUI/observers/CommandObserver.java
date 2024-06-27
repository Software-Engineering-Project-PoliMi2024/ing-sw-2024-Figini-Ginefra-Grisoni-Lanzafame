package it.polimi.ingsw.view.TUI.observers;

import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;

/**
 * This interface represents an object that can observe a CommandObserved, that provides a Command Prompt Result as context.
 */
public interface CommandObserver {
    void updateCommand(CommandPromptResult input);

}
