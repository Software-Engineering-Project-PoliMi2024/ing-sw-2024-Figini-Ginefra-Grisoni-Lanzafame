package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.GUI.Scenes.*;
import it.polimi.ingsw.view.ViewState;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

public enum StateGUI {
    SERVER_CONNECTION(ViewState.SERVER_CONNECTION, ServerConnectionScene::new),
    LOGIN_FORM(ViewState.LOGIN_FORM, LoginFormScene::new),
    JOIN_LOBBY(ViewState.JOIN_LOBBY, LobbyListScene::new),
    LOBBY(ViewState.LOBBY, LobbyScene::new),
    CHOOSE_START_CARD(ViewState.CHOOSE_START_CARD, GameScene::new),
    SELECT_OBJECTIVE(ViewState.SELECT_OBJECTIVE, GameScene::new),
    WAITING_STATE(ViewState.WAITING_STATE, GameScene::new),
    IDLE(ViewState.IDLE, GameScene::new),

    DRAW_CARD(ViewState.DRAW_CARD, GameScene::new),

    PLACE_CARD(ViewState.PLACE_CARD, GameScene::new),

    GAME_WAITING(ViewState.GAME_WAITING, GameScene::new),

    GAME_ENDING(ViewState.GAME_ENDING, GameScene::new),;

    private final ViewState referenceState;

    private final Supplier<SceneGUI> sceneConstruvtor;


    StateGUI(ViewState referenceState, Supplier<SceneGUI> sceneConstruvtor) {
        this.referenceState = referenceState;
        this.sceneConstruvtor = sceneConstruvtor;
    }

    public boolean references(ViewState state) {
        return referenceState == state;
    }

    public SceneGUI getScene() {
        return sceneConstruvtor.get();
    }

    public boolean equals(StateGUI other){
        return other != null && this.sceneConstruvtor == other.sceneConstruvtor;
    }
}
