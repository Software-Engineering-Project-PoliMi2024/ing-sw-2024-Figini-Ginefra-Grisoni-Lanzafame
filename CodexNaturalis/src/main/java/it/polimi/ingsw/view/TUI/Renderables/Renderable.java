package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;
import it.polimi.ingsw.view.TUI.observers.CommandObserver;
import it.polimi.ingsw.view.TUI.observers.InputObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class Renderable implements InputObserver, CommandObserver {
    private boolean active = false;

    private final CommandPrompt[] relatedCommands;

    public Renderable(CommandPrompt[] relatedCommands){
        this.relatedCommands = relatedCommands;
    }

    public abstract void render();

    public abstract void update();

    public abstract void updateInput(String input);

    public void setActive(boolean active){
        this.active = active;
    }

    public void updateCommand(CommandPromptResult command){
    }

    public boolean isActive(){
        return this.active;
    }
}
