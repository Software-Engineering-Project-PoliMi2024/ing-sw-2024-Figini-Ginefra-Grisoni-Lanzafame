package it.polimi.ingsw.view.GUI.Controllers;

import it.polimi.ingsw.view.GUI.Components.PreGame.LobbyGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller for the lobby SceneBuilder scene.

 */
public class LobbyControllerGUI implements Initializable {
    /**
     * The container of the lobby.
     */
    @FXML
    VBox lobbyContainer;

    /**
     * The lobby display.

     */
    LobbyGUI lobbyDisplay = new LobbyGUI();

    /**
     * This method is called when the controller is initialized.
     * It adds the lobby display to the container.
     * @param url the url
     * @param resourceBundle the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lobbyContainer.getChildren().add(lobbyDisplay.getLobbyDisplay());
    }
}
