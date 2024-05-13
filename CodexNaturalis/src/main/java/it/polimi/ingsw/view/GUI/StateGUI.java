package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.ViewState;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public enum StateGUI {
    SERVER_CONNECTION(ViewState.SERVER_CONNECTION, Root.SERVER_CONNECTION_FORM),
    LOGIN_FORM(ViewState.LOGIN_FORM, Root.LOGIN_FORM),
    JOIN_LOBBY(ViewState.JOIN_LOBBY, Root.LOBBY_LIST),
    LOBBY(ViewState.LOBBY, Root.LOBBY),

    CHOOSE_START_CARD(ViewState.CHOOSE_START_CARD, Root.GAME),

    SELECT_OBJECTIVE(ViewState.SELECT_OBJECTIVE, Root.GAME),

    WAITING_STATE(ViewState.WAITING_STATE, Root.GAME),

    IDLE(ViewState.IDLE, Root.GAME),

    DRAW_CARD(ViewState.DRAW_CARD, Root.GAME),

    PLACE_CARD(ViewState.PLACE_CARD, Root.GAME),

    GAME_WAITING(ViewState.GAME_WAITING, Root.GAME),

    GAME_ENDING(ViewState.GAME_ENDING, Root.GAME),;

    private final ViewState referenceState;
    private final Root referenceRoot;

    StateGUI(ViewState referenceState, Root referenceRoot) {
        this.referenceState = referenceState;
        this.referenceRoot = referenceRoot;
    }

    public Root getRoot() {
        return referenceRoot;
    }

    public boolean references(ViewState state) {
        return referenceState == state;
    }
}
