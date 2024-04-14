package it.polimi.ingsw.view.TUI.Renderables;

import it.polimi.ingsw.view.TUI.observers.InputObserver;

import java.util.ArrayList;
import java.util.List;

public abstract class Renderable implements InputObserver {
    private boolean active = false;

    public Renderable(){
    }

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
