package it.polimi.ingsw.view.GUI.Controllers;

import it.polimi.ingsw.view.GUI.Components.PreGame.LobbyGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyControllerGUI implements Initializable {
    @FXML
    VBox lobbyContainer;
    LobbyGUI lobbyDisplay = new LobbyGUI();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lobbyContainer.getChildren().add(lobbyDisplay.getLobbyDisplay());
    }
}
