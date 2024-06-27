package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.GUI.Scenes.*;
import it.polimi.ingsw.view.ViewState;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

/**
 * The enum that contains all the possible states of the GUI.
 * Each state is associated with a scene that will be displayed when the state is active.
 */
public enum StateGUI {
    SERVER_CONNECTION(ViewState.SERVER_CONNECTION, ServerConnectionScene.class),
    LOGIN_FORM(ViewState.LOGIN_FORM, LoginFormScene.class),
    JOIN_LOBBY(ViewState.JOIN_LOBBY, LobbyListScene.class),
    LOBBY(ViewState.LOBBY, LobbyScene.class),
    CHOOSE_PAWN(ViewState.CHOOSE_PAWN, GameScene.class),
    CHOOSE_START_CARD(ViewState.CHOOSE_START_CARD, GameScene.class),
    SELECT_OBJECTIVE(ViewState.SELECT_OBJECTIVE, GameScene.class),
    WAITING_STATE(ViewState.WAITING_STATE, GameScene.class),
    IDLE(ViewState.IDLE, GameScene.class),

    DRAW_CARD(ViewState.DRAW_CARD, GameScene.class),

    PLACE_CARD(ViewState.PLACE_CARD, GameScene.class),
    GAME_ENDING(ViewState.GAME_ENDING, GameScene.class),;

    /** The state that this enum represents */
    private final ViewState referenceState;

    /** The class of the scene that will be displayed when this state is active */
    private final Class<? extends SceneGUI> sceneClass;

    /** The constructor of the enum
     * @param referenceState the state that this enum represents
     * @param sceneClass the class of the scene that will be displayed when this state is active
     * */
    StateGUI(ViewState referenceState, Class<? extends SceneGUI> sceneClass) {
        this.referenceState = referenceState;
        this.sceneClass = sceneClass;
    }

    /**
     * Whether the given state is the state that this enum represents
     * @param state the state to check
     * @return whether the given state is the state that this enum represents
     */
    public boolean references(ViewState state) {
        return referenceState == state;
    }

    /**
     * Instantiates a new scene associated with this state
     * @return the new scene
     */
    public SceneGUI getScene() {
        try {
            return sceneClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Whether the given stateGUI refers to the same state as this stateGUI
     * @param other the stateGUI to compare
     * @return whether the given stateGUI refers to the same state as this stateGUI
     */
    public boolean equals(StateGUI other){
        return other != null && this.sceneClass.equals(other.sceneClass);
    }
}
