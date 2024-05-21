package it.polimi.ingsw.view.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public enum Root {
    SERVER_CONNECTION_FORM("/GUI/ServerConnection.fxml"),
    LOGIN_FORM("/GUI/LoginForm.fxml"),
    LOBBY_LIST("/GUI/LobbyList.fxml"),
    LOBBY("/GUI/Lobby.fxml"),
    GAME("/GUI/Game.fxml"),
    POST_GAME("/GUI/PostGameScene.fxml");


    private final Parent root;

    Root(String fxmlPath) {
        try {
            this.root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            //Set bg color
            //root.setStyle("-fx-background-color: #FF0000;");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Parent getRoot() {
        return root;
    }
}
