package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ConnectionLayerClient;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;
import it.polimi.ingsw.view.TUI.observers.CommandObserver;
import it.polimi.ingsw.view.TUI.observers.InputObserver;

public abstract class Renderable implements InputObserver, CommandObserver {
    private boolean active = false;

    private final CommandPrompt[] relatedCommands;

    protected final ConnectionLayerClient controller;

    private final String name;

    public Renderable(String name, CommandPrompt[] relatedCommands, ConnectionLayerClient controller){
        this.name = name;

        this.relatedCommands = relatedCommands;

        this.controller = controller;

        if(relatedCommands != null) {
            for (CommandPrompt command : relatedCommands) {
                command.attach(this);
            }
        }
    }

    public abstract void render();

    public void updateInput(String input){
        throw new UnsupportedOperationException("Update without input not supported");
    };

    public void setActive(boolean active){
        this.active = active;
    }

    public void updateCommand(CommandPromptResult command){
    }

    public boolean isActive(){
        return this.active;
    }

    public String getName(){
        return name;
    }
    public CommandPrompt[] getRelatedCommands(){
        return relatedCommands;
    }
}
