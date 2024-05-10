package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.view.ViewState;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.Objects;

public enum StateGUI {
    SERVER_CONNECTION(ViewState.SERVER_CONNECTION, "/GUI/ServerConnection.fxml"),
    LOGIN_FORM(ViewState.LOGIN_FORM, "/GUI/LoginForm.fxml"),;

    private final ViewState referenceState;
    private final Parent root;

    private final Scene scene;

    StateGUI(ViewState referenceState, String fxmlPath) {
        this.referenceState = referenceState;

        try {
            this.root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.scene = new Scene(root, 400, 400);
    }

    public Parent getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }

    public boolean references(ViewState state) {
        return referenceState == state;
    }
}
