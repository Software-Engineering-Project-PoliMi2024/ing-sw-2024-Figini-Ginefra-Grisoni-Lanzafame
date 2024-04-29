package it.polimi.ingsw.view.TUI.States;

import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.ViewState;

import java.util.ArrayList;
import java.util.List;

public enum StateTUI {
    SERVER_CONNECTION(ViewState.SERVER_CONNECTION),
    LOGIN_FORM(ViewState.LOGIN_FORM),
    JOIN_LOBBY(ViewState.JOIN_LOBBY),
    LOBBY(ViewState.LOBBY),
    CHOOSE_START_CARD(ViewState.CHOOSE_START_CARD),
    SELECT_OBJECTIVE(ViewState.SELECT_OBJECTIVE),
    IDLE(ViewState.IDLE),
    DRAW_CARD(ViewState.DRAW_CARD),
    PLACE_CARD(ViewState.PLACE_CARD),
    GAME_ENDING(ViewState.GAME_ENDING);

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
