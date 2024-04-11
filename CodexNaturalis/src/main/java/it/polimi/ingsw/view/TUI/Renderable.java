package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.view.TUI.observers.InputObserver;

public abstract class Renderable implements InputObserver {
    private boolean active = false;

    public abstract void render();

    public abstract void update();

    public abstract void updateInput(String input);

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean isActive(){
        return this.active;
    }
}
