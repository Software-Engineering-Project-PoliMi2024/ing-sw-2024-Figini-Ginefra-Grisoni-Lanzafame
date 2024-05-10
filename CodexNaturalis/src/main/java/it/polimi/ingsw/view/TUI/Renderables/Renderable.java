package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.controller2.ControllerInterface;
import it.polimi.ingsw.view.ActualView;
import it.polimi.ingsw.view.ControllerProvider;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.CommandPromptResult;
import it.polimi.ingsw.view.TUI.observers.CommandObserver;
import it.polimi.ingsw.view.TUI.observers.InputObserver;

import java.io.Serializable;

/**
 * Abstract class that represents a renderable object that is an object that can be rendered on the screen and can handle inputs
 */
public abstract class Renderable implements InputObserver, CommandObserver, Serializable {
    private boolean active = false;
    private final CommandPrompt[] relatedCommands;

    protected final ControllerProvider view;
    private final String name;

    /**
     * Constructor
     * @param name the name of the renderable
     * @param relatedCommands the commands that are related to this renderable
     * @param view the controller provider
     */
    public Renderable(String name, CommandPrompt[] relatedCommands, ControllerProvider view){
        this.name = name;
        this.relatedCommands = relatedCommands;

        this.view = view;

        if(relatedCommands != null) {
            for (CommandPrompt command : relatedCommands) {
                command.attach(this);
            }
        }
    }

    /**
     * Method that renders the object on the screen
     */
    public abstract void render();

    /**
     * Method that updates the renderable on terminal input
     * @param input the input to update
     */
    public void updateInput(String input){
        throw new UnsupportedOperationException("Update without input not supported");
    };

    /**
     * Sets the renderable as active or not
     * @param active true if the renderable is active, false otherwise
     */
    public void setActive(boolean active){
        this.active = active;
    }

    /**
     * Method that updates the renderable on the result of an observed CommandPrompt
     * @param command the command to update
     */
    public void updateCommand(CommandPromptResult command){
    }

    /**
     * Method that returns if the renderable is active
     * @return true if the renderable is active, false otherwise
     */
    public boolean isActive(){
        return this.active;
    }

    /**
     * Method that returns the name of the renderable
     * @return the name of the renderable
     */
    public String getName(){
        return name;
    }

    /**
     * Method that returns the related commands of the renderable
     * @return the related commands of the renderable
     */
    public CommandPrompt[] getRelatedCommands(){
        return relatedCommands;
    }
}
