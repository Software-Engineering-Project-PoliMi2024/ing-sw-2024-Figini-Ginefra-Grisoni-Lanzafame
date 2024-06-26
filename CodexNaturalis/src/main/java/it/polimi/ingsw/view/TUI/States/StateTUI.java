package it.polimi.ingsw.view.TUI.States;

import it.polimi.ingsw.view.TUI.Renderables.Renderable;
import it.polimi.ingsw.view.TUI.inputs.CommandPrompt;
import it.polimi.ingsw.view.TUI.inputs.StartUpPrompt;
import it.polimi.ingsw.view.ViewState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This enum represents the states of the TUI.
 * Each state has a reference to a ViewState and a list of attached Renderables that will be displayed when the state is active.
 */
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

    /** The ViewState that this state references.*/
    private final ViewState referenceState;

    /** The list of renderables that will be displayed when the state is active. */
    private final List<Renderable> targetRenderables;

    /** The list of prompts that will be triggered at the start of the state.*/
    private final List<StartUpPrompt> startupPrompts = new LinkedList<>();

    /**
     * Creates a new StateTUI.
     * @param referenceState The ViewState that this state references.
     */
    StateTUI(ViewState referenceState) {
        this.referenceState = referenceState;
        this.targetRenderables = new ArrayList<>();
    }

    /**
     * Attaches a renderable to the state.
     * @param renderable The renderable to attach.
     */
    public void attach(Renderable renderable) {
        targetRenderables.add(renderable);
    }

    /**
     * Gets the renderables attached to the state.
     * @return The renderables attached to the state.
     */
    public List<Renderable> getRenderables() {
        return targetRenderables;
    }

    /**
     * Returns whether the state references the given ViewState.
     * @param state The ViewState to check.
     * @return True if the state references the given ViewState, false otherwise.
     */
    public boolean references(ViewState state) {
        return referenceState == state;
    }

    /**
     * Adds a startup prompt to the state.
     * @param prompt The prompt to add.
     */
    public void addStartupPrompt(StartUpPrompt prompt) {
        if(!prompt.getCommand().isLocal())
            throw new IllegalArgumentException("Only local prompts can be added to the startup prompts of a state. " + prompt.getCommand().getCommandName() + " is not local.");
        startupPrompts.add(prompt);
    }

    /**
     * Adds a startup prompt to the state. The prompt will be created from the given CommandPrompt and it will not provide any answers. This is meant for the prompts that are not meant to be answered by the user.
     * @param prompt The prompt to add.
     */
    public void addStartupPrompt(CommandPrompt prompt) {
        addStartupPrompt(new StartUpPrompt(prompt));
    }

    /**
     * This methods makes all the startup prompts of the state notify their observers.
     */
    public void triggerStartupPrompts() {
        for (StartUpPrompt prompt : startupPrompts) {
            prompt.trigger();
        }
    }

    /**
     * This method checks if the given prompt is a startup prompt of the state.
     * @param prompt The prompt to check.
     * @return True if the prompt is a startup prompt of the state, false otherwise.
     */
    public boolean isStartupPrompt(CommandPrompt prompt) {
        return startupPrompts.stream().anyMatch(p -> p.getCommand().equals(prompt));
    }
}
