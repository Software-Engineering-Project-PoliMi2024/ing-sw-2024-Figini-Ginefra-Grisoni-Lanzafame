package it.polimi.ingsw.view.TUI.States;

import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.ViewState;

import java.util.ArrayList;
import java.util.List;

public enum StateTUI {
    STATE0(ViewState.SERVER_CONNECTION), STATE1(null);

    private final ViewState referenceState;
    private final List<Renderable> targetRenderables;

    StateTUI(ViewState referenceState) {
        this.referenceState = referenceState;
        this.targetRenderables = new ArrayList<>();
    }

    public void attach(Renderable renderable) {
        targetRenderables.add(renderable);
    }

    public List<Renderable> getRenderables() {
        return targetRenderables;
    }

    public ViewState getReferenceState() {
        return referenceState;
    }

    public boolean references(ViewState state) {
        return referenceState == state;
    }
}
