package it.polimi.ingsw.view.TUI.States;

import it.polimi.ingsw.view.TUI.Renderables.Renderable;

import java.util.ArrayList;
import java.util.List;

public enum StateTUI {
    STATE0, STATE1;

    private final List<Renderable> targetRenderables;

    StateTUI() {
        this.targetRenderables = new ArrayList<>();
    }

    public void attach(Renderable renderable) {
        targetRenderables.add(renderable);
    }

    public List<Renderable> getRenderables() {
        return targetRenderables;
    }
}
