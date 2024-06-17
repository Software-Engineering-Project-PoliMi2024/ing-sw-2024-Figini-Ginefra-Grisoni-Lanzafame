package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.GUI.Scenes.*;
import it.polimi.ingsw.view.ViewState;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

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

    GAME_WAITING(ViewState.GAME_WAITING, GameScene.class),

    GAME_ENDING(ViewState.GAME_ENDING, PostGameScene.class),;

    private final ViewState referenceState;

    private final Class<? extends SceneGUI> sceneClass;


    StateGUI(ViewState referenceState, Class<? extends SceneGUI> sceneClass) {
        this.referenceState = referenceState;
        this.sceneClass = sceneClass;
    }

    public boolean references(ViewState state) {
        return referenceState == state;
    }

    public SceneGUI getScene() {
        try {
            return sceneClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean equals(StateGUI other){
        return other != null && this.sceneClass.equals(other.sceneClass);
    }
}
