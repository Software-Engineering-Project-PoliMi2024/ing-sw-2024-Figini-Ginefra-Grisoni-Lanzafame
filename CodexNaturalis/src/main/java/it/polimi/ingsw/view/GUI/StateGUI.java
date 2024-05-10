package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.ViewState;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public enum StateGUI {
    SERVER_CONNECTION(ViewState.SERVER_CONNECTION, Root.SERVER_CONNECTION_FORM),
    LOGIN_FORM(ViewState.LOGIN_FORM, Root.LOGIN_FORM),;

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
