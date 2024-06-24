package it.polimi.ingsw.view.TUI.States;

import it.polimi.ingsw.model.playerReleted.PlayerState;
import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.ViewState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public enum StateTUI {
    SERVER_CONNECTION(ViewState.SERVER_CONNECTION),
    LOGIN_FORM(ViewState.LOGIN_FORM),
    JOIN_LOBBY(ViewState.JOIN_LOBBY),
    LOBBY(ViewState.LOBBY),
    CHOOSE_START_CARD(ViewState.CHOOSE_START_CARD),
    SELECT_OBJECTIVE(ViewState.SELECT_OBJECTIVE),
    WAITING_STATE(ViewState.WAITING_STATE),
    IDLE(ViewState.IDLE),
    DRAW_CARD(ViewState.DRAW_CARD),
    PLACE_CARD(ViewState.PLACE_CARD),
    GAME_ENDING(ViewState.GAME_ENDING),
    CHOOSE_PAWN(ViewState.CHOOSE_PAWN);

    private final ViewState referenceState;
    private final List<Renderable> targetRenderables;

    /**
     * The list of prompts that will be triggered at the start of the state.
     */
    private final List<CommandPrompt> startupPrompts = new LinkedList<>();

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

    public void addStartupPrompt(CommandPrompt prompt) {
        if(!prompt.isLocal())
            throw new IllegalArgumentException("Only local prompts can be added to the startup prompts of a state. " + prompt.getCommandName() + " is not local.");
        startupPrompts.add(prompt);
    }

    public void addAllStartupPrompts(List<CommandPrompt> prompts) {
        for(CommandPrompt prompt : prompts)
            if(!prompt.isLocal())
                throw new IllegalArgumentException("Only local prompts can be added to the startup prompts of a state. " + prompt.getCommandName() + " is not local.");

        startupPrompts.addAll(prompts);
    }

    /**
     * This methods makes all the startup prompts of the state notify their observers.
     */
    public void triggerStartupPrompts() {
        for (CommandPrompt prompt : startupPrompts) {
            prompt.notifyObservers();
        }
    }
}
