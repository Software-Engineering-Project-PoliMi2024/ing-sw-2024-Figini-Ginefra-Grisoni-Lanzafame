package it.polimi.ingsw.view.GUI.Controllers;

import it.polimi.ingsw.view.GUI.Components.PreGame.LobbyGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyControllerGUI implements Initializable {
    @FXML
    AnchorPane lobby;
    LobbyGUI lobbyDisplay = new LobbyGUI();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lobby.getChildren().add(lobbyDisplay.getLobbyDisplay());
    }
}
